## Create user wallet for a given CA

### Requirements
  * MAVEN
  * openssl


### Prerequisites

``DATA_SHARE=10.15.130.111 ``  
``WALLET_COMMON_NAME=alice``  
``PEER_NAME=peer200``  

### App side

* ``cd scray-ledger/tools/wallet-creator``
* ``./cert-creator.sh create_csr --common-name $WALLET_COMMON_NAME --shared-fs-host $DATA_SHARE``
* GOTO [Peer side](#peer-side)
* ``./cert-creator.sh create_wallet --common-name $WALLET_COMMON_NAME --mspId ${PEER_NAME}MSP``

The wallet is stored in ./wallet/$WALLET_COMMON_NAME.id

### Peer side
```
PEER_POD=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh
```

* ``WALLET_COMMON_NAME=alice``
* ``DATA_SHARE=10.15.130.111``

  * ```
    CA_CERT=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/ca/ca.*.pem
    CA_KEY=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/ca/priv_sk
    /mnt/tools/wallet-creator/cert-creator.sh sign_csr --common-name $WALLET_COMMON_NAME --cacert $CA_CERT --cakey $CA_KEY  --shared-fs-host $DATA_SHARE
    ```

* ``/mnt/tools/wallet-creator/cert-creator.sh push_crt --common-name $WALLET_COMMON_NAME --shared-fs-host $DATA_SHARE``
* GOTO [App side](#app-side)
