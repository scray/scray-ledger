package org.scray.scray_ledger.java_fabric_gateway_client;

import java.time.Instant;

import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.Network;
import org.hyperledger.fabric.client.Status;
import org.hyperledger.fabric.client.SubmitException;
import org.hyperledger.fabric.client.SubmittedTransaction;
import org.hyperledger.fabric.client.Gateway;

public class App
{

    private static final String channelName = "mychannel";
    private static final String chaincodeName = "events";

    private final Network network = null;
    private final Contract contract = null;
    private final String assetId = "asset" + Instant.now().toEpochMilli();

    public static void main( String[] args )
    {

        Gateway.newInstance().identity(null);

    }

    private long createAsset() throws EndorseException, SubmitException, CommitStatusException {

        System.out.println("\n--> Submit transaction: CreateAsset, " + assetId + " owned by Sam with appraised value 100");

        SubmittedTransaction commit = contract.newProposal("CreateAsset")
                .addArguments(assetId, "blue", "10", "Sam", "100")
                .build()
                .endorse()
                .submitAsync();

        Status status = commit.getStatus();
        if (!status.isSuccessful()) {
            throw new RuntimeException("failed to commit transaction with status code " + status.getCode());
        }

        System.out.println("\n*** CreateAsset committed successfully");

        return status.getBlockNumber();
    }
}
