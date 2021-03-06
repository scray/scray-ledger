package org.scray.hyperledger.fabric.example.app;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.scray.hyperledger.fabric.example.app.events.ContractListener;


public class BlockchainOperations
{
    String channel = "mychannel";
    String smartContract = "basic";
    String walletPathString = "";
    String userName = "";
    Gateway gateway = null;

    public BlockchainOperations(String channel, String smartContract, String userName, String walletPath)
    {
        this.channel = channel;
        this.smartContract = smartContract;
        this.walletPathString = walletPath;
        this.userName = userName;
    }

    static
    {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");
    }

    public void addEventListener() {
        if (gateway == null)
        {
            try
            {
                gateway = connect(userName);

                // get the network and contract
                Network network = gateway.getNetwork(channel);
                Contract contract = network.getContract(smartContract);

                contract.addContractListener(new ContractListener());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void addEventListener(org.scray.hyperledger.fabric.example.app.event.buffer.ContractListener listener) {
        if (gateway == null)
        {
            try
            {
                gateway = connect(userName);

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

    public void write(String id, String value)
    {

        try
        {
            if (gateway == null)
            {
                gateway = connect(userName);
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
                gateway = connect(userName);
            }

            // get the network and contract
            Network network = gateway.getNetwork(channel);
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


    public Gateway connect(String userName)
        throws Exception
    {
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get(walletPathString);

        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path networkConfigPath = Paths.get(walletPathString + File.separator + "connection.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(true);

        return builder.connect();
    }
}
