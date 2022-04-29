/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

// Running TestApp:
// gradle runApp

package org.scray.hyperledger.fabric.example.app;

public class App {

	public static void main(String[] args) throws Exception {
		String walletPath = "C:\\Users\\st.obermeier\\git\\scray-ledger\\applications\\asset-reader-writer-app\\wallet";

	    BlockchainOperations op = new BlockchainOperations("channel111", "basic", "otto", walletPath);

		//op.write("id5");
	    String assets = op.read("GetAllAssets");
	    System.out.println("Assets:\t" + assets);
	}


}
