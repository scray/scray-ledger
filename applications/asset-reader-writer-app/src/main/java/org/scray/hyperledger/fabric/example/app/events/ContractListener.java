package org.scray.hyperledger.fabric.example.app.events;

import org.hyperledger.fabric.gateway.ContractEvent;

import java.util.function.Consumer;

public class ContractListener implements Consumer<ContractEvent> {

    @Override
    public void accept(ContractEvent contractEvent) {
        System.out.println(
                        "New contract event :  " +
                        "\tChaincode id:" +  contractEvent.getChaincodeId() +
                        "\tName: " + contractEvent.getName());
    }
}
