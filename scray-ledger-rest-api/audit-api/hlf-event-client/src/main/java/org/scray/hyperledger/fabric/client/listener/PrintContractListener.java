package org.scray.hyperledger.fabric.client.listener;

import java.util.function.Consumer;

import org.hyperledger.fabric.gateway.ContractEvent;

public class PrintContractListener
implements Consumer<ContractEvent> {

    @Override
    public void accept(ContractEvent contractEvent) {
        System.out.println("New contract event {name: \"" + contractEvent.getName() + "\",  Payload: \"" + new String(contractEvent.getPayload().orElse("".getBytes())) + "\"}");
    }
}



