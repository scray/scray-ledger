package org.scray.ledger.rest.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.scray.hyperledger.fabric.client.EventBuffer;
import org.scray.ledger.rest.RestApplication;
import org.scray.ledger.rest.modules.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class GetEvents
{

    @Operation(summary = "Consume events",
                    description = "Consume DLT events",
                    security = {@SecurityRequirement(name = "BearerJWT")},
                    tags = {"Event-API"})
         @ApiResponses(value =
         {
           @ApiResponse(responseCode = "200", description = "OK",
                           content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Event.class)
                                               )
           )})
    @GetMapping(value = "/subscriptions/{subscriptionid}/events/")
    ResponseEntity<List<Event>> eventsGet(@PathVariable String subscriptionid) {

        List<Event> events = new ArrayList<Event>();
        events.add(new Event("e1", "id1", "abwwc".getBytes(), 1L));

        RestApplication.blockchainClient.getEvents()
        .forEach(event -> {events.add(new Event(event.getName(), event.getChaincodeId(), event.getPayload().orElse("".getBytes()), 1L));});

        return new ResponseEntity<List<Event>>(events, HttpStatus.OK);
    }


}



