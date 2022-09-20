

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

```bash

```
