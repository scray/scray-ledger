# Hyperledger Fabric Samples ERC-1155 token scenario
[Original implementation]((https://github.com/hyperledger/fabric-samples/tree/main/token-erc-1155))


```bash
PEER_NAME=peer403
CHANNEL_NAME=channel-erc-1155
SHARED_FS=10.15.130.111
```

## Create Peer [if not exists]
```bash
cd ../../installer/
./scray-ledger.sh create-peer -n $PEER_NAME
cd ../chaincode/erc-1155/
```

## Deploy chaincode
```bash
./deploy-cc.sh --peer-name peer403 --channel-name channel-erc-1155 --share 10.15.130.111
```
