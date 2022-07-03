package org.scray.hyperledger.fabric.client;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Consumer;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractEvent;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.sdk.BlockInfo;


public class BlockchainOperations
{
    String channel = "mychannel";
    String smartContract = "basic";
    String walletPathString = "";
    String userName = "";
    Gateway gateway = null;
    Optional<String> connectionProfile;

    public BlockchainOperations(String walletPath, String userName , String channel, String smartContract, Optional<String> connectionProfile)
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

    public void addEventListener(org.scray.hyperledger.fabric.client.listener.ContractListener listener) {
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

    public void addEventListener(long startEvent, Consumer<ContractEvent> listener) {
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

    public void addEventListener(org.scray.hyperledger.fabric.client.listener.BlockEventListener listener) {
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
                gateway = connect(userName,  connectionProfile);
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

    public String queryBlock(long blockNumber)
    {
        String data = "{}";

        try
        {
            if (gateway == null)
            {
                gateway = connect(userName,  connectionProfile);
            }

            // get the network and contract
            Network network = gateway.getNetwork(channel);

            BlockInfo block = network.getChannel().queryBlockByNumber(blockNumber);

           block.getBlock().getAllFields().values().stream().forEach(s -> System.out.println(s));



            return data;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return data;
    }
    public String readAndShowSize(String methodName)
    {
        String data = "{}";

        try
        {
            if (gateway == null)
            {
                gateway = connect(userName,  connectionProfile);
            }

            // get the network and contract
            Network network = gateway.getNetwork(channel);
            Contract contract = network.getContract(smartContract);

            data = new String(contract.evaluateTransaction(methodName));

            return  data.getBytes().length + "";

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
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get(walletPathString);

        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path networkConfigPath = Paths.get(walletPathString + File.separator + connectionProfile.orElse("connection.yaml"));
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(true);

        return builder.connect();
    }
}
