package org.scray.hyperledger.fabric.client;

import java.util.Optional;

public class Event
{
    private String name;
    private String chaincodeId;

    Long blockNumber;

    private Optional<byte[]> eventPayload;


    public Event(String name, String chaincodeId, Optional<byte[]> eventPayload, Long blockNumber)
    {
        super();
        this.name = name;
        this.chaincodeId = chaincodeId;
        this.eventPayload = eventPayload;
        this.blockNumber = blockNumber;
    }

    /**
     * Get the name of the event emitted by the contract.
     * @return An event name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the identifier of the chaincode that emitted the event.
     * @return A chaincode ID.
     */
    public String getChaincodeId() {
        return chaincodeId;
    }


    /**
     * Any binary data associated with this event by the chaincode.
     * @return A binary payload.
     */
    public Optional<byte[]> getPayload() {
        System.out.println("AAD: payload" + new String(this.eventPayload.orElse("ff".getBytes())) );
        return eventPayload;
    }
}



