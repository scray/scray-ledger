CHANNEL_ID=$1
PKGID=$2


echo "Mint token and show balance"

export PKGID="$2"

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/

# Init chaincode
peer chaincode invoke -o  orderer.example.com:30081  --waitForEventTimeout 60s  --tls --cafile /tmp/tlsca.example.com-cert.pem  -C $CHANNEL_ID -n basic -c '{"function":"Initialize","Args":["name", "symbol"]}'

ACCOUNT_ID=$(peer chaincode query -C  $CHANNEL_ID -n basic  -c '{"function":"ClientAccountID","Args":[]}')

echo ACCOUNT_ID: $ACCOUNT_ID



echo "Mint token"
TOKEN_ID="$RANDOM"


peer chaincode invoke orderer.example.com:30081  --waitForEventTimeout 60s  --tls --cafile /tmp/tlsca.example.com-cert.pem  -C $CHANNEL_ID -n basic -c "{\"function\":\"MintBatch\",\"Args\":[\"$ACCOUNT_ID\",\"[1,2,3,4,5,6]\",\"[100,200,300,150,100,100]\"]}"

echo Mint  token with ID: $TOKEN_ID
mint_command="{\"function\":\"MintWithTokenURI\",\"Args\":[\"$TOKEN_ID\", \"https://example.com/nft$TOKEN_ID.json\"]}"



echo "Show balance of token 3"
# shellcheck disable=SC1073
peer chaincode query -C $CHANNEL_ID -n basic  -c "{\"function\":\"BalanceOf\",\"Args\":[\"$ACCOUNT_ID\",\"3\"]}"
