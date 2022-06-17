package org.scray.ledger.rest.modules;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Event
{
    @JsonProperty("name")
    private String name;

    @JsonProperty("chaincodeId")
    private String chaincodeId;

    @JsonProperty("eventPayload")
    private byte[] eventPayload;


    public Event(String name, String chaincodeId, byte[] eventPayload)
    {
        super();
        this.name = name;
        this.chaincodeId = chaincodeId;
        this.eventPayload = eventPayload;
    }

    /**
     * Get the name of the event emitted by the contract.
     * @return An event name.
     */
    String getName() {
        return name;
    }

    /**
     * Get the identifier of the chaincode that emitted the event.
     * @return A chaincode ID.
     */
    String getChaincodeId() {
        return chaincodeId;
    }


    /**
     * Any binary data associated with this event by the chaincode.
     * @return A binary payload.
     */
    byte[] getPayload() {
        System.out.println("Payload" + eventPayload);
        return eventPayload;
    }
}



