package org.scray.hyperledger.fabric.client;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Block
{

    @JsonProperty("channel")
    String channel;

    @JsonProperty("channelHeight")
    Long channelHeight; // the current ledger blocks height

    @JsonProperty("blockNumber")
    Long blockNumber;

    @JsonProperty("transactionTime")
    Long transactionTime;

    @JsonProperty("previousHash")
    String previousHash;

    @JsonProperty("dataHash")
    String dataHash;

    @JsonProperty("transaktionData")
    List<BlockData> transaktionData;

    public Block() {
        super();
    }


    public Block(String previousHash, String dataHash, byte[] data, Long blockNumber, Long channelHeight)
    {
        super();
        this.previousHash = previousHash;
        this.dataHash = dataHash;
        this.blockNumber = blockNumber;
        this.channelHeight = channelHeight;
    }

    public String getPreviousHash()
    {
        return previousHash;
    }

    public void setPreviousHash(String previousHash)
    {
        this.previousHash = previousHash;
    }


    public String getDataHash()
    {
        return dataHash;
    }


    public void setDataHash(String dataHash)
    {
        this.dataHash = dataHash;
    }


    public Long getBlockNumber()
    {
        return blockNumber;
    }


    public void setBlockNumber(Long blockNumber)
    {
        this.blockNumber = blockNumber;
    }


    public Long getTransactionTime()
    {
        return transactionTime;
    }


    public void setTransactionTime(Long transactionTime)
    {
        this.transactionTime = transactionTime;
    }


    public String getChannel()
    {
        return channel;
    }


    public void setChannel(String channel)
    {
        this.channel = channel;
    }


    public void addTransactionData(BlockData transaktionData) {
        if(this.transaktionData == null) {
            this.transaktionData = new ArrayList<>();
        }

        this.transaktionData.add(transaktionData);
    }


    public List<BlockData> getTransaktionData()
    {
        return transaktionData;
    }


    public void setTransaktionData(List<BlockData> transaktionData)
    {
        this.transaktionData = transaktionData;
    }


    public Long getChannelHeight()
    {
        return channelHeight;
    }


    public void setChannelHeight(Long channelHeight)
    {
        this.channelHeight = channelHeight;
    }

}



