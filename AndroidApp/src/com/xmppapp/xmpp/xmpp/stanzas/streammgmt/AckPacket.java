package com.xmppapp.xmpp.xmpp.stanzas.streammgmt;

import com.xmppapp.xmpp.xmpp.stanzas.AbstractStanza;

public class AckPacket extends AbstractStanza {

	public AckPacket(int sequence, int smVersion) {
		super("a");
		this.setAttribute("xmlns", "urn:xmpp:sm:" + smVersion);
		this.setAttribute("h", "" + sequence);
	}

}
