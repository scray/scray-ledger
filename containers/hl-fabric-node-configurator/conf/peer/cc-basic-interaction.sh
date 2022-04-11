CHANNEL_ID=$1
PKGID=$2

export PKGID="$2"

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/

echo "Call init method of chain code basic"
peer chaincode invoke -o orderer.example.com:30081 --waitForEventTimeout 60s --tls --cafile /tmp/tlsca.example.com-cert.pem -C $CHANNEL_ID -n basic -c '{"function":"InitLedger","Args":[]}'

sleep 5s
echo "Read all assets"
peer chaincode query -C $CHANNEL_ID -n basic -c '{"Args":["GetAllAssets"]}'

