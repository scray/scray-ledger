/*
 * Client.java
 *
 * created at 2022-06-03 by st.obermeier <YOURMAILADDRESS>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package org.scray.ledger.rest;

import org.scray.hyperledger.fabric.example.app.event.buffer.Client;

public class EventListnerClient
{

    public static void main(String[] args)
    {

        //String walletPath = "/home/ubuntu/wallet/";
        String walletPath = "C:\\Users\\st.obermeier\\git\\scray-ledger\\applications\\asset-reader-writer-app\\wallet";

        Client client = new Client(walletPath);

        client.run();

        for (int i = 0; i < 1000; i++)
        {
            System.out.println("Event count  " + client.getEvents().size());

            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }

}



