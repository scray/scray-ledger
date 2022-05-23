package org.scray.hyperledger.fabric.example.app.events;

import java.util.function.Consumer;

import org.hyperledger.fabric.gateway.ContractEvent;
import org.hyperledger.fabric.sdk.BlockEvent;

public class BlockListener implements Consumer<BlockEvent>
{

    @Override
    public void accept(BlockEvent event)
    {
      System.out.println("New Block event:  " + event.getBlock());

    }

}



