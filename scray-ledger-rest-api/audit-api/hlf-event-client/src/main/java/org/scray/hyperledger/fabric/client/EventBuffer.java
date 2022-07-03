package org.scray.hyperledger.fabric.client;

import java.util.List;
import java.util.stream.Collectors;

import org.hyperledger.fabric.gateway.ContractEvent;

import com.google.common.collect.EvictingQueue;

public class EventBuffer
{
    EvictingQueue<Event> events = EvictingQueue.create(10);

    public void addEvent(ContractEvent event) {
        events.add(new Event(event.getName(), event.getChaincodeId(), event.getPayload().orElse("".getBytes()), event.getTransactionEvent().getBlockEvent().getBlockNumber()));
    }

    public List<Event> getLastEvents() {
        events.stream().forEach(s -> System.out.println(s));
        return events.stream().collect(Collectors.toList());
    }
}



