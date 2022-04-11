package org.scray.ledger.hlf.client.tools;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.scray.ledger.hlf.connectionprofile.nodes.Peer;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

public class ConnectionProfileCreatorTests {

	@Test
	public void addCutomPeer() {
		try {
			Peer peer = new Peer(
					"peer0",
					"grpcs://peer0.hlf.ledger.scray.org:31060",
					"-----BEGIN CERTIFICATE-----\nMIICsjCCAlmgAwIBAgIQErRcjKAiP8QiiwLetdUpWDAKBggqhkjOPQQDAjCBozEL\nMAkGA1UEBhMCREUxDjAMBgNVBAgTBUJhZGVuMRAwDgYDVQQHEwdCcmV0dGVuMTUw\nMwYDVQQKEyxwZWVyNjYwLmt1YmVybmV0ZXMucmVzZWFyY2guZGV2LnNlZWJ1cmdl\nci5kZTE7MDkGA1UEAxMydGxzY2EucGVlcjY2MC5rdWJlcm5ldGVzLnJlc2VhcmNo\nLmRldi5zZWVidXJnZXIuZGUwHhcNMjIwMjE4MTYwMzAwWhcNMzIwMjE2MTYwMzAw\nWjCBozELMAkGA1UEBhMCREUxDjAMBgNVBAgTBUJhZGVuMRAwDgYDVQQHEwdCcmV0\ndGVuMTUwMwYDVQQKEyxwZWVyNjYwLmt1YmVybmV0ZXMucmVzZWFyY2guZGV2LnNl\nZWJ1cmdlci5kZTE7MDkGA1UEAxMydGxzY2EucGVlcjY2MC5rdWJlcm5ldGVzLnJl\nc2VhcmNoLmRldi5zZWVidXJnZXIuZGUwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNC\nAAQfmTJEJnncWRSnnWTfBmZ2Y4b8q26E6wIoNAt34SrDetmwG+srU7EDnBW5aO07\nn0o25H3JktBExKzrXmAZQORAo20wazAOBgNVHQ8BAf8EBAMCAaYwHQYDVR0lBBYw\nFAYIKwYBBQUHAwIGCCsGAQUFBwMBMA8GA1UdEwEB/wQFMAMBAf8wKQYDVR0OBCIE\nIE+7gmKX1br1Sjh8Eua1JOPoaZa+r/WeDWfJ4xrP0uU/MAoGCCqGSM49BAMCA0cA\nMEQCIAGeK2Ejlg2eIzhTHxUNCj3DSiJVx4mzPUC+VeGE2ZVdAiARvRX5yXBjNSq4\nYgL2uDljseYB4gBWiRjYU07/5BGaUA==\n-----END CERTIFICATE-----\n"
					);

			ConnectionProfileCreator prof = new ConnectionProfileCreator();
			ObjectNode peerRawDoc = prof.readTemplate("src/main/resources/connection.yaml");
			peerRawDoc.set("peers", peer.getJsonNode());

			YAMLMapper mapper = new YAMLMapper();
			mapper.configure(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE, true);

			String updatedConfiguration = mapper.writeValueAsString(peerRawDoc);
System.out.println(updatedConfiguration);
			Assert.assertTrue(updatedConfiguration.contains("url: \"grpcs://peer0.hlf.ledger.scray.org:31060\""));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

}
