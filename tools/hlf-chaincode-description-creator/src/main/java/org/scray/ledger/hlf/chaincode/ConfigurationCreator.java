package org.scray.ledger.hlf.chaincode;

import org.scray.ledger.hlf.chaincode.configurationfiles.Metadata;
import org.scray.ledger.hlf.chaincode.tools.ConnectionJsonCreator;
import org.scray.ledger.hlf.chaincode.tools.MetadataCreator;

import java.io.File;
import java.io.IOException;

public class ConfigurationCreator {

    public static void main(String[] args) throws IOException {

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        String label = args[2];

        var configFilePath = getConfigurationPath("target/", hostname + "_" + label);

        var cr = new ConnectionJsonCreator();
        var connection = cr.createConnectionDescription(hostname, port);
        cr.writeConnectionToFile(hostname, configFilePath, connection);

        new MetadataCreator().createMetadataFile(label, configFilePath);
    }

    public static  String getConfigurationPath(String basePath, String contractId) {
        var destinationPath = "";

        if(basePath.endsWith("/")) {
            destinationPath = basePath + contractId;
            var idFolder = new File(basePath + contractId);
            idFolder.mkdir();
        } else {
            destinationPath = basePath + "/" + contractId;
            var idFolder = new File(basePath + "/" + contractId);
            idFolder.mkdir();
        }

        return  destinationPath;
    }
}
