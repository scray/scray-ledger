package org.scray.ledger.rest.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class CLIOptionsParser
{
    private static Options options = new Options();

    public CLIOptions parseParameters(String[] args)
        throws ParseException
    {

        options.addOption("w", "wallet", true, "Path where the wallet is located.");

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

        CLIOptions params = new CLIOptions();

        if (cmd.hasOption("w"))
        {
            if (cmd.getOptionValue("w") != null)
            {
                params.setWalletPath(cmd.getOptionValue("w"));
            }
            else
            {
                throw new ParseException("parameter --wallet <path> is missing");
            }
        } else {
            throw new ParseException("parameter --wallet <path> is missing");
        }

        return params;
    }


    private static void help()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Scray Rest Event API", options);
        System.exit(0);
    }

}
