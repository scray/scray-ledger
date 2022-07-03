package org.scray.ledger.rest.controllers;


import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.scray.hyperledger.fabric.client.EventSubscriptionClient;
import org.scray.ledger.rest.RestApplication;
import org.scray.ledger.rest.Subscriptions;
import org.scray.hyperledger.fabric.client.Event;
import org.scray.ledger.rest.modules.Subscription;
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
        logger.debug("Subscripton parameters: " + subscription);

        if (RestApplication.subscriptions.isEmpty())
        {
            logger.debug("Add subscription " + subscription.toString());

            RestApplication.subscriptions = Optional.of(new Subscriptions());
        }

        var subscriptionId = RestApplication.subscriptions.get().addSubscription(subscription);
        RestApplication.subscriptions.get().connect(subscriptionId);

        return ResponseEntity.ok().body(subscriptionId);
    }


    @Operation(summary = "Consume events",
               description = "Consume DLT events",
               security =
               { @SecurityRequirement(name = "BearerJWT") },
               tags =
               { "Event-API" })
    @ApiResponses(value =
    {
      @ApiResponse(responseCode = "200",
                   description = "OK",
                   content = @Content(
                                      mediaType = MediaType.APPLICATION_JSON_VALUE,
                                      schema = @Schema(implementation = Event.class))) })
    @GetMapping(value = "/subscriptions/{subscriptionid}/events/")
    ResponseEntity<List<Event>> eventsGet(@PathVariable String subscriptionid)
    {

        java.util.List<Event> latestEvent = RestApplication.subscriptions.get().getLastEvents(UUID.fromString(subscriptionid));

        return new ResponseEntity<List<Event>>(latestEvent, HttpStatus.OK);
    }

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
                                           schema = @Schema(implementation = Event.class))) })
         @GetMapping(value = "/blocks/{chanel}/{blocknumber}")
         ResponseEntity<String> glockGet(@PathVariable String chanel, @PathVariable String blocknumber)
         {

             //java.util.List<Event> latestEvent = RestApplication.subscriptions.get().getLastEvents(UUID.fromString(subscriptionid));

             return new ResponseEntity<String>("", HttpStatus.OK);
         }
}
