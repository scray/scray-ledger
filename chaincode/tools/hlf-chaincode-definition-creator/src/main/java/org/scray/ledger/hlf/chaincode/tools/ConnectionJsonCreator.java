package org.scray.ledger.hlf.chaincode.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.scray.ledger.hlf.chaincode.configurationfiles.Connection;

import java.io.File;
import java.io.IOException;

public class ConnectionJsonCreator {

    public Connection createConnectionDescription(String hostname, int port) {
        return new Connection(hostname, port);
    }

    public void writeConnectionToFile(String contractId, String destinationPath, Connection con) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(destinationPath + "/connection.json"), con);
    }


}
