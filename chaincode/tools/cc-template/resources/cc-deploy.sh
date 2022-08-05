#!/bin/sh

WORKDIR=$(cd $(dirname $0) && pwd)
deploy() {

	  SHARED_FS=$1
    CC_HOSTNAME=$2

    echo "Push image to local registry"
    ./docker_deploy.sh -l

    echo "Create k8s service for smart contract"
    kubectl apply -f k8s-service-external-chaincode.yaml

    CC_PORT=$(kubectl get service "$CC_SERVICE_NAME" -o jsonpath="{.spec.ports[?(@.name=='chaincode')].nodePort}")
    CC_LABEL=basic_1.0

    if [ -z "$CC_PORT" ]
    then
        echo "No CC port  found"
        exit 1
    fi

    kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/tools/hlf-chaincode-definition-creator/k8s-cc-deployer.yaml
    CC_DEPLOYER_POD=$(kubectl get pod -l app=cc-deployer -o jsonpath="{.items[0].metadata.name}")



    if [ ! -z "$CC_DEPLOYER_POD" ]
    then
        kubectl exec --stdin --tty $CC_DEPLOYER_POD -c cc-deployer -- /bin/sh /opt/create-archive.sh $CC_HOSTNAME $CC_PORT $CC_LABEL $SHARED_FS
    else
        echo "No CC deployer found"
        exit 1
    fi

}

startChaincode() {

    SHARED_FS=$1
    CC_HOSTNAME=$2

    # Clean up
    kubectl delete deployment "$CC_NAME"

    # Get configuration
    CC_LABEL=basic_1.0

    CC_PORT=$(kubectl get service "$CC_SERVICE_NAME" -o jsonpath="{.spec.ports[?(@.name=='chaincode')].nodePort}")
    PKGID=$(curl -s  --user $SHARED_FS_USER:$SHARED_FS_PW http://$SHARED_FS/cc_descriptions/${CC_HOSTNAME}_$CC_LABEL/description-hash.json 2>&1 | jq -r '."description-hash"')

    if [ ! -z "$PKGID" ]
    then
        kubectl exec --stdin --tty $CC_DEPLOYER_POD -c cc-deployer -- /bin/sh /opt/create-archive.sh $CC_HOSTNAME $CC_PORT $CC_LABEL $SHARED_FS
    else
        echo "Package id is empty"
        exit 1
    fi

    kubectl delete configmap --ignore-not-found=true "$CC_NAME"
    echo "create configmap" "$CC_NAME"

    kubectl create configmap "$CC_NAME" \
        --from-literal=chaincode_id="$PKGID"

	kubectl apply -f k8s-external-chaincode.yaml
}

export $(cat .env | xargs)

while [ "$1" != "" ]; do
    case $1 in
        --data-share )   shift
 	   SHARED_FS=$1
	;;
        --caincode-hostname ) shift
        CC_HOSTNAME=$1
        ;;
    esac
    shift
done


if [ -z "$SHARED_FS" ]
then
  echo "value for parameter --data-share is missing"
  exit 1
fi

if [ -z "$CC_HOSTNAME" ]
then
  echo "chaincode hostname is missing"
  exit 1
fi

deploy $SHARED_FS $CC_HOSTNAME
startChaincode $SHARED_FS $CC_HOSTNAME