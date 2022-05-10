CHANNEL_NAME=

function createChannel() {
# Create channel

CHANNEL_NAME=$1

echo "Create channel $1"

ORDERER_POD=$(kubectl get pod -l app=orderer-org1-scray-org -o jsonpath="{.items[0].metadata.name}")
ORDERER_PORT=$(kubectl get service orderer-org1-scray-org -o jsonpath="{.spec.ports[?(@.name=='orderer-listen')].nodePort}")
ORDERER_PORT=30081
kubectl exec --stdin --tty $ORDERER_POD -c scray-orderer-cli  -- /bin/sh /mnt/conf/orderer/scripts/create_channel.sh $CHANNEL_NAME orderer.example.com $ORDERER_PORT
}


while [ "$1" != "" ]; do
    case $1 in
        -n | --name )   shift
           CHANNEL_NAME=$1
    esac
    shift
done

if [ -z "$CHANNEL_NAME" ]
then
  echo "channel name is missing" ${usage}
  exit 1
else
  createChannel $CHANNEL_NAME
fi

