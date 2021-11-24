#wget https://raw.githubusercontent.com/scray/scray-ledger/develop/tools/wallet-creator/cert-creator.sh
#chmod 755 ./cert-creator.sh

#USER="ben" 
#HOSTNAME="babby-1.kubernetes.research.dev.seeburger.de"
USER=$1
HOSTNAME=$2
CN=$USER@$HOSTNAME

/tmp/cert-creator.sh create_csr --common-name $CN
/tmp/cert-creator.sh push_csr --common-name $CN --shared-fs-host kubernetes.research.dev.seeburger.de:30080

/tmp/cert-creator.sh pull_csr --common-name $CN --shared-fs-host kubernetes.research.dev.seeburger.de:30080
CA_CERT=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/ca/ca.*.pem
CA_KEY=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/ca/priv_sk
/tmp/cert-creator.sh sign_csr --common-name $CN --cacert $CA_CERT --cakey $CA_KEY
/tmp/cert-creator.sh push_crt --common-name $CN --shared-fs-host kubernetes.research.dev.seeburger.de:30080

/tmp/cert-creator.sh pull_signed_crt --common-name $CN --shared-fs-host kubernetes.research.dev.seeburger.de:30080
#./cert-creator.sh create_wallet --common-name $CN --mspId peer2MSP -j /tmp
 
openssl x509 -in /tmp/crt_target/$CN/user.crt -out /tmp/crt_target/$CN/user.pem -outform PEM


mkdir /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/
mkdir /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/tls
cp  /tmp/crt_target/$CN/key.pem /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/tls/client.key
cp  /tmp/crt_target/$CN/user.crt  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/tls/client.crt
cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/tls/ca.crt /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/tls/

mkdir  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp
mkdir  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/admincerts
mkdir  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/cacerts
mkdir  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/keystore
mkdir  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/signcerts
mkdir  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/tlscacerts

cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/cacerts/* /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/cacerts/
cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/tls/client.key  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/keystore/

cp /tmp/crt_target/$CN/user.pem   /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/signcerts/$USER@$HOSTNAME-cert.pem

cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/tlscacerts/tlsca.$HOSTNAME-cert.pem /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/tlscacerts
cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/config.yaml /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/msp/
