

function publicChaincodeDefinition() {
   CC_HOSTNAME=hl-fabric-erc-721-example.example.com
   CC_SERVICE_NAME=erc-1155
   CC_PORT=$(kubectl get service $CC_SERVICE_NAME -o jsonpath="{.spec.ports[?(@.name=='chaincode')].nodePort}")
   CC_LABEL=basic_1.0

   CC_DEPLOYER_POD=$(kubectl get pod -l app=cc-deployer -o jsonpath="{.items[0].metadata.name}")
   kubectl exec --stdin --tty $CC_DEPLOYER_POD -c cc-deployer -- /bin/sh /opt/create-archive.sh $CC_HOSTNAME $CC_PORT $CC_LABEL $SHARED_FS
}

function createChannelAndJoinPeer() {
  cd ../../installer/

  ./scray-ledger.sh create-channel --name "$CHANNEL_NAME"
  ./scray-ledger.sh add-peer  --peer-name $PEER_NAME --channel-name $CHANNEL_NAME
}

function getPKGID() {
    SHARED_FS_USER=scray
    SHARED_FS_PW=scray
    PKGID=$(curl -s  --user $SHARED_FS_USER:$SHARED_FS_PW http://$SHARED_FS/cc_descriptions/${CC_HOSTNAME}_$CC_LABEL/description-hash.json 2>&1 | jq -r '."description-hash"')

    # Create configuration
    kubectl delete configmap erc-1155
    kubectl create configmap erc-1155 --from-literal=chaincode_id="$PKGID"

   kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/erc-1155/k8s-external-chaincode.yaml
}

function installOnPeer() {
  ORDERER_NAME=orderer.example.com
  IP_CC_SERVICE=$(kubectl get nodes -o jsonpath="{.items[0].status.addresses[?(@.type=='InternalIP')].address}")
  PEER_POD=$(kubectl get pod -l app="$PEER_NAME" -o jsonpath="{.items[0].metadata.name}")
  ORDERER_IP=$(kubectl get pods  -l app=orderer-org1-scray-org -o jsonpath='{.items[*].status.podIP}')
  ORDERER_LISTEN_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
  ORDERER_HOST=orderer.example.com
  EXT_PEER_IP=$(kubectl get nodes -o jsonpath="{.items[0].status.addresses[?(@.type=='InternalIP')].address}")

  ORDERER_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
  ORDERER_PORT=30081
  ORDERER_IP=$(kubectl get pods  -l app=orderer-org1-scray-org -o jsonpath='{.items[*].status.podIP}')

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
  kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/examples/cc-erc-1155-mint-token.sh $CHANNEL_NAME $PKGID
}

function usage() {
    echo "deploy-cc.sh --peer-name peer403 --channel-name channel-erc-1155 --share 10.15.130.111"
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
 echo "peer name is missing" "${usage}"
 exit 1
elif [ -z "$CHANNEL_NAME" ]
then
  echo "channel name is missing" "${usage}"
  exit 1
elif [ -z "$SHARED_FS_HOST" ]
then
  echo "SHARED_FS_HOST is missing" "${usage}"
  exit 1
else
 publicChaincodeDefinition
 createChannelAndJoinPeer
 getPKGID
 installOnPeer
fi



