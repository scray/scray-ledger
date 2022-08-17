# Hyperledger Fabric Samples token-utxo token scenario
[Original implementation]((https://github.com/hyperledger/fabric-samples/tree/main/token-erc-1155))


```bash
PEER_NAME=peer403
CHANNEL_NAME=channel-erc-1155
SHARED_FS=10.15.130.111
```


```bash
cd ../../installer/
./scray-ledger.sh create-peer -n $PEER_NAME
cd ../chaincode/erc-1155/
```

## Deploy chaincode
```bash
./deploy-cc.sh --peer-name $PEER_NAME --channel-name $CHANNEL_NAME --share $SHARED_FS
```
