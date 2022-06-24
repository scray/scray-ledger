package org.scray.hyperledger.fabric.client;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.scray.hyperledger.fabric.client.listener.BlockEventListener;
import org.scray.hyperledger.fabric.client.listener.ContractListener;

public class EventSubscriptionClient implements Runnable
{
    EventBuffer buffer = new EventBuffer();
    String walletPath;
    String chanelName;
    String chaincodeName;
    String userId;
    Optional<String> connectionProfil;



    public EventSubscriptionClient(Path walletPath, String userId, String chanelName, String chaincodeName, Optional<String> connectionProfil)
    {
        super();
        this.walletPath = walletPath.toString();
        this.chaincodeName = chaincodeName;
        this.chanelName = chanelName;
        this.userId = userId;
        this.connectionProfil = connectionProfil;
    }

    public void run() {
        BlockchainOperations blockchainOperations = new BlockchainOperations(chanelName, chaincodeName, userId, walletPath, connectionProfil);
        ContractListener listener = new org.scray.hyperledger.fabric.client.listener.ContractListener(buffer);
        blockchainOperations.addEventListener(1L, listener);
    }

    public List<Event> getEvents() {
        return buffer.getEvents();
    }
}



