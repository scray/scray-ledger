## Event Management

### Scray-Ledger Event-API

REST-API to consume chaincode events. 

![Event API](swagger-screenshot.png)

More technical information can be found [here](event-rest-api)

```
LOCAL_WALLET_PATH=/home/alice/wallet/
WALLET_ID=alice.id
```

```
kubectl create secret generic event-rest-api-i1 --from-file=connection.yaml=$LOCAL_WALLET_PATH/connection.yaml --from-file=$WALLET_ID=$LOCAL_WALLET_PATH/$WALLET_ID
```