/*
 * Client.java
 *
 * created at 2022-06-03 by st.obermeier <YOURMAILADDRESS>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package org.scray.ledger.rest;

import java.nio.file.Paths;
import java.util.Optional;

import org.scray.hyperledger.fabric.client.EventSubscriptionClient;


public class EventListnerClient
{

    public static void main(String[] args)
    {

        //String walletPath = "/home/ubuntu/wallet/";
        String walletPath = "C:\\Users\\st.obermeier\\git\\scray-ledger\\applications\\asset-reader-writer-app\\wallet";

        EventSubscriptionClient client = new EventSubscriptionClient(Paths.get( walletPath), "alice", "channel-1", "basic", Optional.of("connection33.yaml"));

        client.run();

        for (int i = 0; i < 1000; i++)
        {
            System.out.println("Event count  ");

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



