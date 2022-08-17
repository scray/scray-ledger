# Hyperledger Fabric Samples ERC-721 token scenario as external service
[Original implementation]((https://github.com/hyperledger/fabric-samples/tree/main/token-erc-721))

```bash
PEER_1=peer403
CHANNEL_NAME=channel-1
SHARED_FS=10.15.130.111
```

## Create Peer [if not exists]
```bash
cd ../../installer/
./scray-ledger.sh create-peer -n $PEER_NAME
cd ../chaincode/erc-721/
```

## Deploy chaincode
```bash
./deploy-cc.sh --peer-name $PEER_NAME --channel-name $CHANNEL_NAME --share $SHARED_FS
```
[Request details](../../containers/hl-fabric-node-configurator/conf/peer/examples/cc-erc-721-mint-token.sh)
