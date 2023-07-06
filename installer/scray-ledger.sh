#!/bin/bash
export WORKDIR=$(cd $(dirname $0) && pwd)

NAMESPACE=default

prepare() {
  createEnv
  createDatashare
}

createEnv() {
  echo "Create namespace  $NAMESPACE"
  # Kubernetes namespace
  kubectl create namespace $NAMESPACE
  kubectl config set-context --current --namespace=$NAMESPACE
}

function createDatashare() {
  # Data share
  BASE_PATH=..

  kubectl apply -f $BASE_PATH/containers/data-share/data-share-pvc.yaml
  kubectl apply -f $BASE_PATH/containers/data-share/k8s-hl-fabric-data-share-service.yaml
  kubectl apply -f $BASE_PATH/containers/data-share/k8s-hl-fabric-data-share.yaml
}


createOrderer() {
  ORDERER_NAME=orderer
  HOST_NAME=example.com
  BASE_PATH=..

  kubectl apply -f $BASE_PATH/containers/orderer/k8s-orderer-service.yaml

  kubectl delete configmap --ignore-not-found=true hl-fabric-orderer
  kubectl create configmap hl-fabric-orderer \
  	 --from-literal=hostname=$HOST_NAME \
	 --from-literal=org_name=$ORDERER_NAME \

	 --from-literal=ORDERER_GENERAL_LOCALMSPID=${ORDERER_NAME}MSP \
	 --from-literal=NODE_TYPE=orderer

  kubectl apply -f $BASE_PATH/containers/orderer/k8s-orderer.yaml
}


initAndRead() {
  PEER_NAME=peer-$RANDOM
  SHARED_FS=$1
  CC_HOSTNAME=asset-transfer-basic.org1.example.com
  CC_LABEL=basic_1.0
  SHARED_FS_USER=scray
  SHARED_FS_PW=scray
  CHANNEL_NAME=channel-$RANDOM

  echo "Create orderer"
  createOrderer

  echo "Create peer $PEER_NAME"
  ./scray-ledger.sh create-peer -n $PEER_NAME
  echo "Create channel $CHANNEL_NAME"
  ./scray-ledger.sh create-channel --name $CHANNEL_NAME

  sleep 10s
  echo "Add peer $PEER_NAME to channel $CHANNEL_NAME"
  ./scray-ledger.sh add-peer  --peer-name $PEER_NAME --channel-name $CHANNEL_NAME
 
  sleep 10s
  echo "Deploy chaincode "
  ./scray-ledger.sh deploy-chaincode --data-share $SHARED_FS
 
  sleep 15
  echo "Install chaincode on peer $PEER_NAME"
  ./scray-ledger.sh install-chaincode --peer-name $PEER_NAME --channel-name $CHANNEL_NAME --data-share $SHARED_FS

  sleep 5

  PKGID=$(curl -s  --user $SHARED_FS_USER:$SHARED_FS_PW http://$SHARED_FS/cc_descriptions/${CC_HOSTNAME}_$CC_LABEL/description-hash.json 2>&1 | jq -r '."description-hash"')
  $WORKDIR/commands/execute-example-interactions.sh --channel-name $CHANNEL_NAME --peer-name $PEER_NAME --package-id $PKGID
}

function startExternalChaincode() {

# Start external chaincode

kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/chaincode-external/k8s-external-chaincode.yaml


# Integrate chain code

#PEER_NAME=peer48
#CHANNEL_NAME=mychannel

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
PKGID=basic_1.0:54ef61f32a2f2a851865f62e93cb1c59eb7e8d7317bbac59b0a22234c28886a8

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
kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/cc-basic-interaction.sh  $CHANNEL_NAME
}


run() {
  $1
  if [ $? -ne 0 ]; then
    fatal "Error occured while executing command "
    exit 1
   fi
}

while [ "$1" != "" ]; do
    case $1 in
        prepare )shift
		  run prepare 
        ;;
        deploy )   	shift
		deployLedger	
        ;;
	create-orderer) shift
		createOrderer
	;;	
 	create-peer) shift
		PEER_NAME=$1
		"$WORKDIR/commands/create-peer.sh" "${@}" 
	;;
        create-channel) shift
	        "$WORKDIR/commands/create-channel.sh" "${@}"
	;;
	add-peer) shift
		"$WORKDIR/commands/add-peer.sh" "${@}"
	;;
	deploy-chaincode) shift
		"$WORKDIR/commands/deploy-chaincode.sh" "${@}"
	;;
	install-chaincode) shift
		"$WORKDIR/commands/install-chaincode.sh" "${@}"
	;;
	execute-example-interactions) shift
		"$WORKDIR/commands/execute-example-interactions.sh" "${@}"
	;;
	setup-example-ledger) shift
		initAndRead $1
	;;
	clean) shift
		"$WORKDIR/commands/clean.sh" "${@}"	
	;;
        * )                     # usage
                                exit 1
    esac
    shift
done
