package org.scray.hyperledger.fabric.client.test.apps;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.scray.hyperledger.fabric.client.BlockchainOperations;

public class BlockReaderApp
{

    public static void main(String[] args) throws Exception {
        String walletPath = "wallet";

        BlockchainOperations op = new BlockchainOperations(walletPath, "alice", "channel-1", "basic", Optional.of("connection33.yaml"));

        op.queryBlock(1);
    }

}



