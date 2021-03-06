package org.scray.ledger.hlf.client.tools;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class OptionParser
{

    private static Options options = new Options();

    public OptionParameters parseParameters(String[] args)
        throws ParseException
    {

        options.addOption("n", "peer-name", true, "Name of peer which will be part of the connection profile");
        options.addOption("h", "peer-hostname", true, "Hostname of peer which will be part of the connection profile");
        options.addOption("p", "peer-chaincode-port", true, "Port peer-chaincode of peer service");
        options.addOption("c", "ca-cert-path", true, "Path to ca pem");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(options, args);
        }
        catch (Exception e)
        {
            System.err.println("Error while parsing input parameters. " + e.getLocalizedMessage());
            help();
        }


        OptionParameters params = new OptionParameters();

        if (cmd.hasOption("n"))
        {
            if (cmd.getOptionValue("n") != null)
            {
                params.setPeerName(cmd.getOptionValue("n"));
            }
            else
            {
                throw new ParseException("parameter --peer-name <name> is missing");
            }
        }
        else
        {
            throw new ParseException("parameter --peer-name <name> is missing");
        }

        if (cmd.hasOption("p"))
        {
            if (cmd.getOptionValue("p") != null)
            {
                params.setPeerChaincodePort(Integer.parseInt(cmd.getOptionValue("p")));
            }
            else
            {
                throw new ParseException("parameter --peer-chaincode-port <port> is missing");
            }
        }
        else
        {
            throw new ParseException("parameter --peer-chaincode-port <port> is missing");
        }

        if (cmd.hasOption("h"))
        {
            if (cmd.getOptionValue("h") != null)
            {
                params.setPeerHostname(cmd.getOptionValue("h"));
            }
            else
            {
                throw new ParseException("parameter --peer-hostname <hostname> is missing");
            }
        }
        else
        {
            throw new ParseException("parameter --peer-hostname <hostname> is missing");
        }

        if (cmd.hasOption("c"))
        {
            if (cmd.getOptionValue("c") != null)
            {
                params.setCaCertpath(cmd.getOptionValue("c"));
            }
            else
            {
                throw new ParseException("parameter --ca-cert-path <path> is missing");
            }

        }

        return params;
    }


    private static void help()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Scray HLF Connection profile creator", options);
        System.exit(0);
    }

}
