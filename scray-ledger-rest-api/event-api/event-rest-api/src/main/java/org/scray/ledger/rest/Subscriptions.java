package org.scray.ledger.rest;

import java.util.HashMap;
import java.util.UUID;

import org.scray.ledger.rest.modules.Subscription;

public class Subscriptions
{

    private HashMap<UUID, Subscription> subscriptions = new HashMap<UUID, Subscription>();
    private HashMap<UUID, Subscription> connections = new HashMap<UUID, Subscription>();

    public UUID addSubscription(Subscription subscription) {

        var subscriptionId = UUID.randomUUID();

        subscriptions.put(subscriptionId, subscription);
        return subscriptionId;
    }


}



