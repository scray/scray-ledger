#!/bin/bash

export CORE_PEER_MSPCONFIGPATH=/mnt/conf/organizations/peerOrganizations/$HOSTNAME/users/User1@$HOSTNAME/msp/

fetchBlock() {
  CHANNEL_ID=$1
  BLOCK_NUMBER=$2

  echo "Execute: " fetchBlock $CHANNEL_ID $BLOCK_NUMBER

  peer channel fetch $BLOCK_NUMBER $CHANNEL_ID-block-$BLOCK_NUMBER.block  -c $CHANNEL_ID --orderer  orderer.example.com:30081 --tls --cafile /tmp/tlsca.example.com-cert.pem
  configtxgen -inspectBlock $CHANNEL_ID-block-$BLOCK_NUMBER.block > $CHANNEL_ID-block-$BLOCK_NUMBER.block.json
  cat $CHANNEL_ID-block-$BLOCK_NUMBER.block.json
}

publish() {
    SHARED_FS_USER=scray
    SHARED_FS_PW=scray
    curl --user $SHARED_FS_USER:$SHARED_FS_PW -X MKCOL http://$SHARED_FS_HOST/blocks/
    curl --user $SHARED_FS_USER:$SHARED_FS_PW -X MKCOL http://$SHARED_FS_HOST/blocks/$CHANNEL_ID/
    curl --user $SHARED_FS_USER:$SHARED_FS_PW  -X DELETE http://$SHARED_FS_HOST/blocks/$CHANNEL_ID/$CHANNEL_ID-block-$BLOCK_NUMBER.block.json
    curl --user $SHARED_FS_USER:$SHARED_FS_PW -T $CHANNEL_ID-block-$BLOCK_NUMBER.block.json http://$SHARED_FS_HOST/blocks/$CHANNEL_ID/$CHANNEL_ID-block-$BLOCK_NUMBER.block.json
}

getLatestBlockInfos() {
  peer channel getinfo -c $CHANNEL_ID | cut -c18-
}


readParameters()
{
  BASE_PATH=$PWD

  COMMAND=$1
  shift 1

  while [ "$1" != "" ]; do
      case $1 in
           --channel)	shift
                      CHANNEL_ID=$1
                        ;;
          --block)  shift
                    BLOCK_NUMBER=$1
                      ;;
          --publish) shift
              SHARED_FS_HOST=$1
              ;;
      esac
      shift
  done

  case "$COMMAND" in
      info)
        getLatestBlockInfos
        ;;
      fetch)
        fetchBlock $CHANNEL_ID $BLOCK_NUMBER
        ;;
  esac
}

readParameters "$@"

if [ -z "$SHARED_FS_HOST" ]
then
  ""
else
  publish
fi