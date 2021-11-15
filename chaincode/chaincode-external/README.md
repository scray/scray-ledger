# Start external chaincode 
### Create Service
```
kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/chaincode-external/k8s-service-eternal-chaincode.yaml
```

### Publish chaincode definition
[How to publish a cc definition](../tools/hlf-chaincode-definition-creator/README.md)


### Get hash code form share [for testing]
```
SHARED_FS=kubernetes.research.dev.seeburger.de:30080
CC_HOSTNAME=asset-transfer-basic.org1.example.com
CC_LABEL=basic_1.0

SHARED_FS_USER=scray
SHARED_FS_PW=scray
PKGID=$(curl -s  --user $SHARED_FS_USER:$SHARED_FS_PW http://$SHARED_FS/cc_descriptions/${CC_HOSTNAME}_$CC_LABEL/description-hash.json 2>&1 | jq -r '."description-hash"')
```

### Start chaincode container
Create configuration 
```
kubectl delete configmap invoice-chaincode-external
kubectl create configmap invoice-chaincode-external \
 --from-literal=chaincode_id=$PKGID
```

Start chaincode
```
kubectl apply -f k8s-external-chaincode.yaml
```

# Install external chaincode on k8s peer
```
PEER_NAME=peer48
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
CC_HOSTNAME=asset-transfer-basic.org1.example.com
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

Call init method in chaincode
```
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/cc-basic-interaction.sh  $CHANNEL_NAME
```

# Install external chaincode on example network peers 
```
IP_CC_SERVICE=10.14.128.38         # Host where the chaincode is running
IP_OF_EXAMPLE_NETWORK=10.14.128.30 #Host where the example network is running
```


### Add hostname of external chincode host to /etc/hosts
```docker exec -it peer0.org1.example.com  /bin/sh -c "echo $IP_CC_SERVICE asset-transfer-basic.org1.example.com >> /etc/hosts"```  

```docker exec -it peer0.org2.example.com  /bin/sh -c "echo $IP_CC_SERVICE asset-transfer-basic.org1.example.com >> /etc/hosts"``` 

```docker exec -it orderer.example.com  /bin/sh -c "echo $IP_CC_SERVICE asset-transfer-basic.org1.example.com >> /etc/hosts"```

```
apk add curl zip

docker exec cli /bin/bash mkdir -p /opt/scray/scripts/
docker exec cli /bin/bash wget https://raw.githubusercontent.com/scray/scray/feature/k8s-peer/projects/invoice-hyperledger-fabric/scripts/example_network_install_and_approve_cc.sh -P /opt/scray/scripts/
docker exec cli /bin/bash chmod u+x  /opt/scray/scripts/example_network_install_and_approve_cc.sh 
docker exec cli /bin/bash /opt/scray/scripts/example_network_install_and_approve_cc.sh $IP_CC_SERVICE $IP_OF_EXAMPLE_NETWORK /opt/gopath/src/github.com/hyperledger/fabric/peer
```
### Commit chaincode
```
docker exec cli /bin/bash mkdir -p /opt/scray/scripts/
wget https://raw.githubusercontent.com/scray/scray/feature/k8s-peer/projects/invoice-hyperledger-fabric/scripts/example_network_commit_cc.sh -P /opt/scray/scripts/
docker exec cli /bin/bash /opt/scray/scripts/example_network_commit_cc.sh $IP_CC_SERVICE $IP_OF_EXAMPLE_NETWORK /opt/gopath/src/github.com/hyperledger/fabric/peer
```

### Example query
```peer chaincode query -C mychannel -n basic -c '{"function":"ReadAsset","Args":["asset1"]}'```


### Write own invoices
```
PEER_NAME=peer50
CHANNEL_NAME=c3
PEER_POD=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
INVOICE_ID=ID-$RANDOM
PRODUCT_BUYER="x509::CN=User1@kubernetes.research.dev.seeburger.de,OU=client,L=San Francisco,ST=California,C=US::CN=ca.kubernetes.research.dev.seeburger.de,O=kubernetes.research.dev.seeburger.de,L=San Francisco,ST=California,C=US"
```
#### Create invoice
```
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/add-invoice.sh  $CHANNEL_NAME $INVOICE_ID $PRODUCT_BUYER
```

#### Transfer invoice
```
NEW_OWNER="x509::CN=User1@kubernetes.research.dev.seeburger.de,OU=client,L=San Francisco,ST=California,C=US::CN=ca.kubernetes.research.dev.seeburger.de,O=kubernetes.research.dev.seeburger.de,L=San Francisco,ST=California,C=US"
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/transfer_invoice.sh  $CHANNEL_NAME $INVOICE_ID $NEW_OWNER
```

#### Read invoice
```
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/get-my-invoices.sh  $CHANNEL_NAME $INVOICE_ID
```
