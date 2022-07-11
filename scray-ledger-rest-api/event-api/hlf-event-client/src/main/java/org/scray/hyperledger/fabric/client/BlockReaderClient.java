package org.scray.hyperledger.fabric.client;


import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


public class BlockReaderClient
{
    EventBuffer buffer = new EventBuffer();
    String walletPath;
    String channelName;
    String chaincodeName;
    String userId;
    Optional<String> connectionProfil;


    public BlockReaderClient(Path walletPath, String channelName, String chaincodeName, String userId, Optional<String> connectionProfil)
    {
        super();
        this.walletPath = walletPath.toString();
        this.chaincodeName = chaincodeName;
        this.channelName = channelName;
        this.userId = userId;
        this.connectionProfil = connectionProfil;
    }


    public Block getBlock(Long blockNumber)
    {
        BlockchainOperations op = new BlockchainOperations(walletPath, userId, this.channelName, this.chaincodeName, this.connectionProfil);

        var block = op.queryBlock(blockNumber);

        return block;
    }

}
