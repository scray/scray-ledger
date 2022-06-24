package org.scray.hyperledger.fabric.client;

import java.util.ArrayList;
import java.util.Optional;

import org.hyperledger.fabric.gateway.ContractEvent;
import org.hyperledger.fabric.sdk.ChaincodeEvent;

public class EventBuffer
{
    ArrayList<Event> events = new ArrayList<Event>();

    public void addEvent(ContractEvent event) {
        events.add(new Event(event.getName(), event.getChaincodeId(), event.getPayload(), event.getTransactionEvent().getBlockEvent().getBlockNumber()));
        System.out.println(events.size() + "\t" + event.getTransactionEvent().getBlockEvent().getBlockNumber() + "");
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}



