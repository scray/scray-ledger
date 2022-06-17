package org.scray.ledger.rest.modules;


public class Subscription
{
    private String chanelName;
    private String chaincodeId;

    public Subscription(String chanelName, String chaincodeId)
    {
        super();
        this.chanelName = chanelName;
        this.chaincodeId = chaincodeId;
    }

    public String getChanelName()
    {
        return chanelName;
    }
    public String getChaincodeId()
    {
        return chaincodeId;
    }


}



