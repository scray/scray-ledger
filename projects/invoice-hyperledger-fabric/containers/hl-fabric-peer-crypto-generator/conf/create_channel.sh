export FABRIC_CFG_PATH=/mnt/conf/orderer/

cd /mnt/conf/orderer/
configtxgen -profile TwoOrgsChannel -outputCreateChannelTx ./channel-artifacts/channel1.tx -channelID channel1
export FABRIC_CFG_PATH=/mnt/conf/


export CORE_PEER_TLS_ENABLED=true
export CORE_PEER_LOCALMSPID="AdminOrgMSP"
export CORE_PEER_TLS_ROOTCERT_FILE=/mnt/conf/admin/organizations/peerOrganizations/kubernetes.research.dev.seeburger.de/peers/peer0.kubernetes.research.dev.seeburger.de/msp/cacerts/ca.kubernetes.research.dev.seeburger.de-cert.pem 
export CORE_PEER_MSPCONFIGPATH=/mnt/conf/admin/organizations/peerOrganizations/kubernetes.research.dev.seeburger.de/users/Admin\@kubernetes.research.dev.seeburger.de/msp/



export CORE_PEER_ADDRESS=kubernetes.research.dev.seeburger.de:32052


peer channel create -o kubernetes.research.dev.seeburger.de:32052  --ordererTLSHostnameOverride orderer.example.com -c channel1 -f ./channel-artifacts/channel1.tx --outputBlock ./channel-artifacts/channel1.block --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem

configtxlator proto_decode --input genesis.block --type common.Block --output config_block.json
