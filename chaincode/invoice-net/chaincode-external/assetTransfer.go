/*
SPDX-License-Identifier: Apache-2.0
*/

package main

import (
	"encoding/base64"
	"encoding/json"
	"fmt"
	"log"
	"os"

	"github.com/hyperledger/fabric-chaincode-go/shim"
	"github.com/hyperledger/fabric-contract-api-go/contractapi"
)

type serverConfig struct {
	CCID    string
	Address string
}

// SmartContract provides functions for managing an asset
type SmartContract struct {
	contractapi.Contract
}

// Asset describes basic details of what makes up a simple asset
type Asset struct {
	ID                 string  `json:"ID"`
	Owner              string  `json:"owner"`
	Buyer              string  `json:"buyer"`
	Hash               int     `json:"hash"`
	InvoiceNumber      string  `json:"invoiceNumber"`
	Tax                float32 `json:"tax"`
	Netto              float32 `json:"netto"`
	CountryOrigin      string  `json:"countryOrigin"`
	CountryBuyer       string  `json:"countryBuyer"`
	Received           bool    `json:"received"`
	ReceivedOrder      bool    `json:"receivedOrder"`
	Sold               bool    `json:"sold"`
	ClaimPaid          bool    `json:"claimPaid"`
	ClaimPaidBy        string  `json:"claimPaidBy"`
	TaxExemptionReason string  `json:"taxExemptionReason"`
	TaxReceived        bool    `json:"taxReceived"`
}

type Role int

const (
	Buyer Role = iota
	Seller
	Factor
	TaxInspector
)

var roles map[string]Role = make(map[string]Role)

// QueryResult structure used for handling result of query
type QueryResult struct {
	Key    string `json:"Key"`
	Record *Asset
}

// InitLedger adds a base set of cars to the ledger
func (s *SmartContract) InitLedger(ctx contractapi.TransactionContextInterface) error {
	assets := []Asset{
		{ID: "asset1", Owner: "company", Hash: 0, InvoiceNumber: "0", Tax: 0.0, Netto: 0.0, CountryOrigin: "DE", CountryBuyer: "DE", Received: false,
			ReceivedOrder: false, Sold: false, ClaimPaid: false, ClaimPaidBy: "", TaxExemptionReason: "", TaxReceived: false},
	}
	for _, asset := range assets {
		assetJSON, err := json.Marshal(asset)
		if err != nil {
			return err
		}

		err = ctx.GetStub().PutState(asset.ID, assetJSON)
		if err != nil {
			return fmt.Errorf("failed to put to world state: %v", err)
		}
	}

	return nil
}

// CreateAsset issues a new asset to the world state with given details.
func (s *SmartContract) CreateAsset(ctx contractapi.TransactionContextInterface, id, owner string, buyer string, hash int,
	invoiceNumber string, tax float32, netto float32, countryOrigin string, CountryBuyer string, received bool,
	receivedOrder bool, sold bool, claimPaid bool, claimPaidBy string, taxExemptionReason string, taxReceived bool,
) error {
	exists, err := s.AssetExists(ctx, id)
	if err != nil {
		return err
	}
	if exists {
		return fmt.Errorf("the asset %s already exists", id)
	}

	// test
	//clientOrgID, err := ctx.GetClientIdentity().GetMSPID()

	// Get ID of submitting client identity
	clientID, err := s.GetSubmittingClientIdentity(ctx)
	if err != nil {
		return err
	}

	asset := Asset{
		ID:                 id,
		Owner:              clientID,
		Buyer:              buyer,
		Hash:               hash,
		InvoiceNumber:      invoiceNumber,
		Tax:                tax,
		Netto:              netto,
		CountryOrigin:      countryOrigin,
		CountryBuyer:       CountryBuyer,
		Received:           received,
		ReceivedOrder:      receivedOrder,
		Sold:               sold,
		ClaimPaid:          claimPaid,
		ClaimPaidBy:        claimPaidBy,
		TaxExemptionReason: taxExemptionReason,
		TaxReceived:        taxReceived,
	}

	assetJSON, err := json.Marshal(asset)
	if err != nil {
		return err
	}

	return ctx.GetStub().PutState(id, assetJSON)
}

// ReadAsset returns the asset stored in the world state with given id.
func (s *SmartContract) ReadAssetTest(ctx contractapi.TransactionContextInterface, id string) (*Asset, error) {
	assetJSON, err := ctx.GetStub().GetState(id)
	if err != nil {
		return nil, fmt.Errorf("failed to read from world state. %s", err.Error())
	}
	if assetJSON == nil {
		return nil, fmt.Errorf("the asset %s does not exist", id)
	}

	var asset Asset
	err = json.Unmarshal(assetJSON, &asset)
	if err != nil {
		return nil, err
	}

	return &asset, nil
}

// ReadAsset returns the asset stored in the world state with given id.
func (s *SmartContract) ReadAsset(ctx contractapi.TransactionContextInterface, id string) (*Asset, error) {

	assetJSON, err := ctx.GetStub().GetState(id)
	if err != nil {
		return nil, fmt.Errorf("failed to read from world state. %s", err.Error())
	}
	if assetJSON == nil {
		return nil, fmt.Errorf("the asset %s does not exist", id)
	}

	var asset Asset
	err = json.Unmarshal(assetJSON, &asset)
	if err != nil {
		return nil, err
	}

	// Get ID of submitting client identity
	clientID, err := s.GetSubmittingClientIdentity(ctx)
	if err != nil {
		return nil, err
	}

	if clientID == asset.Owner || clientID == asset.Buyer {
		return &asset, nil
	} else {
		return nil, fmt.Errorf("Only InvoiceOwner or ProductBuyer are allow to read this invoice ")
	}
}

// UpdateAsset updates an existing asset in the world state with provided parameters.
func (s *SmartContract) UpdateAsset(ctx contractapi.TransactionContextInterface, id, owner string, hash int,
	invoiceNumber string, Tax float32, netto float32, countryOrigin string, CountryBuyer string, received bool,
	receivedOrder bool, sold bool, claimPaid bool, claimPaidBy string, taxExemptionReason string, taxReceived bool) error {
	exists, err := s.AssetExists(ctx, id)
	if err != nil {
		return err
	}
	if !exists {
		return fmt.Errorf("the asset %s does not exist", id)
	}

	// overwritting original asset with new asset
	asset := Asset{
		ID:                 id,
		Owner:              owner,
		Hash:               hash,
		InvoiceNumber:      invoiceNumber,
		Tax:                Tax,
		Netto:              netto,
		CountryOrigin:      countryOrigin,
		CountryBuyer:       CountryBuyer,
		Received:           received,
		ReceivedOrder:      receivedOrder,
		Sold:               sold,
		ClaimPaid:          claimPaid,
		ClaimPaidBy:        claimPaidBy,
		TaxExemptionReason: taxExemptionReason,
		TaxReceived:        taxReceived,
	}

	assetJSON, err := json.Marshal(asset)
	if err != nil {
		return err
	}

	return ctx.GetStub().PutState(id, assetJSON)
}

// DeleteAsset deletes an given asset from the world state.
func (s *SmartContract) DeleteAsset(ctx contractapi.TransactionContextInterface, id string) error {
	exists, err := s.AssetExists(ctx, id)
	if err != nil {
		return err
	}
	if !exists {
		return fmt.Errorf("the asset %s does not exist", id)
	}

	return ctx.GetStub().DelState(id)
}

// AssetExists returns true when asset with given ID exists in world state
func (s *SmartContract) AssetExists(ctx contractapi.TransactionContextInterface, id string) (bool, error) {
	assetJSON, err := ctx.GetStub().GetState(id)
	if err != nil {
		return false, fmt.Errorf("failed to read from world state. %s", err.Error())
	}

	return assetJSON != nil, nil
}

// TransferAsset updates the owner field of asset with given id in world state.
func (s *SmartContract) TransferAsset(ctx contractapi.TransactionContextInterface, id string, newOwner string) error {
	asset, err := s.ReadAsset(ctx, id)
	if err != nil {
		return err
	}

	asset.Owner = newOwner
	assetJSON, err := json.Marshal(asset)
	if err != nil {
		return err
	}

	return ctx.GetStub().PutState(id, assetJSON)
}

// GetAllAssets returns all assets found in world state
func (s *SmartContract) GetAllAssets(ctx contractapi.TransactionContextInterface) ([]QueryResult, error) {
	// range query with empty string for startKey and endKey does an open-ended query of all assets in the chaincode namespace.
	resultsIterator, err := ctx.GetStub().GetStateByRange("", "")

	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	var results []QueryResult

	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()

		if err != nil {
			return nil, err
		}

		var asset Asset
		err = json.Unmarshal(queryResponse.Value, &asset)
		if err != nil {
			return nil, err
		}

		queryResult := QueryResult{Key: queryResponse.Key, Record: &asset}
		results = append(results, queryResult)
	}

	return results, nil
}

func (s *SmartContract) GetSubmittingClientIdentity(ctx contractapi.TransactionContextInterface) (string, error) {

	b64ID, err := ctx.GetClientIdentity().GetID()
	if err != nil {
		return "", fmt.Errorf("Failed to read clientID: %v", err)
	}
	decodeID, err := base64.StdEncoding.DecodeString(b64ID)
	if err != nil {
		return "", fmt.Errorf("failed to base64 decode clientID: %v", err)
	}
	return string(decodeID), nil
}

func (s *SmartContract) AppendRole(ctx contractapi.TransactionContextInterface, name string) (string, error) {

	//println(role, name)
	roles["test"] = TaxInspector
	println(name, roles["test"])
	//roles[name] = role
	//return string(roles[name])
	return string(roles["test"]), nil
}

func (s *SmartContract) GetRoles(ctx contractapi.TransactionContextInterface) (string, error) {

	//println(role, name)
	//roles["test"] = TaxInspector
	//println(name, roles["test"])
	//roles[name] = role
	//return string(roles[name])

	//var result = "{'Role': " + roles["test"] + "}"
	result := fmt.Sprintf("{'Role': " + string(roles["test"]) + "}")
	println(result)

	return result, nil
}

func main() {
	// See chaincode.env.example
	config := serverConfig{
		CCID:    os.Getenv("CHAINCODE_ID"),
		Address: os.Getenv("CHAINCODE_SERVER_ADDRESS"),
	}

	chaincode, err := contractapi.NewChaincode(&SmartContract{})

	if err != nil {
		log.Panicf("error create asset-transfer-basic chaincode: %s", err)
	}

	server := &shim.ChaincodeServer{
		CCID:    config.CCID,
		Address: config.Address,
		CC:      chaincode,
		TLSProps: shim.TLSProperties{
			Disabled: true,
		},
	}

	if err := server.Start(); err != nil {
		log.Panicf("error starting asset-transfer-basic chaincode: %s", err)
	}
}
