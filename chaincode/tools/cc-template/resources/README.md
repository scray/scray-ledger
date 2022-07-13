# Hyperledger Fabric Samples ERC-721 token scranrio as external service
[Original implementation]((https://github.com/hyperledger/fabric-samples/tree/main/token-erc-721))


## Deploy created smart contract
```bash
./target/$CC_NAME/cc-deploy.sh --data-share 10.15.130.111 --caincode-hostname chaincode.host.example.com
```

# Install external chaincode on k8s peer
```
PEER_NAME=Org1 # Org1 has the permission to mint nfts
CHANNEL_NAME=mychannel
ORDERER_NAME=orderer.example.com
IP_CC_SERVICE=$(kubectl get nodes -o jsonpath="{.items[0].status.addresses[?(@.type=='InternalIP')].address}")         # Host where the chaincode is running
PEER_POD=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
ORDERER_IP=$(kubectl get pods  -l app=orderer-org1-scray-org -o jsonpath='{.items[*].status.podIP}')
ORDERER_LISTEN_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
ORDERER_HOST=orderer.example.com
EXT_PEER_IP=$(kubectl get nodes -o jsonpath="{.items[0].status.addresses[?(@.type=='InternalIP')].address}") 
```

```
ORDERER_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
ORDERER_PORT=30081
ORDERER_IP=$(kubectl get pods  -l app=orderer-org1-scray-org -o jsonpath='{.items[*].status.podIP}')

# PKGID=basic_1.0:fd7a1dd538bca88611519d55085d7dcc59218bfcdfc32d1d1adc7f9359e69240
CC_HOSTNAME=hl-fabric-erc-721-example.example.com
CC_LABEL=basic_1.0

kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh \
    /mnt/conf/install_and_approve_cc.sh \
        $IP_CC_SERVICE \
        $ORDERER_IP \
        $ORDERER_HOST \
        $ORDERER_PORT \
        $CHANNEL_NAME \
        $PKGID \
        $CC_HOSTNAME \
        $CC_LABEL \
        $SHARED_FS
```


Commit chaincode
```
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/cc_commit.sh  $CHANNEL_NAME $PKGID
```

# Mint token and show new balance
```
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/examples/cc-erc-721-mint-token.sh $CHANNEL_NAME $PKGID
```
[Request details](../../containers/hl-fabric-node-configurator/conf/peer/examples/cc-erc-721-mint-token.sh)