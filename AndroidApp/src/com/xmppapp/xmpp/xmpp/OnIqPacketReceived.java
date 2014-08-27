package com.xmppapp.xmpp.xmpp;

import com.xmppapp.xmpp.entities.Account;
import com.xmppapp.xmpp.xmpp.stanzas.IqPacket;

public interface OnIqPacketReceived extends PacketReceived {
	public void onIqPacketReceived(Account account, IqPacket packet);
}
