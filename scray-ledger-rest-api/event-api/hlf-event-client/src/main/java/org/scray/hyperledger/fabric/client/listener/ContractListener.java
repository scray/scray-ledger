package org.scray.hyperledger.fabric.client.listener;

import org.hyperledger.fabric.gateway.ContractEvent;
import org.scray.hyperledger.fabric.client.EventBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class ContractListener implements Consumer<ContractEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ContractListener.class);
    EventBuffer buf = null;

    public ContractListener(EventBuffer buf)
    {
        super();
        this.buf = buf;
    }

    @Override
    public void accept(ContractEvent contractEvent) {
        logger.debug("Store new contract event ");
        buf.addEvent(contractEvent);
    }
}
