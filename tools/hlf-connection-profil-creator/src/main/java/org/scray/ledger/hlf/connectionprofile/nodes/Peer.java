package org.scray.ledger.hlf.connectionprofile.nodes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class Peer {

	private String peerName;
	private String url;
	private String tlsCACerts;

	public Peer(String peerName, String url, String tlsCACerts) {
		super();
		this.peerName = peerName;
		this.url = url;
		this.tlsCACerts = tlsCACerts;
	}

	public ObjectNode getJsonNode( ) {
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode peerAttributes = mapper.createObjectNode();

		// Add url node
		peerAttributes.put("url", url);

		// Add pem data
		ObjectNode pemNode = mapper.createObjectNode();
		pemNode.set("pem", new TextNode(tlsCACerts));
		peerAttributes.set("tlsCACerts", pemNode);

		ObjectNode peerNode = mapper.createObjectNode();
		peerNode.set(peerName, peerAttributes);

		return peerNode;
	}



}
