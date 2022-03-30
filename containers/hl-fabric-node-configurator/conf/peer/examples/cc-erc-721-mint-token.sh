CHANNEL_ID=$1
PKGID=$2

export PKGID="$2"

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/

echo "Mint token"
peer chaincode invoke -o orderer.example.com:30081 --waitForEventTimeout 60s --tls --cafile /tmp/tlsca.example.com-cert.pem -C $CHANNEL_ID -n basic -c '{"function":"MintWithTokenURI","Args":["101", "https://example.com/nft101.json"]}'

sleep 5s
echo "Show balance"
peer chaincode query -C $CHANNEL_ID -n basic -c '{"function":"ClientAccountBalance","Args":[]}'

