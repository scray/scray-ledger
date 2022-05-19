package org.scray.ledger.hlf.client.tools;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.scray.ledger.hlf.connectionprofile.nodes.CertLoader;
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

            CertLoader certLoader = new CertLoader();
            String cert = certLoader.readFromFile(parms.getCaCertpath());

            Peer peer = new Peer(parms.getPeerName(), "grpcs://" + parms.getPeerHostname() + ":" + parms.getPeerChaincodePort(), cert);

            ConnectionProfileCreator prof = new ConnectionProfileCreator();
            ObjectNode peerRawDoc = null;

            try
            {
                peerRawDoc = prof.readTemplate("src/main/resources/connection.yaml");
                peerRawDoc.set("peers", peer.getJsonNode());



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
