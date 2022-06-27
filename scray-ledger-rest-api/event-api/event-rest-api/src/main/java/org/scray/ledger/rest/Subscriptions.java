package org.scray.ledger.rest;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import org.scray.hyperledger.fabric.client.Event;
import org.scray.hyperledger.fabric.client.EventSubscriptionClient;
import org.scray.ledger.rest.modules.Subscription;

public class Subscriptions
{

    private HashMap<UUID, Subscription> subscriptions = new HashMap<UUID, Subscription>();
    private HashMap<UUID, EventSubscriptionClient> connections = new HashMap<UUID, EventSubscriptionClient>();

    public UUID addSubscription(Subscription subscription) {

        var subscriptionId = UUID.randomUUID();

        subscriptions.put(subscriptionId, subscription);
        return subscriptionId;
    }

    public void connect(UUID subscriptionId) {

        Subscription sc = subscriptions.get(subscriptionId);

        if(sc != null) {
                var client = new EventSubscriptionClient(Path.of(sc.getWallet()), sc.getChanelName(), sc.getChaincodeId(), sc.getUserId(), Optional.of(sc.getConnectionProfil()));
                connections.put(subscriptionId, client);
                client.run();


        } else {
            throw new NoSuchElementException("No subscription with id: " + subscriptionId);
        }
    }

    public List<Event> getLastEvents(UUID subscriptionId) {
        return connections.get(subscriptionId).getEvents();
    }

}



