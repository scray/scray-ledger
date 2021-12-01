
```bash
PEER_NAME=peer99
CHANNEL_NAME=c92
PEER_POD_NAME=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
SHARED_FS_HOST=10.11.8.156:30080
```

### Get channel informations

```bash
 kubectl exec --stdin --tty $PEER_POD_NAME -c scray-peer-cli -- /bin/sh \
  /mnt/conf/peer/block-operator.sh info --channel $CHANNEL_NAME
```

Output:  
``{"height":8,"currentBlockHash":"78Cik7HnAAldWl0Erq+oMobNFVx/rXFE5epZbzwipvU=","previousBlockHash":"whxdSGbjaWeRLHkDsoBnKxNTcRL2TURJnHPLzyZdClo="}``


### Get block as JSON

Fetch newest bock and publish it to http://$SHARED_FS_HOST/blocks/$CHANNEL_NAME/$CHANNEL_NAME-block-$BLOCK_NUMBER.block.json
```bash
BLOCK_NUMBER=newest
kubectl exec --stdin --tty $PEER_POD_NAME -c scray-peer-cli -- /bin/sh \
  /mnt/conf/peer/block-operator.sh fetch --channel $CHANNEL_NAME   --block $BLOCK_NUMBER  --publish $SHARED_FS_HOST
```

Output:  
``$CHANNEL_NAME-block-$BLOCK_NUMBER.block.json``

Fetch bock 5 and publish it to http://$SHARED_FS_HOST/blocks/$CHANNEL_NAME/$CHANNEL_NAME-block-$BLOCK_NUMBER.block.json
```bash
BLOCK_NUMBER=5
kubectl exec --stdin --tty $PEER_POD_NAME -c scray-peer-cli -- /bin/sh \
  /mnt/conf/peer/block-operator.sh fetch --channel $CHANNEL_NAME   --block $BLOCK_NUMBER  --publish $SHARED_FS_HOST
```

### Path to block content

* Key  
  ``x.data.data[0].payload.data.actions[0].payload.action.proposal_response_payload.extension.results.ns_rwset[1].rwset.writes[0].key``
* Value  
  ``x.data.data[0].payload.data.actions[0].payload.action.proposal_response_payload.extension.results.ns_rwset[1].rwset.writes[0].value``
