
executeExampleOperations() {
  PEER_NAME=$1
  CHANNEL_NAME=$2
  PKGID=$3

  SHARED_FS_USER=scray
  SHARED_FS_PW=scray

  PEER_POD=$(kubectl get pod -l app=$PEER_NAME -o jsonpath="{.items[0].metadata.name}")

  kubectl exec --stdin --tty $PEER_POD -c scray-peer-cli -- /bin/sh /mnt/conf/peer/cc-basic-interaction.sh  $CHANNEL_NAME $PKGID 
}

echo  $@

while [ "$1" != "" ]; do
    case $1 in
        --peer-name )   shift
 	   PEER_NAME=$1
	;;
        --channel-name )   shift
           CHANNEL_NAME=$1
	;;
	--package-id )  shift
	  PKGID=$1
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
elif [ -z "$PKGID" ]
then
  echo "Package id is missing" ${usage}
  exit 1
else
 executeExampleOperations  $PEER_NAME $CHANNEL_NAME $PKGID
fi
