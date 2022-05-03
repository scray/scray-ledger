
### Pushing to Docker Hub

```
DOCKER_USERNAME=[...]
DOCKER_PASSWORD=[...]

kubectl create configmap docker-push
	--from-literal=registry=scrayorg/test1

kubectl create secret docker-registry regcred \
    --docker-username=${DOCKER_USERNAME} \
    --docker-password=${DOCKER_PASSWORD}


kubectl apply -f k8s-publish-description.sh
```

### Example app
``-dockerfile-creator-exampl
kubectl apply -f k8s-dockerfile-creator-example.yaml
kubectl exec --stdin --tty k8s-dockerfile-creator-example  -- /bin/bash
```
