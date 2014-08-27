package com.xmppapp.xmpp.parser;

import com.xmppapp.xmpp.crypto.PgpEngine;
import com.xmppapp.xmpp.entities.Account;
import com.xmppapp.xmpp.entities.Contact;
import com.xmppapp.xmpp.entities.Conversation;
import com.xmppapp.xmpp.entities.Presences;
import com.xmppapp.xmpp.generator.PresenceGenerator;
import com.xmppapp.xmpp.services.XmppConnectionService;
import com.xmppapp.xmpp.xml.Element;
import com.xmppapp.xmpp.xmpp.OnPresencePacketReceived;
import com.xmppapp.xmpp.xmpp.stanzas.PresencePacket;

public class PresenceParser extends AbstractParser implements
		OnPresencePacketReceived {

	public PresenceParser(XmppConnectionService service) {
		super(service);
	}

	public void parseConferencePresence(PresencePacket packet, Account account) {
		PgpEngine mPgpEngine = mXmppConnectionService.getPgpEngine();
		if (packet.hasChild("x", "http://jabber.org/protocol/muc#user")) {
			Conversation muc = mXmppConnectionService.find(account, packet
					.getAttribute("from").split("/")[0]);
			if (muc != null) {
				muc.getMucOptions().processPacket(packet, mPgpEngine);
			}
		} else if (packet.hasChild("x", "http://jabber.org/protocol/muc")) {
			Conversation muc = mXmppConnectionService.find(account, packet
					.getAttribute("from").split("/")[0]);
			if (muc != null) {
				muc.getMucOptions().processPacket(packet, mPgpEngine);
			}
		}
		mXmppConnectionService.updateConversationUi();
	}

	public void parseContactPresence(PresencePacket packet, Account account) {
		PresenceGenerator mPresenceGenerator = mXmppConnectionService
				.getPresenceGenerator();
		if (packet.getFrom() == null) {
			return;
		}
		String[] fromParts = packet.getFrom().split("/");
		String type = packet.getAttribute("type");
		if (fromParts[0].equals(account.getJid())) {
			if (fromParts.length == 2) {
				if (type == null) {
					account.updatePresence(fromParts[1],
							Presences.parseShow(packet.findChild("show")));
				} else if (type.equals("unavailable")) {
					account.removePresence(fromParts[1]);
				}
			}

		} else {
			Contact contact = account.getRoster().getContact(packet.getFrom());
			if (type == null) {
				if (fromParts.length == 2) {
					int sizeBefore = contact.getPresences().size();
					contact.updatePresence(fromParts[1],
							Presences.parseShow(packet.findChild("show")));
					PgpEngine pgp = mXmppConnectionService.getPgpEngine();
					if (pgp != null) {
						Element x = packet.findChild("x", "jabber:x:signed");
						if (x != null) {
							Element status = packet.findChild("status");
							String msg;
							if (status != null) {
								msg = status.getContent();
							} else {
								msg = "";
							}
							contact.setPgpKeyId(pgp.fetchKeyId(account, msg,
									x.getContent()));
						}
					}
					boolean online = sizeBefore < contact.getPresences().size();
					updateLastseen(packet, account, true);
					mXmppConnectionService.onContactStatusChanged
							.onContactStatusChanged(contact, online);
				}
			} else if (type.equals("unavailable")) {
				if (fromParts.length != 2) {
					contact.clearPresences();
				} else {
					contact.removePresence(fromParts[1]);
				}
				mXmppConnectionService.onContactStatusChanged
						.onContactStatusChanged(contact, false);
			} else if (type.equals("subscribe")) {
				if (contact.getOption(Contact.Options.PREEMPTIVE_GRANT)) {
					mXmppConnectionService.sendPresencePacket(account,
							mPresenceGenerator.sendPresenceUpdatesTo(contact));
				} else {
					contact.setOption(Contact.Options.PENDING_SUBSCRIPTION_REQUEST);
				}
			}
		}
		mXmppConnectionService.updateRosterUi();
	}

	@Override
	public void onPresencePacketReceived(Account account, PresencePacket packet) {
		if (packet.hasChild("x", "http://jabber.org/protocol/muc#user")) {
			this.parseConferencePresence(packet, account);
		} else if (packet.hasChild("x", "http://jabber.org/protocol/muc")) {
			this.parseConferencePresence(packet, account);
		} else {
			this.parseContactPresence(packet, account);
		}
	}

}
