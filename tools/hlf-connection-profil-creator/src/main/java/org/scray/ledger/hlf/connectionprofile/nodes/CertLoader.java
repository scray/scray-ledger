package org.scray.ledger.hlf.connectionprofile.nodes;

import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;


public class CertLoader {

	public String readFromFile(String filePath) {
		try {
		FileInputStream inputStream = new FileInputStream(filePath);

		CertificateFactory fact = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate) fact.generateCertificate(inputStream);

		cert.checkValidity();

		byte[] lineEnding = {'\n'};
		Base64 encoder = new Base64(64, lineEnding);
		String certBegin = "-----BEGIN CERTIFICATE-----\n";
		String endCert = "-----END CERTIFICATE-----";

		byte[] derCert = cert.getEncoded();
		String pemCertPre = new String(encoder.encode(derCert));

		return certBegin + pemCertPre + endCert ;
		} catch(Exception e) {
			throw new RuntimeException("Error while loading certificate from file. " + e.getMessage());
		}
	}

}
