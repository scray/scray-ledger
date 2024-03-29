# Chaincode description creator

This tool creates a chaincode description and publishes it to a http data share.

### Start container
```
kubectl apply -f https://raw.githubusercontent.com/scray/scray-ledger/develop/chaincode/tools/hlf-chaincode-definition-creator/k8s-cc-deployer.yaml
```

### Publish deployment description
```
SHARED_FS=10.15.130.111
CC_HOSTNAME=$CC_INSTANCE_NAME-cc.org1.example.com
CC_INSTANCE_NAME=hl-fabric-cc-external-invoice
CC_SERVICE_NAME=$CC_INSTANCE_NAME
CC_PORT=$(kubectl get service $CC_SERVICE_NAME -o jsonpath="{.spec.ports[?(@.name=='chaincode')].nodePort}")
CC_LABEL=basic_1.0

CC_DEPLOYER_POD=$(kubectl get pod -l app=cc-deployer -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $CC_DEPLOYER_POD -c cc-deployer -- /bin/sh /opt/create-archive.sh $CC_HOSTNAME $CC_PORT $CC_LABEL $SHARED_FS
```

### Deployment description location
Location where the chaincode description (``chainecode_description.tgz`` and ``ccid.json``) can be downloaded from.  

``
http://$SHARED_FS/cc_descriptions/${HOSTNAME}_$CC_LABEL
``

