
package org.scray.hyperledger.fabric.example.app.events;


import java.util.Date;
import java.util.Random;

import org.scray.hyperledger.fabric.example.app.BlockchainOperations;


public class EventConsumerApp
{
    public static void main(String[] args)
        throws Exception
    {
        String walletPath = "wallet";

        BlockchainOperations blockchainOperations = new BlockchainOperations("channel-1", "basic", "alice", walletPath);

        //blockchainOperations.addEventListener();

        while(true) {
        blockchainOperations.write(
                                   "key " + (new Date()).toString(),
                                   "value" + new Random().nextInt());

        Thread.sleep(1000);

        }

        //Thread.sleep(5000);

    }
}
