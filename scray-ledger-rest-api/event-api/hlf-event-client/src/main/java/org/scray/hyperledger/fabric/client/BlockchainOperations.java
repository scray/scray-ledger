package org.scray.hyperledger.fabric.client;


import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractEvent;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.common.Common.BlockData;
import org.hyperledger.fabric.protos.common.Common.Payload;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;
import org.hyperledger.fabric.protos.peer.TransactionPackage;
import org.hyperledger.fabric.protos.peer.TransactionPackage.Transaction;

import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeInfo.IdentitiesInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hyperledger.fabric.sdk.BlockInfo.TransactionEnvelopeInfo;
import org.hyperledger.fabric.sdk.BlockchainInfo;
import org.hyperledger.fabric.sdk.TxReadWriteSetInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import com.google.protobuf.ByteString;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat.Parser;


public class BlockchainOperations
{
    String channel = "mychannel";
    String smartContract = "basic";
    String walletPathString = "";
    String userName = "";
    Gateway gateway = null;
    Optional<String> connectionProfile;

    private static final Logger log = LoggerFactory.getLogger(BlockchainOperations.class);

    public BlockchainOperations(String walletPath, String userName, String channel, String smartContract,
                                Optional<String> connectionProfile)
    {
        this.channel = channel;
        this.smartContract = smartContract;
        this.walletPathString = walletPath;
        this.userName = userName;
        this.connectionProfile = connectionProfile;
    }

    static
    {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");
    }

    public void addEventListener(org.scray.hyperledger.fabric.client.listener.ContractListener listener)
    {
        if (gateway == null)
        {
            try
            {
                gateway = connect(userName, connectionProfile);

                // get the network and contract
                Network network = gateway.getNetwork(channel);
                Contract contract = network.getContract(smartContract);

                contract.addContractListener(listener);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public void addEventListener(long startEvent, Consumer<ContractEvent> listener)
    {
        if (gateway == null)
        {
            try
            {
                gateway = connect(userName, connectionProfile);

                // get the network and contract
                Network network = gateway.getNetwork(channel);
                Contract contract = network.getContract(smartContract);

                contract.addContractListener(startEvent, listener);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public void addEventListener(org.scray.hyperledger.fabric.client.listener.BlockEventListener listener)
    {
        if (gateway == null)
        {
            try
            {
                gateway = connect(userName, connectionProfile);

                // get the network and contract
                Network network = gateway.getNetwork(channel);
                network.addBlockListener(1L, listener);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    public void write(String id, String value)
    {

        try
        {
            if (gateway == null)
            {
                gateway = connect(userName, connectionProfile);
            }

            // get the network and contract
            Network network = gateway.getNetwork(channel);
            Contract contract = network.getContract(smartContract);

            contract.submitTransaction("CreateAsset",
                                       id,
                                       value,
                                       "data1");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public String read(String methodName)
    {
        String data = "{}";

        try
        {
            if (gateway == null)
            {
                gateway = connect(userName, connectionProfile);
            }

            // get the network and contract
            Network network = gateway.getNetwork(channel);

            network.getChannel().queryBlockByNumber(0);

            Contract contract = network.getContract(smartContract);

            data = new String(contract.evaluateTransaction(methodName));

            return data;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return data;
    }


    public Block queryBlock(long blockNumber)
    {
        Block data = null;

        try
        {
            if (gateway == null)
            {
                gateway = connect(userName, connectionProfile);
            }

            // get the network and contract
            Network network = gateway.getNetwork(channel);

            BlockInfo block = network.getChannel().queryBlockByNumber(blockNumber);
            Block resultBlock = new Block();

            BlockchainInfo blockchainInfo = network.getChannel().queryBlockchainInfo();

            resultBlock.setPreviousHash(Hex.encodeHexString((block.getPreviousHash())));
            resultBlock.setDataHash(Hex.encodeHexString(block.getDataHash()));
            resultBlock.setChannelHeight(blockchainInfo.getHeight());
            resultBlock.setBlockNumber(block.getBlockNumber());

            for (EnvelopeInfo info : block.getEnvelopeInfos())
            {

                final String channelId = info.getChannelId();
                resultBlock.setChannel(channelId);
                resultBlock.setTransactionTime(info.getTimestamp().getTime());

                if (info.getType() == EnvelopeType.TRANSACTION_ENVELOPE)
                {
                    BlockInfo.TransactionEnvelopeInfo transactionInfos = (TransactionEnvelopeInfo)info;
                    int transaktionCount = transactionInfos.getTransactionActionInfoCount();

                    for (int i = 0; i < transaktionCount; i++)
                    {
                        BlockInfo.TransactionEnvelopeInfo.TransactionActionInfo txInfo = transactionInfos.getTransactionActionInfo(i);

                        TxReadWriteSetInfo rwsetInfo = txInfo.getTxReadWriteSet();
                        if (null != rwsetInfo)
                        {

                            for (TxReadWriteSetInfo.NsRwsetInfo nsRwsetInfo : rwsetInfo.getNsRwsetInfos())
                            {
                                KvRwset.KVRWSet rws = nsRwsetInfo.getRwset();

                                for (KvRwset.KVWrite writeList : rws.getWritesList())
                                {
                                    resultBlock.addTransactionData(new org.scray.hyperledger.fabric.client.BlockData(writeList.getKey(),
                                                                                                                     printableString(new String(writeList.getValue()
                                                                                                                                                         .toByteArray(),
                                                                                                                                                "UTF-8"))));
                                }
                            }
                        }
                    }
                }
            }

            return resultBlock;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return data;
    }


    String printableString(String string)
    {
        if (string == null || string.length() == 0)
        {
            return string;
        }
        String ret = string.replaceAll("[^\\p{Print}]", "?");
        return ret;
    }


    public String readAndShowSize(String methodName)
    {
        String data = "{}";

        try
        {
            if (gateway == null)
            {
                gateway = connect(userName, connectionProfile);
            }

            // get the network and contract
            Network network = gateway.getNetwork(channel);
            Contract contract = network.getContract(smartContract);

            data = new String(contract.evaluateTransaction(methodName));

            return data.getBytes().length + "";

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return data.getBytes().length + "";
    }


    public Gateway connect(String userName, Optional<String> connectionProfile)
        throws Exception
    {
        log.debug("Read wallet from base path " + walletPathString);
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get(walletPathString);

        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path networkConfigPath = Paths.get(walletPathString + File.separator + connectionProfile.orElse("connection.yaml"));
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(true);

        return builder.connect();
    }
}
