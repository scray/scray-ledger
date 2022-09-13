## Connection Profile creator
Tool to create a Hyperledger Fabric connection profile for clients which uses the Hyperledger Fabric gateway

### Add peer
#### Prerequisites

```
DATA_SHARE=10.15.130.111
SHARED_FS_USER=scray
SHARED_FS_PW=scray

PEER_NAME=peer200
PEER_HOSTNAME=$PEER_NAME.kubernetes.research.dev.seeburger.de
```

#### Publish Peer TLS CA cert

```
PEER_POD_NAME=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $PEER_POD_NAME  -c scray-peer-cli -- /bin/sh /mnt/conf/peer/publish-tlsca.sh --shared-fs-host $DATA_SHARE
```

#### Download Peer TLS CA cert
```
curl --user $SHARED_FS_USER:$SHARED_FS_PW -T "$CA_CERT_PATH" http://$DATA_SHARE/peer-tlsca-certs/"$PEER_HOSTNAME"/tlsca.pem -o target/$PEER_HOSTNAME-tlsca.pem
```
#### Create connection profile

```
 mvn install
 PEER_CHAINCODE_PORT=$(kubectl get service $PEER_NAME -o jsonpath="{.spec.ports[?(@.name=='peer-listen')].nodePort}")
 echo $PEER_CHAINCODE_PORT
 java -jar target/hlf-connection-profile-creator-1.0-jar-with-dependencies.jar --peer-name $PEER_NAME  --peer-hostname $PEER_HOSTNAME --peer-chaincode-port $PEER_CHAINCODE_PORT
```

Show connection profile

```
cat target/connection.yaml
```




