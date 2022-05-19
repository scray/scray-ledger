/*
 * BlockchainWriter.java
 *
 * created at 2021-07-01 by st.obermeier <YOURMAILADDRESS>
 *
 * Copyright (c) SEEBURGER AG, Germany. All Rights Reserved.
 */
package org.scray.hyperledger.fabric.example.app;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import javax.json.JsonArray;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;


import  com.google.gson.JsonParser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class BlockchainOperations
{
    String channel = "mychannel";
    String smartContract = "basic";
    String walletPathString = "";
    String userName = "";
    Gateway gateway = null;

    JsonMapper j = null;

    public BlockchainOperations(String channel, String smartContract, String userName, String walletPath)
    {
        super();
        this.channel = channel;
        this.smartContract = smartContract;
        this.walletPathString = walletPath;
        this.j = new JsonMapper();
        this.userName = userName;
    }


    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");
    }

    public String write(String id) {

        String resultString = "OK";

            try
            {
                if(gateway == null) {
                    gateway = connect(userName);
                }

                // get the network and contract
                Network network = gateway.getNetwork(channel);
                Contract contract = network.getContract(smartContract);

                contract.submitTransaction("CreateAsset",
                        id,
                            "12"
                        );
            }
            catch (Exception e)
            {
                e.printStackTrace();
                resultString = e.toString();
            }

            return resultString;
    }

    public String read(String methodName) {
        String data = "{}";

            try
            {
                if(gateway == null) {
                    gateway = connect(userName);
                }
                // get the network and contract
                Network network = gateway.getNetwork(channel);
                Contract contract = network.getContract("basic");


                data = new String(contract.evaluateTransaction(methodName)).replace("\\\\", "");

                contract.addContractListener(new ContractListener());
                network.addBlockListener(new MyBlockListener());


                JsonParser jsonObject = new JsonParser();//.parse("{\"name\": \"John\"}").getAsJsonObject();
                com.google.gson.JsonArray record = jsonObject.parse(data).getAsJsonArray();


                System.out.println("ddddddddddddd" + record.get(0).getAsJsonObject().get("Record")); //John
                System.out.println();


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


        return data;
    }

    // helper function for getting connected to the gateway
    public Gateway connect(String userName) throws Exception{
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get(walletPathString);


        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path networkConfigPath = Paths.get(walletPathString + File.separator + "connection.yaml");
        System.out.println(networkConfigPath);
        Gateway.Builder builder = Gateway.createBuilder();
        System.out.println("Wallet path: " + walletPathString + "\t" + wallet.list());
        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }
}



