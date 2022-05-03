package org.scray.ledger.hlf.client.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OptionParameters
{

    private String peerName;
    private String peerHostname;
    private String caCertpath;
    private String outPath = "target/connection.yaml";

    private static final Logger logger = LoggerFactory.getLogger(OptionParameters.class);

    public String getPeerName()
    {
        return peerName;
    }


    public void setPeerName(String peerName)
    {
        this.peerName = peerName;
    }


    public String getPeerHostname()
    {
        return peerHostname;
    }


    public void setPeerHostname(String peerHostname)
    {
        this.peerHostname = peerHostname;
    }


    public String getCaCertpath()
    {
        if (this.caCertpath == null)
        {
            String defaultPath = "target/" + this.getPeerHostname() + "-tlsca.pem";
            logger.debug("CA cert path not set. Default {} is used", defaultPath);
            return defaultPath;
        }
        return caCertpath;
    }


    public void setCaCertpath(String caCertpath)
    {
        this.caCertpath = caCertpath;
    }


    public String getOutPath()
    {
        return this.outPath;
    }

}
