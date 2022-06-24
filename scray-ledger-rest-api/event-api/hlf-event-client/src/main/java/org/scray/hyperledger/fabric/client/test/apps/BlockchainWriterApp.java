package org.scray.hyperledger.fabric.client.test.apps;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.scray.hyperledger.fabric.client.BlockchainOperations;

public class BlockchainWriterApp
{

    public static void main(String[] args) throws Exception {
        String walletPath = "wallet";

        BlockchainOperations op = new BlockchainOperations("channel-1", "basic", "alice", walletPath, Optional.of("connection33.yaml"));

        for(int i=0; i < 1000000; i++) {
            op.write(new Date() + "", "value" + new Random().nextInt());
            Thread.sleep(10000);
        }
    }

}



