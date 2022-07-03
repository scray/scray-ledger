package org.scray.ledger.rest.controllers;


import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.scray.hyperledger.fabric.client.EventSubscriptionClient;
import org.scray.ledger.rest.Block;
import org.scray.ledger.rest.RestApplication;
import org.scray.hyperledger.fabric.client.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
         @GetMapping(value = "/blocks/{channel}/{blocknumber}")
         ResponseEntity<String> glockGet(@PathVariable String channel, @PathVariable String blocknumber)
         {


             return new ResponseEntity<String>("", HttpStatus.OK);
         }
}
