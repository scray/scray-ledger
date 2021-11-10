# Chaincode description creator

This tool creates a chaincode description and publishes it to a http data share.

### Publish deployment description
```
SHARED_FS=kubernetes.research.dev.seeburger.de:30080
HOSTNAME=asset-transfer-basic.org1.example.com
CC_SERVICE_NAME=hl-fabric-cc-external-invoice
CC_PORT=$(kubectl get service $CC_SERVICE_NAME -o jsonpath="{.spec.ports[?(@.name=='chaincode')].nodePort}")
LABEL=basic_1.0

CC_DEPLOYER_POD=$(kubectl get pod -l app=cc-deployer -o jsonpath="{.items[0].metadata.name}")
kubectl exec --stdin --tty $CC_DEPLOYER_POD -c cc-deployer -- /bin/sh /opt/create-archive.sh $HOSTNAME $CC_PORT $LABEL $SHARED_FS
```

### Deployment description location
Location where the chaincode description (``chainecode_description.tgz`` and ``ccid.json``) can be downloaded from.  

``
http://$SHARED_FS/cc_descriptions/${HOSTNAME}_$LABEL
``

