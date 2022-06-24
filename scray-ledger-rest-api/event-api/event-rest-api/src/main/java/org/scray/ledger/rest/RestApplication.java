package org.scray.ledger.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.cli.ParseException;
import org.scray.hyperledger.fabric.client.EventSubscriptionClient;
import org.scray.ledger.rest.cli.CLIOptions;
import org.scray.ledger.rest.cli.CLIOptionsParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Scray-Ledger Event-API", version = "1.0.0"
		, description = "This a REST-API to access events from Scray-Ledger",
		license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")),
		servers = {@Server(url = "http://localhost:8080"), @Server(url = "http://events.blockchain.research.dev.seeburger.de:8080")},
		tags = {@Tag(name = "Event-API", description = "Resources to consume Scray-Ledger events")}
)

@SecurityScheme(name = "BearerJWT", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT",
description = "Bearer token for the project.")

public class RestApplication {

    public static EventSubscriptionClient blockchainClient = null;

	public static void main(String[] args) throws ParseException {

	    CLIOptionsParser optionsParser = new CLIOptionsParser();

	    CLIOptions options = optionsParser.parseParameters(args);

        String walletPath = options.getWalletPath();

        RestApplication.blockchainClient = new EventSubscriptionClient(Paths.get(walletPath), "channel-1", "basic", "alice", Optional.of("connection33.yaml"));
        RestApplication.blockchainClient.run();

		SpringApplication.run(RestApplication.class, args);
	}

}
