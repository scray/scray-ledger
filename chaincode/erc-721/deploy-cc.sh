### Create Service
kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/erc-721/k8s-service-external-chaincode.yaml

### Publish chaincode definition
CC_HOSTNAME=hl-fabric-erc-721-example.example.com
CC_SERVICE_NAME=hl-fabric-erc-721-example
CC_PORT=$(kubectl get service $CC_SERVICE_NAME -o jsonpath="{.spec.ports[?(@.name=='chaincode')].nodePort}")
CC_LABEL=basic_1.0

CC_DEPLOYER_POD=$(kubectl get pod -l app=cc-deployer -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $CC_DEPLOYER_POD -c cc-deployer -- /bin/sh /opt/create-archive.sh $CC_HOSTNAME $CC_PORT $CC_LABEL $SHARED_FS