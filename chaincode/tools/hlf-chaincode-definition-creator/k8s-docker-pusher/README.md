
### Pushing image to Docker Registry

```
DOCKER_USERNAME=[...]
DOCKER_PASSWORD=[...]

kubectl create configmap docker-push
	--from-literal=registry=myregistry/scrayorg/test1

kubectl create secret docker-registry regcred \
    --docker-username=${DOCKER_USERNAME} \
    --docker-password=${DOCKER_PASSWORD}


kubectl apply -f k8s-publish-description.sh
```

### Example app
```
kubectl apply -f k8s-dockerfile-creator-example.yaml
kubectl exec --stdin --tty k8s-dockerfile-creator-example  -- /bin/bash
```
* Edit /workspace/Dockerfile
