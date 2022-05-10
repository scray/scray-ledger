#1/bin/bash

NAMESPACE=scray-ledger-asset-transfer

# Delete all deplyments
function clean() {
  echo Delete all deployments in namespace $NAMESPACE
  kubectl delete --all deployments --namespace=$NAMESPACE
  echo Delete all persistenc volume claims in namespace $NAMESPACE
  kubectl delete pvc --all --namespace=$NAMESPACE
  echo Delete all persistent volumes in namespace $NAMESPACE
  kubectl delete pv --all --namespace=$NAMESPACE
}

function usage() {
	echo "Required parameters --k8s-namespace "
}

while [ "$1" != "" ]; do
    case $1 in
        --k8s-namespace )   shift
 	   NAMESPACE=$1
	;;
        * )    
		usage
          	exit 1
    esac
    shift
done

if [ -z "$NAMESPACE" ]
then
 echo "K8s namespace is missing" ${usage}
 exit 1
fi

clean
