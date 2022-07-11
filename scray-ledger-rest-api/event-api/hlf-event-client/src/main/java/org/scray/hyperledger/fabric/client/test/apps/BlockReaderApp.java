package org.scray.hyperledger.fabric.client.test.apps;

import java.util.Optional;

import org.scray.hyperledger.fabric.client.BlockchainOperations;

public class BlockReaderApp
{

    public static void main(String[] args) throws Exception {
        String walletPath = "wallet";

        BlockchainOperations op = new BlockchainOperations(walletPath, "alice", "channel-1", "basic", Optional.of("connection33.yaml"));

        var block = op.queryBlock(1003);

        System.out.println("Cur hash " + block.getDataHash());
        System.out.println("Prev hash " + block.getPreviousHash());
        System.out.println(block.getTransaktionData());
    }

}



