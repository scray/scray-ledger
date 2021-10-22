package org.scray.ledger.hlf.chaincode.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ConnectionJsonCreatorTests {

    @Test
    public void shouldCreateJsonWithHostAndPort() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ConnectionJsonCreator cr = new ConnectionJsonCreator();
        var connection = cr.createConnectionDescription("example.com", 4711);
        String jsonRepresentation = mapper.writeValueAsString(connection);

        Assert.assertTrue(jsonRepresentation.contains("\"address\":\"example.com:4711\""));
        Assert.assertTrue(jsonRepresentation.contains("\"tls_required\":false"));
        Assert.assertTrue(jsonRepresentation.contains("\"dial_timeout\":\"10s\""));
    }

    @Test
    public void writeAsJsonFile() {
        var cr = new ConnectionJsonCreator();
        var connection = cr.createConnectionDescription("example.com", 4711);

        try {
            cr.writeConnectionToFile("target/connection.json", connection);
        } catch (IOException e) {
            Assert.fail("IOException occurred");
            e.printStackTrace();
        }

    }
}
