CHANNEL_ID=$1
PKGID=$2
NEW_POLICY=$3

export PKGID=$PKGID

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/Admin@$HOSTNAME/msp/

echo Installed chaincode
peer lifecycle chaincode queryinstalled

NEXT_SEQUENCE=$(peer lifecycle chaincode queryapproved -C $CHANNEL_ID  -n basic --output json | jq -r '.sequence')

peer lifecycle chaincode commit -o orderer1.dlt.see-hsa.s-node.de:30081 --tls  --cafile /tmp/tlsca.example.com-cert.pem --channelID $CHANNEL_ID --name basic --version 1.0 --signature-policy $NEW_POLICY --sequence $NEXT_SEQUENCE