# REST-API to consume event


## Run Server
  ``java -jar event-rest-api-0.0.1-SNAPSHOT.jar --wallet ~/wallet/``


## Swagger definition
  [Swagger UI](http://localhost:8080/swagger-ui/index.html#/Event-API/eventsGet)


## Examples
Subscripton
```
curl -X 'POST' \
  'http://localhost:8080/subscriptions/' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "chanelName": "channel-1",
  "chaincodeId": "basic",
  "userId": "alice",
  "wallet": "~/wallet/",
  "connectionProfil": "connection33.yaml"
}'
```

