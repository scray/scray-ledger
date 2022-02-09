```bash
./create-channel.sh --name channel-102
```
```bash
./add-peer.sh  --peer-name peer401 --channel-name channel-1
```

```bash
./install-chaincode.sh --peer-name $PEER_NAME --channel-name $CHANNEL_NAME
```