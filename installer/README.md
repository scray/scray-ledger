### Setup example ledger
```bash
DATA_SHARE=10.15.130.111
```

```
./asset-trasfer-example.sh setup-example-ledger $DATA_SHARE
```


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
PEER_NAME=peer401
CHANNEL_NAME=channel-1
```

```bash
./asset-trasfer-example.sh add-peer  --peer-name $PEER_NAME --channel-name $CHANNEL_NAME
```

### Deploy chaincode
```bash
DATA_SHARE=10.15.130.111
```
```bash
./asset-trasfer-example.sh deploy-chaincode --data-share $DATA_SHARE
```

### Install example-net chaincode on channel
```bash
PEER_NAME=peer401
CHANNEL_NAME=channel-1
```

```bash
./asset-trasfer-example.sh install-chaincode --peer-name $PEER_NAME --channel-name $CHANNEL_NAME --data-share $DATA_SHARE
```
### Execute example code
```bash
PKGID=basic_1.0:321a837a7587dc3ffa6ee5a16c2af6d0163b4e2a60cd5dbeba6cabde797c5467

```

```bash
./asset-trasfer-example.sh  --channel-name $CHANNEL_NAME --peer-name $PEER_NAME --package-id $PKGID
```
