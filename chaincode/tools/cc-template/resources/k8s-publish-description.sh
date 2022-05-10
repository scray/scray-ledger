apiVersion: batch/v1
kind: Job
metadata:
  name: kaniko
spec:
  template:
    spec:
      containers:
      - name: kaniko
        image: gcr.io/kaniko-project/executor:latest
        args: ["--dockerfile=Dockerfile",
               "--context=git://https://github.com/scray/scray-ledger.git#refs/heads/dev",
               "--destination=scrayorg/chaincode:v1"]
        volumeMounts:
        - name: kaniko-secret
          mountPath: "/kaniko/.docker"
      restartPolicy: Never
      volumes:
      - name: kaniko-secret
        secret:
          secretName: regcred
          items:
          - key: .dockerconfigjson
            path: config.json