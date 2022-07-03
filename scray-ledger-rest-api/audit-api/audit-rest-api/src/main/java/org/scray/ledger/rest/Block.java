package org.scray.ledger.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Block
{
    @JsonProperty("previousHash")
    String previousHash;

    @JsonProperty("blockHash")
    String blockHash;

    @JsonProperty("payload")
    byte[] payload;


    public Block(String previousHash, String blockHash, byte[] payload)
    {
        super();
        this.previousHash = previousHash;
        this.blockHash = blockHash;
        this.payload = payload;
    }

    public String getPreviousHash()
    {
        return previousHash;
    }

    public void setPreviousHash(String previousHash)
    {
        this.previousHash = previousHash;
    }

    public String getBlockHash()
    {
        return blockHash;
    }

    public void setBlockHash(String blockHash)
    {
        this.blockHash = blockHash;
    }

    public byte[] getPayload()
    {
        return payload;
    }

    public void setPayload(byte[] payload)
    {
        this.payload = payload;
    }
}



