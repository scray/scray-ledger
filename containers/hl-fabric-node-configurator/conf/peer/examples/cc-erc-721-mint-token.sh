CHANNEL_ID=$1
PKGID=$2

export PKGID="$2"

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/

TOKEN_ID="$RANDOM"
echo Mint  token with ID: $TOKEN_ID
mint_command="{\"function\":\"MintWithTokenURI\",\"Args\":[\"$TOKEN_ID\", \"https://example.com/nft$TOKEN_ID.json\"]}"

peer chaincode invoke -o orderer.example.com:30081 --waitForEventTimeout 60s --tls --cafile /tmp/tlsca.example.com-cert.pem -C $CHANNEL_ID -n basic -c "$mint_command"

sleep 5s
echo "Show balance"
peer chaincode query -C $CHANNEL_ID -n basic -c '{"function":"ClientAccountBalance","Args":[]}'

