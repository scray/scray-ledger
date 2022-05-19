/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

// Running TestApp:
// gradle runApp

package org.scray.hyperledger.fabric.example.app;

import java.util.Date;
import java.util.UUID;

public class App {

	public static void main(String[] args) throws Exception {
		String walletPath = "C:\\Users\\st.obermeier\\git\\scray-ledger\\applications\\asset-reader-writer-app\\wallet";

	    BlockchainOperations op = new BlockchainOperations("channel-1", "basic", "alice", walletPath);

	       op.read("GetAllAssets");


	    for(int i=0; i < 100; i++) {
	        op.write(new Date() + "");
	          op.read("GetAllAssets");

	        Thread.sleep(10000);
	    }




	    String assets = op.read("GetAllAssets");
	    op.read("GetAllAssets");
	    op.read("GetAllAssets");

	    //System.out.println("Assets:\t" + assets);


	       while(true) {
	            //op.write(new Date() + "");
	        }
	}


}
