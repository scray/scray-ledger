# Local ledger persistence
Script to create local volume definition which can be used to store ledger data

```bash
PEER_NAME=peer0
```
  
```bash
./configure-vol.sh -p /tmp/peer-data -n $PEER_NAME 
```

### Capacity for datashare
```bash
./configure-vol.sh -p /tmp/peer-data -n datashare-vol --storrage-capacity 1Gi
```

