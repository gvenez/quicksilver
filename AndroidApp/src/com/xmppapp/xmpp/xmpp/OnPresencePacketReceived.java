package com.xmppapp.xmpp.xmpp;

import com.xmppapp.xmpp.entities.Account;
import com.xmppapp.xmpp.xmpp.stanzas.PresencePacket;

public interface OnPresencePacketReceived extends PacketReceived {
	public void onPresencePacketReceived(Account account, PresencePacket packet);
}
