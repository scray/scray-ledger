
### Create peer

```bash
PEER_NAME=peer403
```

```bash
./asset-trasfer-example.sh create-peer -n $PEER_NAME
```

### Create channel
```bash
CHANNEL_NAME=channel-1
```

```bash
./asset-trasfer-example.sh create-channel --name $CHANNEL_NAME 
```

### Add peer to channel
```bash
PEER_NAME=peer403
CHANNEL_NAME=channel-1
```

```bash
./asset-trasfer-example.sh add-peer  --peer-name $PEER_NAME --channel-name $CHANNEL_NAME
```

### Install example-net chaincode on channel
```bash
PEER_NAME=peer403
CHANNEL_NAME=channel-1
```

```bash
./asset-trasfer-example.sh install-chaincode --peer-name $PEER_NAME --channel-name $CHANNEL_NAME
```
