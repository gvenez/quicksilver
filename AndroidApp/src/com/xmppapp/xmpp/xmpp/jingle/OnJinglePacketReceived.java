package com.xmppapp.xmpp.xmpp.jingle;

import com.xmppapp.xmpp.entities.Account;
import com.xmppapp.xmpp.xmpp.PacketReceived;
import com.xmppapp.xmpp.xmpp.jingle.stanzas.JinglePacket;

public interface OnJinglePacketReceived extends PacketReceived {
	public void onJinglePacketReceived(Account account, JinglePacket packet);
}
