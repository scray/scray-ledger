echo "${@}" 
PEER_NAME=

function createPeer() {
  PEER_NAME=$1

  #       createEnv()
  # Use docker desktop
  # kubectl config use-context docker-desktop

  #createDatashare

  SHARED_FS_HOST=hl-fabric-data-share-service:80
  SHARED_FS=hl-fabric-data-share-service:80

  # createOrderer

  PEER_HOST_NAME=$PEER_NAME.kubernetes.research.dev.seeburger.de
  EXT_PEER_IP=$(kubectl get nodes -o jsonpath="{.items[0].status.addresses[?(@.type=='InternalIP')].address}")

  cd ~/git/scray-ledger/containers
  ./configure-deployment.sh -n $PEER_NAME
  kubectl apply -f target/$PEER_NAME/k8s-peer-service.yaml
  GOSSIP_PORT=$(kubectl get service $PEER_NAME -o jsonpath="{.spec.ports[?(@.name=='peer-listen')].nodePort}")
  PEER_LISTEN_PORT=$(kubectl get service $PEER_NAME -o jsonpath="{.spec.ports[?(@.name=='peer-listen')].nodePort}")
  PEER_CHAINCODE_PORT=$(kubectl get service $PEER_NAME -o jsonpath="{.spec.ports[?(@.name=='peer-chaincode')].nodePort}")
  kubectl delete --ignore-not-found=true  configmap hl-fabric-peer-$PEER_NAME 
  kubectl create configmap hl-fabric-peer-$PEER_NAME \
   --from-literal=hostname=$PEER_HOST_NAME \
   --from-literal=org_name=$PEER_NAME \
   --from-literal=data_share=hl-fabric-data-share-service:80 \
   --from-literal=ca_country=DE \
   --from-literal=ca_province=Baden \
   --from-literal=ca_locality=Bretten \
   --from-literal=CORE_PEER_ADDRESS=peer0.$PEER_HOST_NAME:$PEER_LISTEN_PORT \
   --from-literal=CORE_PEER_GOSSIP_EXTERNALENDPOINT=peer0.$PEER_HOST_NAME:$GOSSIP_PORT \
   --from-literal=CORE_PEER_LOCALMSPID=${PEER_NAME}MSP
  kubectl apply -f target/$PEER_NAME/k8s-peer.yaml
}

while [ "$1" != "" ]; do
    case $1 in
        -n | --name )   shift
 	   PEER_NAME=$1
    esac
    shift
done

if [ -z "$PEER_NAME" ]
then
 echo "peer name is missing" ${usage}
 exit 1
else
 createPeer $PEER_NAME
fi
