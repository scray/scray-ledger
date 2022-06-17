/*
 * Client.java
 *
 * created at 2022-06-02 by st.obermeier <YOURMAILADDRESS>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package org.scray.hyperledger.fabric.example.app.event.buffer;

import java.util.List;

import org.scray.hyperledger.fabric.example.app.BlockchainOperations;

public class Client
{
    EventBuffer buffer = new EventBuffer();
    String walletPath;



    public Client(String walletPath)
    {
        super();
        this.walletPath = walletPath;
    }

    public void run() {
        //BlockchainOperations blockchainOperations = new BlockchainOperations("channel-1", "basic", "alice", walletPath);
        BlockchainOperations blockchainOperations = new BlockchainOperations("invoicing28", "basic", "blog-rest-client", walletPath);
        ContractListener listener = new org.scray.hyperledger.fabric.example.app.event.buffer.ContractListener(buffer);
        blockchainOperations.addEventListener(listener);
    }

    public List<Event> getEvents() {
        return buffer.getEvents();
    }
}



