package com.xmppapp.xmpp.xmpp.stanzas.streammgmt;

import com.xmppapp.xmpp.xmpp.stanzas.AbstractStanza;

public class ResumePacket extends AbstractStanza {

	public ResumePacket(String id, int sequence, int smVersion) {
		super("resume");
		this.setAttribute("xmlns", "urn:xmpp:sm:" + smVersion);
		this.setAttribute("previd", id);
		this.setAttribute("h", "" + sequence);
	}

}
