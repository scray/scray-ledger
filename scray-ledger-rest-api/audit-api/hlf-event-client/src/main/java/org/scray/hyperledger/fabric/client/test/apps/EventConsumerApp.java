package org.scray.hyperledger.fabric.client.test.apps;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;

import org.scray.hyperledger.fabric.client.BlockchainOperations;
import org.scray.hyperledger.fabric.client.EventBuffer;
import org.scray.hyperledger.fabric.client.listener.ContractListener;
import org.scray.hyperledger.fabric.client.listener.PrintContractListener;

public class EventConsumerApp
{
    public static void main(String[] args) throws Exception {


        String chanelName = "channel-1";
        String chaincodeName = "basic";
        String userId = "alice";
        Optional<String> connectionProfil = Optional.of("connection33.yaml");
        String walletPath = "wallet";

        BlockchainOperations blockchainOperations = new BlockchainOperations(walletPath, userId, chanelName,  chaincodeName, connectionProfil);
        blockchainOperations.addEventListener(1L, new PrintContractListener());
        blockchainOperations.addEventListener(1L, new ContractListener(new EventBuffer()));

       System.out.println("Press key to terminate programm");
       new Scanner(System.in).nextLine();

    }
}



