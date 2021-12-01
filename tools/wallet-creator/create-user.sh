#wget https://raw.githubusercontent.com/scray/scray-ledger/develop/tools/wallet-creator/cert-creator.sh
#chmod 755 ./cert-creator.sh

#USER="ben" 
#HOSTNAME="babby-1.kubernetes.research.dev.seeburger.de"
USER=$1
HOSTNAME=$2
HOSTNAME2=$2
CN=$USER@$HOSTNAME

/tmp/cert-creator.sh create_csr --common-name $CN
/tmp/cert-creator.sh push_csr --common-name $CN --shared-fs-host kubernetes.research.dev.seeburger.de:30080

/tmp/cert-creator.sh pull_csr --common-name $CN --shared-fs-host kubernetes.research.dev.seeburger.de:30080
CA_CERT=/mnt/conf/organizations/peerOrganizations/$HOSTNAME2/ca/ca.*.pem
CA_KEY=/mnt/conf/organizations/peerOrganizations/$HOSTNAME2/ca/priv_sk
/tmp/cert-creator.sh sign_csr --common-name $CN --cacert $CA_CERT --cakey $CA_KEY
/tmp/cert-creator.sh push_crt --common-name $CN --shared-fs-host kubernetes.research.dev.seeburger.de:30080

/tmp/cert-creator.sh pull_signed_crt --common-name $CN --shared-fs-host kubernetes.research.dev.seeburger.de:30080
#./cert-creator.sh create_wallet --common-name $CN --mspId peer2MSP -j /tmp
 
openssl x509 -in /tmp/crt_target/$CN/user.crt -out /tmp/crt_target/$CN/user.pem -outform PEM


mkdir -p /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/
mkdir -p /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/tls
cp  /tmp/crt_target/$CN/key.pem /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/tls/client.key
cp  /tmp/crt_target/$CN/user.crt  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/tls/client.crt
cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME2/users/User1@$HOSTNAME2/tls/ca.crt /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/tls/

mkdir  -p /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp
mkdir  -p /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/admincerts
mkdir  -p /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/cacerts
mkdir  -p /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/keystore
mkdir  -p /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/signcerts
mkdir  -p /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/tlscacerts

cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME2/users/User1@$HOSTNAME/msp/cacerts/* /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/cacerts/
cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$USER@$HOSTNAME/tls/client.key  /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/keystore/

cp /tmp/crt_target/$CN/user.pem   /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/signcerts/$CN-cert.pem

cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME2/users/User1@$HOSTNAME/msp/tlscacerts/tlsca.$HOSTNAME-cert.pem /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/tlscacerts
cp /mnt/conf/organizations/peerOrganizations/$HOSTNAME2/users/User1@$HOSTNAME/msp/config.yaml /mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/$CN/msp/
