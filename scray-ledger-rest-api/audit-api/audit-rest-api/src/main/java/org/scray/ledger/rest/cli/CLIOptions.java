package org.scray.ledger.rest.cli;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CLIOptions
{
    private Path walletPath = Paths.get(System.getProperty("user.home") + "/wallet");



    public Path getWalletPath()
    {
        return walletPath;
    }

    public void setWalletPath(String walletPath)
    {
        this.walletPath = Paths.get(walletPath);
    }



}



