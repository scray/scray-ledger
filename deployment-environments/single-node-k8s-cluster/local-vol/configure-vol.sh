#!/bin/bash

PEER_NAME=
LOCAL_FILE_PATH=/tmp/
BASE_PATH=$PWD

createEnvForNewConf() {
  mkdir -p target/$PEER_NAME-vol
  cp host-vol.yaml ./target/$PEER_NAME-vol
  cp configure-vol.sh ./target/$PEER_NAME-vol/
  cd ./target/$PEER_NAME-vol/
}

yq() {
  $BASE_PATH/bin/yq $1 $2 $3 $4 $5
}

# Check if yq exists
checkYqVersion() {
  dowloadYqBin
}

dowloadYqBin() {
  if [[ ! -f "./bin/yq" ]]
  then
    echo "yq does not exists"
    if [ "$OSTYPE" == "linux-gnu" ]
    then
      echo "download linux_amd64 yq binary"
      mkdir bin
      curl -L https://github.com/mikefarah/yq/releases/download/3.4.1/yq_linux_amd64 -o ./bin/yq
      chmod u+x ./bin/yq
    elif [ "$OSTYPE" == "msys" ]
    then
      echo "download yq_windows_amd64  yq binary"
      mkdir bin
      curl -L https://github.com/mikefarah/yq/releases/download/3.4.1/yq_windows_amd64.exe -o ./bin/yq
      chmod u+x ./bin/yq
    fi
  fi
}

setValuesInLocalFile() {
  yq w -i host-vol.yaml "metadata.name" $PEER_NAME-vol
  yq w -i host-vol.yaml "spec.hostPath.path" $LOCAL_FILE_PATH/$PEER_NAME
}


usage()
{
    echo "usage: Create local volum with a defined name and location "
}


while [ "$1" != "" ]; do
    case $1 in
        -n | --name )   shift
				PEER_NAME=$1
                                ;;
        -p | --local-path ) shift
          			LOCAL_FILE_PATH=$1
        ;;
        -i | --inplace )   	shift
	       			PEER_NAME=$1
				checkYqVersion
				setValuesInLocalFile
                                ;;
        -h | --help )           usage
                                exit
                                ;;
	      -c| --check )		checkYqVersion
				;;
        * )                     usage
                                exit 1
    esac
    shift
done

if [ -z "$PEER_NAME" ]
then
 echo "peer name is missing" ${usage}
 exit 1
else
  checkYqVersion
  createEnvForNewConf
  setValuesInLocalFile
fi
