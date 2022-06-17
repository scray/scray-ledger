package org.scray.hyperledger.fabric.example.app.event.buffer;

import org.hyperledger.fabric.gateway.ContractEvent;

import java.util.function.Consumer;

public class ContractListener implements Consumer<ContractEvent> {
    EventBuffer buf = null;


    public ContractListener(EventBuffer buf)
    {
        super();
        this.buf = buf;
    }


    @Override
    public void accept(ContractEvent contractEvent) {
        System.out.println("New contract event");
        buf.addEvent(contractEvent);
    }
}
