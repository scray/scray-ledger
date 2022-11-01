#!/bin/sh

SHARED_FS=hl-fabric-data-share-service:80
WORKDIR=$(cd $(dirname $0) && pwd)

deploy() {

	SHARED_FS=$1
	INSTANCE_NAME=$2

  # Create service for external chaincode
  echo "Create service for external chaincode"
  kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/chaincode-external/k8s-service-external-chaincode.yaml
  kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/chaincode-external/k8s-external-chaincode.yaml

  kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/tools/hlf-chaincode-definition-creator/k8s-cc-deployer.yaml

  CC_HOSTNAME=asset-transfer-basic.org1.example.com
  CC_SERVICE_NAME=hl-fabric-cc-external-invoice
  CC_PORT=$(kubectl get service $CC_SERVICE_NAME -o jsonpath="{.spec.ports[?(@.name=='chaincode')].nodePort}")
  CC_LABEL=basic_1.0


  while [[ $(kubectl get pods $CC_DEPLOYER_POD -o 'jsonpath={..status.conditions[?(@.type=="Ready")].status}') != "True" ]]; do echo "waiting for cc deployer pod $CC_DEPLOYER_POD" && CC_DEPLOYER_POD=$(kubectl get pod -l app=cc-deployer -o jsonpath="{.items[0].metadata.name}") && sleep 1; done

  echo "Publish deployment description"
  kubectl exec --stdin --tty "$CC_DEPLOYER_POD" -c cc-deployer -- /bin/sh /opt/create-archive.sh $CC_HOSTNAME "$CC_PORT" "$CC_LABEL" "$SHARED_FS"


}

startChaincode() {

  SHARED_FS=$1

  # Clean up
  kubectl delete deployment invoice-chaincode-external

  # Get configuration
  CC_HOSTNAME=asset-transfer-basic.org1.example.com
  CC_LABEL=basic_1.0
  SHARED_FS_USER=scray
  SHARED_FS_PW=scray
  PKGID=$(curl -s  --user $SHARED_FS_USER:$SHARED_FS_PW http://"$SHARED_FS"/cc_descriptions/${CC_HOSTNAME}_$CC_LABEL/description-hash.json 2>&1 | jq -r '."description-hash"')
	kubectl delete configmap --ignore-not-found=true invoice-chaincode-external
	kubectl create configmap invoice-chaincode-external \
		 --from-literal=chaincode_id="$PKGID"

	kubectl apply -f "$WORKDIR"/../../chaincode/chaincode-external/k8s-external-chaincode.yaml

}

while [ "$1" != "" ]; do
    case $1 in
        --data-share )   shift
 	   SHARED_FS=$1
	;;
    esac
    shift
done

deploy "$SHARED_FS"
startChaincode "$SHARED_FS"
