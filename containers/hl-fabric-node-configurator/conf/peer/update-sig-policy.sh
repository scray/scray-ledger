ORDERER_HOSTNAME=$1
ORDERER_PORT=$2
CHANNEL_ID=$3
PKGID=$4
NEW_POLICY=$5

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/Admin@$HOSTNAME/msp/
export CORE_PEER_ADDRESS=$CORE_PEER_ADDRESS

# Find next sequence number
SEQUENCE_NUMBER=$(peer lifecycle chaincode querycommitted  -C $CHANNEL_ID  -n basic --output json | jq -r '.sequence')
NEXT_SEQUENCE=$(($SEQUENCE_NUMBER+1))

peer lifecycle chaincode queryinstalled
peer lifecycle chaincode approveformyorg      -o $ORDERER_HOSTNAME:$ORDERER_PORT --tls  --cafile /tmp/tlsca.example.com-cert.pem --channelID $CHANNEL_ID --name basic --version 1.0 --signature-policy $NEW_POLICY --package-id $PKGID --sequence  $NEXT_SEQUENCE
peer lifecycle chaincode checkcommitreadiness -o $ORDERER_HOSTNAME:$ORDERER_PORT --ordererTLSHostnameOverride orderer.example.com --tls  --cafile /tmp/tlsca.example.com-cert.pem --channelID $CHANNEL_ID --name basic --version 1.0 --signature-policy $NEW_POLICY  --sequence  $NEXT_SEQUENCE
