# Update signature policy

```bash
NEW_POLICY="AND('peer90MSP.member','peer91MSP.member')"
PEER_NAME=peer91
```

```bash
PEER_POD=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
```

Approve change on all peers
```bash
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh \
    /mnt/conf/update-sig-policy.sh \
        $ORDERER_HOST \
        $ORDERER_PORT \
        $CHANNEL_NAME \
        $PKGID \
        $NEW_POLICY
```

Commit changes
```bash
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/cc_commit.sh  $CHANNEL_NAME $PKGID $NEW_POLICY
```

It is now necessary approve an operation by members of peer90MSP AND peer91MSP
Pseudocode: 
```bash
peer chaincode invoke -o orderer.example.com:30081 --tls --cafile /tmp/tlsca.example.com-cert.pem -C $CHANNEL_ID
  --waitForEvent  -n basic \
  --peerAddresses peer0.peer90.kubernetes.research.dev.seeburger.de:31625 --tlsRootCertFiles /tmp/90.ca.crt \
  --peerAddresses peer0.peer91.kubernetes.research.dev.seeburger.de:32119 --tlsRootCertFiles /tmp/91.ca.crt \
  -c '{"function":"CreateAsset","Args":["'${ASSET_ID}'", "'${PRODUCT_BUYER}'"]}'
```