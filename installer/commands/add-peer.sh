SHARED_FS_HOST=hl-fabric-data-share-service:80

function addPeer() {

  PEER_NAME=$1
  CHANNEL_NAME=$2
  SHARED_FS_HOST=$3

  # Add peer to channel
  PEER_HOST_NAME=$PEER_NAME.kubernetes.research.dev.seeburger.de
  EXT_PEER_IP=$(kubectl get nodes -o jsonpath="{.items[0].status.addresses[?(@.type=='InternalIP')].address}")
  ORDERER_IP=$(kubectl get pods  -l app=orderer-org1-scray-org -o jsonpath='{.items[*].status.podIP}')
  ORDERER_HOSTNAME=orderer.example.com
  ORDERER_PORT=30081
  ORDERER_POD=$(kubectl get pod -l app=orderer-org1-scray-org -o jsonpath="{.items[0].metadata.name}")
  echo FFF
  kubectl exec --stdin --tty $ORDERER_POD -c scray-orderer-cli  -- /bin/sh /mnt/conf/orderer/scripts/inform_existing_nodes.sh $ORDERER_IP $CHANNEL_NAME $PEER_NAME $SHARED_FS_HOST $EXT_PEER_IP $PEER_HOST_NAME

  PEER_POD_NAME=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")
  ORDERER_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
  ORDERER_PORT=30081
  PEER_PORT=$(kubectl get service $PEER_NAME -o jsonpath="{.spec.ports[?(@.name=='peer-listen')].nodePort}")
  kubectl exec --stdin --tty $PEER_POD_NAME  -c scray-peer-cli -- /bin/sh /mnt/conf/peer_join.sh $ORDERER_IP  $ORDERER_HOSTNAME $ORDERER_PORT $CHANNEL_NAME $SHARED_FS_HOST $EXT_PEER_IP
}

while [ "$1" != "" ]; do
    case $1 in
        --peer-name )   shift
 	   PEER_NAME=$1
	;;
        --channel-name )   shift
           CHANNEL_NAME=$1
	;;
        --share )   shift
	   SHARED_FS_HOST=$1
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
 addPeer $PEER_NAME $CHANNEL_NAME $SHARED_FS_HOST 
fi
