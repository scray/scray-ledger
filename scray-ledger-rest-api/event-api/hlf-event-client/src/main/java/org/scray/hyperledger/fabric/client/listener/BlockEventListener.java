package org.scray.hyperledger.fabric.client.listener;

import java.util.function.Consumer;

import org.hyperledger.fabric.sdk.BlockEvent;

public class BlockEventListener implements Consumer<BlockEvent>
{

    @Override
    public void accept(BlockEvent t)
    {

        System.out.println("Block event " + t);
    }

}



