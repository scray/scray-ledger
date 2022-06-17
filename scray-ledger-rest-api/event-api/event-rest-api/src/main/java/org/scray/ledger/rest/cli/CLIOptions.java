package org.scray.ledger.rest.cli;

public class CLIOptions
{
    private String walletPath = System.getProperty("user.home") + "/wallet";



    public String getWalletPath()
    {
        return walletPath;
    }

    public void setWalletPath(String walletPath)
    {
        this.walletPath = walletPath;
    }



}



