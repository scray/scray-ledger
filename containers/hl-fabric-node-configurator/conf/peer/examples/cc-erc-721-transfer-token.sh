CHANNEL_ID=$1
PKGID=$2

export PKGID="$2"

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/

echo "Transfer token"
TOKEN_ID="$RANDOM"
echo Generate token with ID: $TOKEN_ID
mint_command="{\"function\":\"TransferFrom\",\"Args\":[\"x509::CN=User1@peer403.kubernetes.research.dev.seeburger.de,OU=client,L=Bretten,ST=Baden,C=DE::CN=ca.peer403.kubernetes.research.dev.seeburger.de,O=peer40

peer chaincode invoke -o orderer.example.com:30081 --waitForEventTimeout 60s --tls --cafile /tmp/tlsca.example.com-cert.pem -C $CHANNEL_ID -n basic -c "$mint_command"

sleep 5s
echo "Show balance"
peer chaincode query -C $CHANNEL_ID -n basic -c '{"function":"ClientAccountBalance","Args":[]}'