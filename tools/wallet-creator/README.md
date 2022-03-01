## Create user wallet for a given CA

### Requirements
  * MAVEN
  * openssl  
  
## Example workflow

### Prerequisites
```DATA_SHARE=10.15.130.111```

### App side
```cd scray-ledger/tools/wallet-creator```
* ```./cert-creator.sh create_csr --common-name otto```
* ```./cert-creator.sh push_csr --common-name otto --shared-fs-host $DATA_SHARE```
* GOTO Peer side
* ```./cert-creator.sh pull_signed_crt --common-name otto --shared-fs-host $DATA_SHARE```
* ```./cert-creator.sh create_wallet --common-name otto --mspId peer2MSP``` 


### Peer side
```
PEER_POD=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh
```
* ```/mnt/tools/wallet-creator/cert-creator.sh pull_csr --common-name otto --shared-fs-host $DATA_SHARE```

* ```
  CA_CERT=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/ca/ca.*.pem
  CA_KEY=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/ca/priv_sk
  
  /mnt/tools/wallet-creator/cert-creator.sh sign_csr --common-name otto --cacert $CA_CERT --cakey $CA_KEY
   ```


* ```
  /mnt/tools/wallet-creator/cert-creator.sh push_crt --common-name otto --shared-fs-host $DATA_SHARE
  ````
* GOTO App side
