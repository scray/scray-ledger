EXT_CC_IP=$1
ORDERER_IP=$2
ORDERER_HOSTNAME=$3
ORDERER_PORT=$4
CHANNEL_ID=$5
PKGID=$6
CC_HOSTNAME=$7
CC_LABEL=$8
SHARED_FS_HOST=$9

apk add curl
export PKGID=$PKGID

SHARED_FS_USER=scray
SHARED_FS_PW=scray

echo $ORDERER_IP $ORDERER_HOSTNAME >> /etc/hosts
# Set hostname of external chaincode node
echo $EXT_CC_IP $CC_HOSTNAME >> /etc/hosts

export CHANNEL_NAME=$CHANNEL_NAME

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/Admin@$HOSTNAME/msp/
export CORE_PEER_ADDRESS=$CORE_PEER_ADDRESS

# Get chaincode description
curl --user $SHARED_FS_USER:$SHARED_FS_PW http://$SHARED_FS_HOST/cc_descriptions/${CC_HOSTNAME}_$CC_LABEL/chainecode_description.tgz > chaincode_description.tgz
# TODO compare hash of chaincode_description.tgz with PKGID ...

peer lifecycle chaincode install chaincode_description.tgz

# Find next sequence number
NEXT_SEQUENCE=$(peer lifecycle chaincode queryapproved -C $CHANNEL_ID  -n basic --output json | jq -r '.sequence')


peer lifecycle chaincode queryinstalled
peer lifecycle chaincode approveformyorg      -o $ORDERER_HOSTNAME:$ORDERER_PORT --tls  --cafile /tmp/tlsca.example.com-cert.pem --channelID $CHANNEL_ID --name basic --version 1.0 --package-id $PKGID --sequence  $NEXT_SEQUENCE
peer lifecycle chaincode checkcommitreadiness -o $ORDERER_HOSTNAME:$ORDERER_PORT --ordererTLSHostnameOverride orderer.example.com --tls  --cafile /tmp/tlsca.example.com-cert.pem --channelID $CHANNEL_ID --name basic --version 1.0  --sequence  $NEXT_SEQUENCE
