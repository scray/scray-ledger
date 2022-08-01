# Create project for a kubernetes deployable hyperledger fabric external smart contract

## Create project
```bash
CC_NAME=super-smart-contract
./create-cc-project.sh --name $CC_NAME  --docker-tag repo1/cc-example:0.1
```

## Deploy created smart contract
```bash
cd target/$CC_NAME
./cc-deploy.sh --data-share 10.15.130.111 --caincode-hostname chaincode.host.example.com
```

##  Extend example chaincode

This script creates a go chaincode project with components to deploy this chaincode as external service in a K8s cluster.
This project contains an example implementation for a ERC-721 token scenario.
[Original implementation]((https://github.com/hyperledger/fabric-samples/tree/main/token-erc-721))  

The created project can be found at ./target/$CC_NAME  
Details for deploying this chaincode can be found in the provided README.md of the created project.

## Convert local chaincode to chaincode as external service
Most go chain codes can be converted by passing the chaincode instance to the ```shim.ChaincodeServer```.
For details read the Hyperledger Fabric [docs](https://hyperledger-fabric.readthedocs.io/en/release-2.2/cc_service.html)  

Main method example:
```go
package main

import (
	"os"
	"github.com/hyperledger/fabric-chaincode-go/shim"
	"github.com/hyperledger/fabric-contract-api-go/contractapi"
	"github.com/hyperledger/fabric-contract-api-go/metadata"
)

type serverConfig struct {
	CCID    string
	Address string
}

func main() {

    config := serverConfig{
      CCID:    os.Getenv("CHAINCODE_ID"),
      Address: os.Getenv("CHAINCODE_SERVER_ADDRESS"),
    }

    // FIXME create chaincode instance
    chaincode=...
    
    server := &shim.ChaincodeServer{
        CCID:    config.CCID,
        Address: config.Address,
        CC:      chaincode,
        TLSProps: shim.TLSProperties{
          Disabled: true,
      },
    }

    if err := server.Start(); err != nil {
      panic("Failed to start chaincode. " + err.Error())
    }
}
```
