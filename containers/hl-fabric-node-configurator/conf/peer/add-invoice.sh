CHANNEL_ID=$1
ASSET_ID=$3
PRODUCT_BUYER=$4


export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/

peer chaincode invoke -o orderer.example.com:30081 --tls --cafile /tmp/tlsca.example.com-cert.pem --waitForEvent -C "$CHANNEL_ID" -n basic -c '{"function":"CreateAsset","Args":["'${ASSET_ID}'", "'${PRODUCT_BUYER}'"]}'
