package org.scray.ledger.hlf.client.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.scray.ledger.hlf.connectionprofile.nodes.CertLoader;

public class LocalConnectionProfileCreatorTests {

	@Test
	public void readAndParseCert() {

		 CertLoader loader = new  CertLoader();

		 try {
			loader.readFromFile("src/test/resources/test-cert.pem");
		} catch (Exception e) {
			Assertions.fail("Unexpected exception while loading cert" + e);
		}
	}

}
