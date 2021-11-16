CHANNEL_ID=$1
PKGID=$3

export PKGID=$PKGID

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/Admin@$HOSTNAME/msp/

echo Installed chaincode
peer lifecycle chaincode queryinstalled

NEXT_SEQUENCE=$(peer lifecycle chaincode queryapproved -C $CHANNEL_ID  -n basic --output json | jq -r '.sequence')

peer lifecycle chaincode checkcommitreadiness -o orderer.example.com:30081 --ordererTLSHostnameOverride orderer.example.com --tls  --cafile /tmp/tlsca.example.com-cert.pem --channelID $CHANNEL_ID --name basic --version 1.0 --sequence $NE

peer lifecycle chaincode commit -o orderer.example.com:30081 --tls  --cafile /tmp/tlsca.example.com-cert.pem --channelID $CHANNEL_ID --name basic --version 1.0 --sequence $NEXT_SEQUENCE