# Start external chaincode 
### Create Service
```
kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/erc-721/k8s-service-external-chaincode.yaml
```

### Publish chaincode definition
[Start required container](../tools/hlf-chaincode-definition-creator/README.md#start-container)

```
CC_HOSTNAME=hl-fabric-erc-721-example.example.com
CC_SERVICE_NAME=hl-fabric-erc-721-example
CC_PORT=$(kubectl get service $CC_SERVICE_NAME -o jsonpath="{.spec.ports[?(@.name=='chaincode')].nodePort}")
CC_LABEL=basic_1.0

CC_DEPLOYER_POD=$(kubectl get pod -l app=cc-deployer -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $CC_DEPLOYER_POD -c cc-deployer -- /bin/sh /opt/create-archive.sh $CC_HOSTNAME $CC_PORT $CC_LABEL $SHARED_FS
```


### Get hash code form share [for testing]
```
SHARED_FS=10.15.130.111
CC_HOSTNAME=hl-fabric-erc-721-example.example.com
CC_LABEL=basic_1.0

SHARED_FS_USER=scray
SHARED_FS_PW=scray
PKGID=$(curl -s  --user $SHARED_FS_USER:$SHARED_FS_PW http://$SHARED_FS/cc_descriptions/${CC_HOSTNAME}_$CC_LABEL/description-hash.json 2>&1 | jq -r '."description-hash"')
```

### Start chaincode container
Create configuration 
```
kubectl delete configmap hl-fabric-erc-721-example
kubectl create configmap hl-fabric-erc-721-example \
 --from-literal=chaincode_id=$PKGID
```

Start chaincode
```
kubectl apply -f k8s-external-chaincode.yaml
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