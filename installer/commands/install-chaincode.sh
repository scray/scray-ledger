SHARED_FS=hl-fabric-data-share-service:80

function installChaincode() {
  PEER_NAME=$1
  CHANNEL_NAME=$2
  SHARED_FS=$3

  SHARED_FS_USER=scray
  SHARED_FS_PW=scray

CC_HOSTNAME=asset-transfer-basic.org1.example.com

ORDERER_NAME=orderer.example.com
IP_CC_SERVICE=$(kubectl get nodes -o jsonpath="{.items[0].status.addresses[?(@.type=='InternalIP')].address}")         # Host where the chaincode is running
PEER_POD=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
ORDERER_IP=$(kubectl get pods  -l app=orderer-org1-scray-org -o jsonpath='{.items[*].status.podIP}')
ORDERER_LISTEN_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
ORDERER_HOST=orderer.example.com
EXT_PEER_IP=$(kubectl get nodes -o jsonpath="{.items[0].status.addresses[?(@.type=='InternalIP')].address}") 

ORDERER_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
ORDERER_PORT=30081
ORDERER_IP=$(kubectl get pods  -l app=orderer-org1-scray-org -o jsonpath='{.items[*].status.podIP}')

CC_LABEL=basic_1.0
PKGID=$(curl -s  --user $SHARED_FS_USER:$SHARED_FS_PW http://"$SHARED_FS"/cc_descriptions/${CC_HOSTNAME}_$CC_LABEL/description-hash.json 2>&1 | jq -r '."description-hash"')

if [ -z "$PKGID" ]
then
 echo "PKGID is empty. Expected location is: http://$SHARED_FS/cc_descriptions/${CC_HOSTNAME}_$CC_LABEL/description-hash.json"
 exit 1
fi

kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh \
    /mnt/conf/install_and_approve_cc.sh \
        $IP_CC_SERVICE \
        $ORDERER_IP \
        $ORDERER_HOST \
        $ORDERER_PORT \
        $CHANNEL_NAME \
        $PKGID \
        $CC_HOSTNAME \
        $CC_LABEL \
        $SHARED_FS

kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/cc_commit.sh  $CHANNEL_NAME $PKGID
}

while [ "$1" != "" ]; do
    case $1 in
        --peer-name )   shift
 	   PEER_NAME=$1
	;;
        --channel-name )   shift
           CHANNEL_NAME=$1
	;;
        --data-share )   shift
	   SHARED_FS=$1
        ;;

    esac
    shift
done

if [ -z "$PEER_NAME" ]
then
 echo "peer name is missing" ${usage}
 exit 1
elif [ -z "$CHANNEL_NAME" ]
then
  echo "channel name is missing" ${usage}
  exit 1
else
 installChaincode $PEER_NAME $CHANNEL_NAME $SHARED_FS 
fi
