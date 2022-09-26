#!/bin/bash

CC_INSTANCE_NAME=""
BASE_PATH=$PWD

yq() {
  downloadYqBin
  $BASE_PATH/bin/yq $1 $2 $3 $4 $5 $6 $7 $8 $9
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

createEnvForNewConf() {
  mkdir -p target/$CC_INSTANCE_NAME
  cp k8s-service-external-chaincode.yaml ./target/$CC_INSTANCE_NAME/k8s-service-external-chaincode-$CC_INSTANCE_NAME.yaml
  cp k8s-external-chaincode.yaml ./target/$CC_INSTANCE_NAME/k8s-external-chaincode-$CC_INSTANCE_NAME.yaml
  cp configure-service.sh ./target/$CC_INSTANCE_NAME
}

setServiceName() {
  SERVICE_YAML=./target/$CC_INSTANCE_NAME/k8s-service-external-chaincode-$CC_INSTANCE_NAME.yaml
  yq w -i "$SERVICE_YAML" "metadata.name" $CC_INSTANCE_NAME
  yq w -i "$SERVICE_YAML" "metadata.labels.run" $CC_INSTANCE_NAME
  yq w -i "$SERVICE_YAML" "spec.selector.app" $CC_INSTANCE_NAME
}

setDeploymentDescriptorParameters() {
  DEPLOYMENT_YAML=./target/$CC_INSTANCE_NAME/k8s-external-chaincode-$CC_INSTANCE_NAME.yaml

  yq w -i "$DEPLOYMENT_YAML" "metadata.name" $CC_INSTANCE_NAME
  yq w -i "$DEPLOYMENT_YAML" "metadata.labels.app" $CC_INSTANCE_NAME
  yq w -i "$DEPLOYMENT_YAML" "spec.selector.matchLabels.app" $CC_INSTANCE_NAME
  yq w -i "$DEPLOYMENT_YAML" "spec.template.metadata.labels.app" $CC_INSTANCE_NAME

  yq w -i "$DEPLOYMENT_YAML" "spec.template.spec.containers(name==invoice-chaincode-external).name" $CC_INSTANCE_NAME
  yq w -i "$DEPLOYMENT_YAML" "spec.template.spec.containers(name==$CC_INSTANCE_NAME).env(name==CHAINCODE_ID).valueFrom.configMapKeyRef.name" $CC_INSTANCE_NAME
}



usage() {
    echo "usage: configure chaincode k8s components. --instance-name examplecc"
}

while [ "$1" != "" ]; do
    case $1 in
        -i | --instance-name )   shift
                                CC_INSTANCE_NAME=$1
                                ;;
        --help )                usage
                                exit 1
    esac
    shift
done

if [[ -z "$CC_INSTANCE_NAME" ]]
then
    echo "Instance name is empty. Use default cc-service-i1"
    CC_INSTANCE_NAME="cc-service-i1"
fi

createEnvForNewConf

echo "Configure service description"
setServiceName

echo "Configure deployment descriptor"
setDeploymentDescriptorParameters
