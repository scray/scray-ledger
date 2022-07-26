package org.scray.ledger.rest.controllers;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.scray.hyperledger.fabric.client.EventSubscriptionClient;
import org.scray.ledger.rest.RestApplication;
import org.scray.hyperledger.fabric.client.Block;
import org.scray.hyperledger.fabric.client.BlockReaderClient;
import org.scray.hyperledger.fabric.client.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
public class Subscribe
{
    private static final Logger logger = LoggerFactory.getLogger(Subscribe.class);

    @Operation(summary = "Get block",
               description = "Get block of blockchain",
               security =
               { @SecurityRequirement(name = "BearerJWT") },
               tags =
               { "Block-API" })
    @ApiResponses(value =
    {
      @ApiResponse(responseCode = "200",
                   description = "OK",
                   content = @Content(
                                      mediaType = MediaType.APPLICATION_JSON_VALUE,
                                      schema = @Schema(implementation = Block.class))) })
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/blocks/{channel}/{blocknumber}")
    ResponseEntity<Block> glockGet(@PathVariable String channel, @PathVariable Long blocknumber)
    {
        String user = "aubonmoulin"; // FIXME add to client management
        String connectionKey = channel + "_" + user;

        if (RestApplication.blockClients.get(connectionKey) == null)
        {
            RestApplication.blockClients.put(connectionKey, new BlockReaderClient(RestApplication.options.getWalletPath(), channel, "basic",
                                                                                  user, Optional.of("connection.yaml"))); // FIXME add to client management
        }

        var blockClient = RestApplication.blockClients.get(connectionKey);
        return new ResponseEntity<Block>(blockClient.getBlock(blocknumber), HttpStatus.OK);
    }
}
