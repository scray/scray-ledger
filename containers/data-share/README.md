## Data share to share informations between components

```
cd scray-ledger/containers/data-share
```

### Provide volumes
 * Create local volume (for one node cluster only)
   ```
   kubectl apply -f local-data-share-vol.yaml
   ```

 * Create  a PersistentVolumeClaim
   ```
    kubectl apply -f data-share-pvc.yaml
   ```

### Start data share

    kubectl apply -f k8s-hl-fabric-data-share-service.yaml 
    kubectl apply -f k8s-hl-fabric-data-share.yaml

   
### Clean up

```
kubectl delete pod task-pv-pod
kubectl delete pvc task-pv-claim
kubectl delete pv task-pv-volume
```
