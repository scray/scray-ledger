

# Two nodes
	peer1  -> C1_0
	peer2  -> C1_1
    
    Channel= channel1

```bash
PEER_1=p1
PEER_2=p2

CHANNEL_NAME=c1
DATA_SHARE=10.15.130.111

```

### Create peer
```bash

./scray-ledger.sh create-peer -n $PEER_2
./scray-ledger.sh create-peer -n $PEER_1
```

### Create channel
```bash
./scray-ledger.sh create-channel --name $CHANNEL_NAME
```
# Add peers to channel
```bash
./scray-ledger.sh add-peer  --peer-name $PEER_1 --channel-name $CHANNEL_NAME
./scray-ledger.sh add-peer  --peer-name $PEER_2 --channel-name $CHANNEL_NAME
```

# Install shared chaincode
```bash
./scray-ledger.sh install-chaincode --peer-name $PEER_1 --channel-name $CHANNEL_NAME --data-share $DATA_SHARE
./scray-ledger.sh install-chaincode --peer-name $PEER_2 --channel-name $CHANNEL_NAME --data-share $DATA_SHARE
```


#### Peer 1 write
```bash
PEER_POD=$(kubectl get pod -l app=$PEER_1 -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/add-invoice.sh  $CHANNEL_NAME 14 otto
```

#### Peer 2 write
```bash
PEER_POD=$(kubectl get pod -l app=$PEER_2 -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/add-invoice.sh  $CHANNEL_NAME 14 otto
```

```Error: endorsement failure during invoke. response: status:500 message:"the asset 14 already exists"```

