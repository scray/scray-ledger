#!/bin/bash

BASE_PATH=$PWD

createEnvForNewConf() {
  CC_NAME=$1
  CC_DESTINATION=$2

  mkdir -p "$CC_DESTINATION"/"$CC_NAME"
  cp -r resources/*  "$CC_DESTINATION"/"$CC_NAME"

  cd "$CC_DESTINATION"/"$CC_NAME"
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

setValuesInLocalFile() {
  CC_NAME=$1
  DOCKER_IMAGE_NAME=$2

  # Configure service
  yq w -i k8s-service-external-chaincode.yaml "metadata.name" "$CC_NAME"
  yq w -i k8s-service-external-chaincode.yaml "metadata.labels.run" "$CC_NAME"
  yq w -i k8s-service-external-chaincode.yaml "spec.selector.app" "$CC_NAME"

  # Configure deployment descriptor
  yq w -i  k8s-external-chaincode.yaml "metadata.name" "$CC_NAME"
  yq w -i  k8s-external-chaincode.yaml "metadata.labels.app" "$CC_NAME"
  yq w -i  k8s-external-chaincode.yaml "spec.selector.matchLabels.app" "$CC_NAME"
  yq w -i  k8s-external-chaincode.yaml "spec.template.metadata.labels.app" "$CC_NAME"
  yq w -i  k8s-external-chaincode.yaml "spec.template.spec.containers(name==hl-fabric-erc-721-example).image" "$DOCKER_IMAGE_NAME"
  yq w  -i k8s-external-chaincode.yaml "spec.template.spec.containers(name==hl-fabric-erc-721-example).env(name==CHAINCODE_ID).valueFrom.configMapKeyRef.name" "$CC_NAME"
  yq w  -i k8s-external-chaincode.yaml "spec.template.spec.containers(name==hl-fabric-erc-721-example).name" "$CC_NAME"

  echo DOCKER_IMAGE_NAME="$DOCKER_IMAGE_NAME" >> .env
}


while [ "$1" != "" ]; do
    case $1 in
        -n | --name )   shift
				CC_NAME=$1
				checkYqVersion
				createEnvForNewConf "$CC_NAME" target
				setValuesInLocalFile "$CC_NAME" "$CC_NAME"
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