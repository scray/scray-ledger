package org.scray.ledger.rest.controllers;


import java.util.UUID;

import org.scray.ledger.rest.modules.Subscription;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
public class Subscribe
{
    @PostMapping(value = "/subscriptions/")
    @Operation(
               tags =
               { "Event-API" },
               responses =
               { @ApiResponse(responseCode = "200",
                              content = @Content(
                                                 mediaType = MediaType.APPLICATION_JSON_VALUE),
                              description = "Success Response.") },
               summary = "Create event subscription",
               description = "Create event subscription",
               requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Channel and chaincode id",
                                                                                   content = @Content(schema = @Schema(implementation = Subscription.class),
                                                                                                      mediaType = MediaType.APPLICATION_JSON_VALUE)),
               security =
               { @SecurityRequirement(name = "BearerJWT") })
    public ResponseEntity<Object> createSubscription(@RequestBody Subscription subscription)
    {
        return ResponseEntity.ok().body(UUID.randomUUID());
    }
}
