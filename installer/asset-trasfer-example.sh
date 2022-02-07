#!/bin/bash
export WORKDIR=$(cd $(dirname $0) && pwd)

NAMESPACE=scray-ledger-asset-transfer

# Delete all deplyments
function clean() {
  kubectl delete --all deployments --namespace=$NAMESPACE
}

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
  cd ~/git/scray-ledger/containers/data-share
  kubectl apply -f k8s-hl-fabric-data-share-service.yaml
  kubectl apply -f k8s-hl-fabric-data-share.yaml
}


createOrderer() {
  ORDERER_NAME=orderer
  HOST_NAME=example.com
  cd  ~/git/scray-ledger/containers/orderer/

  kubectl apply -f k8s-orderer-service.yaml

  kubectl delete configmap hl-fabric-orderer
  kubectl create configmap hl-fabric-orderer \
  	 --from-literal=hostname=$HOST_NAME \
	 --from-literal=org_name=$ORDERER_NAME \
	 --from-literal=ORDERER_GENERAL_LOCALMSPID=${ORDERER_NAME}MSP \
	 --from-literal=NODE_TYPE=orderer

  kubectl apply -f k8s-orderer.yaml

}

function createChannel() {
# Create channel

CHANNEL_NAME=$1

ORDERER_POD=$(kubectl get pod -l app=orderer-org1-scray-org -o jsonpath="{.items[0].metadata.name}")
ORDERER_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
ORDERER_PORT=30081
kubectl exec --stdin --tty $ORDERER_POD -c scray-orderer-cli  -- /bin/sh /mnt/conf/orderer/scripts/create_channel.sh $CHANNEL_NAME orderer.example.com $ORDERER_PORT 
# kubectl exec -i $ORDERER_POD -c scray-orderer-cli  -- //bin/bash //mnt/conf/orderer/scripts/create_channel.sh $CHANNEL_NAME orderer.example.com $ORDERER_PORT $EXT_PEER_IP $PEER_HOST_NAME

}

function addPeer() {

	PEER_NAME=$1
	CHANNEL_NAME=$2

# Add peer to channel

PEER_HOST_NAME=$PEER_NAME.kubernetes.research.dev.seeburger.de 
EXT_PEER_IP=$(kubectl get nodes -o jsonpath="{.items[0].status.addresses[?(@.type=='InternalIP')].address}")
ORDERER_IP=$(kubectl get pods  -l app=orderer-org1-scray-org -o jsonpath='{.items[*].status.podIP}')
ORDERER_HOSTNAME=orderer.example.com 
ORDERER_PORT=30081
ORDERER_POD=$(kubectl get pod -l app=orderer-org1-scray-org -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $ORDERER_POD -c scray-orderer-cli  -- /bin/sh /mnt/conf/orderer/scripts/inform_existing_nodes.sh $ORDERER_IP $CHANNEL_NAME $PEER_NAME $SHARED_FS_HOST $EXT_PEER_IP $PEER_HOST_NAME 

PEER_POD_NAME=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
ORDERER_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
ORDERER_PORT=30081
PEER_PORT=$(kubectl get service $PEER_NAME -o jsonpath="{.spec.ports[?(@.name=='peer-listen')].nodePort}")
kubectl exec --stdin --tty $PEER_POD_NAME  -c scray-peer-cli -- /bin/sh /mnt/conf/peer_join.sh $ORDERER_IP  $ORDERER_HOSTNAME $ORDERER_PORT $CHANNEL_NAME $SHARED_FS_HOST $EXT_PEER_IP 



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
  echo FFF
  if [ $? -ne 0 ]; then
    fatal "Error occured while executing command "
    exit 1
   fi
}

while [ "$1" != "" ]; do
    case $1 in
         clean )   shift
		clean                                
	  ;;
          prepare )shift
		  run prepare 
          ;;
         deploy )   	shift
		deployLedger	
         ;;
 	create-peer) shift
		PEER_NAME=$1
		"$WORKDIR/commands/create-peer.sh" "${@}" 
	;;
        create-channel) shift
		CHANNEL_NAME=$1
		shift
	       	echo CHANNEL $CHANNEL_NAME	
		createChannel $CHANNEL_NAME
	;;
	add-peer) shift
		PEER_NAME=$1
		CHANNEL_NAME=$2
		shift 2
		addPeer $PEER_NAME $CHANNEL_NAME
	;;
        * )                     # usage
                                exit 1
    esac
    shift
done
