package org.scray.ledger.rest.modules;


public class Subscription
{
    private String chanelName;
    private String chaincodeId;
    private String userId;
    private String wallet;
    private String connectionProfil;

    public Subscription(String chanelName, String chaincodeId, String userId, String wallet, String connectionProfil)
    {
        super();
        this.chanelName = chanelName;
        this.chaincodeId = chaincodeId;
        this.userId = userId;
        this.wallet = wallet;
        this.connectionProfil = connectionProfil;
    }

    public String getChanelName()
    {
        return chanelName;
    }
    public String getChaincodeId()
    {
        return chaincodeId;
    }
    public String getUserId()
    {
        return userId;
    }
    public String getWallet()
    {
        return wallet;
    }
    public String getConnectionProfil()
    {
        return connectionProfil;
    }

    @Override
    public String toString()
    {
        return "{\"chanelName\":\"" + chanelName + "\", \"chaincodeId\":\"" + chaincodeId + "\", \"userId\":\"" + userId
               + "\", \"wallet\":\"" + wallet + "\", \"connectionProfil\":\"" + connectionProfil + "\"}";
    }
}



