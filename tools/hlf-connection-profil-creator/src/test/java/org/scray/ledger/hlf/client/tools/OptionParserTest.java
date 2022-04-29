package org.scray.ledger.hlf.client.tools;



import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptionParserTest {

	@Test
	public void checkIfParametersAreSet() {
		OptionParser parser = new OptionParser();

		String[] options = {"--peer-name", "peer101", "--peer-hostname", "example.scray.org", "--ca-cert-path", "target/peer101.example.scray.org.tlsca.pem"};

		try {
			 OptionParameters parms = parser.parseParameters(options);
			 Assertions.assertEquals("peer101", parms.getPeerName());
			Assertions.assertEquals("example.scray.org", parms.getPeerHostname());
			Assertions.assertEquals("target/peer101.example.scray.org.tlsca.pem", parms.getCaCertpath());

		} catch (ParseException e) {
			Assertions.fail(e.toString());
		}
	}

	@Test
	public void checkMissingParameters() throws ParseException {
		OptionParser parser = new OptionParser();

		String[] options = {"--peer-name", "peer101"};

		Assertions.assertThrows(ParseException.class, () -> {parser.parseParameters(options);});
	}

}
