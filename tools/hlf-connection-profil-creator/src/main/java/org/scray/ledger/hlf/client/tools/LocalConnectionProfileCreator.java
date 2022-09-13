package org.scray.ledger.hlf.client.tools;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.scray.ledger.hlf.connectionprofile.nodes.CertLoader;
import org.scray.ledger.hlf.connectionprofile.nodes.Organisation;
import org.scray.ledger.hlf.connectionprofile.nodes.Peer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LocalConnectionProfileCreator
{

    private static final Logger logger = LoggerFactory.getLogger(LocalConnectionProfileCreator.class);

    public static void main(String[] args)
    {

        OptionParser parser = new OptionParser();
        OptionParameters parms;
        try
        {
            parms = parser.parseParameters(args);

            // Configure peers node
            CertLoader certLoader = new CertLoader();
            String cert = certLoader.readFromFile(parms.getCaCertpath());
            Peer peer = new Peer(parms.getPeerName(), "grpcs://" + parms.getPeerHostname() + ":" + parms.getPeerChaincodePort(), cert, parms.getPeerHostname());

            // Configure organizations node
            Organisation org = new Organisation(parms.getPeerName(), parms.getPeerName() + "MSP",
                    Arrays.asList(parms.getPeerHostname()),
                    Arrays.asList("tlsca." + parms.getPeerName() + "." + parms.getPeerHostname()));

            ConnectionProfileCreator prof = new ConnectionProfileCreator();
            ObjectNode peerRawDoc = null;

            try
            {
                peerRawDoc = prof.readTemplate("src/main/resources/connection.yaml");

                peerRawDoc.set("peers", peer.getJsonNode());

                // Set org name
                ((ObjectNode)peerRawDoc.get("client")).put("organization", parms.getPeerName());
                peerRawDoc.set("organizations", org.getJsonNode());

                // Update grpc options
                ((ObjectNode)peerRawDoc.get("grpcOptions")).put(
                        "ssl-target-name-override",
                        "peer0." + parms.getPeerName() + "." + parms.getPeerHostname());

                ((ObjectNode)peerRawDoc.get("grpcOptions")).put(
                        "hostnameOverride",
                        "peer0." + parms.getPeerName() + "." + parms.getPeerHostname());

                YAMLMapper mapper = new YAMLMapper();
                mapper.configure(YAMLGenerator.Feature.LITERAL_BLOCK_STYLE, true);

                String updatedConfiguration = mapper.writeValueAsString(peerRawDoc);

                logger.debug("Write connection profile to {}.", parms.getOutPath());

                BufferedWriter writer = new BufferedWriter(new FileWriter(parms.getOutPath()));
                try
                {
                    writer.write(updatedConfiguration);
                    writer.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    writer.close();
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        catch (ParseException e1)
        {
            logger.error("Error: {}", e1.getMessage());
            System.exit(0);
        }

    }

}
