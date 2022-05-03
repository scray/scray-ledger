apiVersion: batch/v1
kind: Job
metadata:
  name: kaniko
spec:
  template:
    spec:
      containers:
        - name: kaniko
          image: 'gcr.io/kaniko-project/executor:latest'
          env:
            - name: REGISTRY
              valueFrom:
                configMapKeyRef:
                  name: docker-push
                  key: registry
          args:
            - '--dockerfile=/workspace/Dockerfile'
            - '--context=dir:///workspace/'
            - '--destination=$(REGISTRY)'
            - '--skip-tls-verify'
          volumeMounts:
           - name: docker-push-vol
             mountPath: "/workspace/"
           - name: docker-registry
             mountPath: /kaniko/.docker
      restartPolicy: Never
      volumes:
        - name: docker-registry
          secret:
            secretName: regcred
            items:
              - key: .dockerconfigjson
                path: config.json
        - name: docker-push-vol
          persistentVolumeClaim:
            claimName: docker-push-vol-claim
