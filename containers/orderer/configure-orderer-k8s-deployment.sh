#!/bin/bash

ORDERER_NAME=peer0.scray.org
BASE_PATH=$PWD

createEnvForNewConf() {
  mkdir -p target/$ORDERER_NAME
  cp k8s-orderer.yaml ./target/$ORDERER_NAME
  cp k8s-orderer-service.yaml ./target/$ORDERER_NAME
  cp configure-orderer-k8s-deployment.sh ./target/$ORDERER_NAME/
  cd ./target/$ORDERER_NAME/
}

yq() {
  $BASE_PATH/bin/yq $1 $2 $3 $4 $5 $6 $7 $8 $9
}

# Check if yq exists
checkYqVersion() {
  downloadYqBin
}

downloadYqBin() {
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

configureService() {
  echo "Configure service for orderer $ORDERER_NAME"
  yq w -i k8s-orderer-service.yaml "metadata.name" $ORDERER_NAME
  yq w -i k8s-orderer-service.yaml "metadata.labels.run" $ORDERER_NAME
  yq w -i k8s-orderer-service.yaml "spec.selector.app" $ORDERER_NAME
  yq w -i k8s-orderer-service.yaml "spec.ports(name==$ORDERER_NAME)" port-$ORDERER_NAME
}

configureOrdererDeployment() {
  echo "Configure deployment for orderer $ORDERER_NAME"

}


usage()
{
    echo "usage: Create peer K8s configuration [[[-n ] [-i]] | [-h]]"
}


while [ "$1" != "" ]; do
    case $1 in
        -n | --name )   shift
				ORDERER_NAME=$1
				checkYqVersion
				createEnvForNewConf
				configureService
                                ;;
        -i | --inplace )   	shift
	       			ORDERER_NAME=$1
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
