package org.scray.ledger.hlf.chaincode.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.scray.ledger.hlf.chaincode.configurationfiles.Metadata;

import java.io.File;
import java.io.IOException;

public class MetadataCreator {

    public void createMetadataFile(String label, String destinationPath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        var metadata = new Metadata(label);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(destinationPath + "/metadata.json"), metadata);
    }
}
