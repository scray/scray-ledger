package org.scray.hyperledger.fabric.example.app.event.buffer;

import java.util.ArrayList;
import java.util.Optional;

import org.hyperledger.fabric.gateway.ContractEvent;
import org.hyperledger.fabric.sdk.ChaincodeEvent;

public class EventBuffer
{
    ArrayList<Event> events = new ArrayList<Event>();

    public void addEvent(ContractEvent event) {
        events.add(new Event(event.getName(), event.getChaincodeId(), event.getPayload()));
        System.out.println(events.size());
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}



