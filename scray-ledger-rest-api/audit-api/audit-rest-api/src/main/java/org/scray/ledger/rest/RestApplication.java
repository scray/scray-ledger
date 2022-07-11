package org.scray.ledger.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.cli.ParseException;
import org.scray.hyperledger.fabric.client.BlockReaderClient;
import org.scray.hyperledger.fabric.client.BlockchainOperations;
import org.scray.hyperledger.fabric.client.EventBuffer;
import org.scray.hyperledger.fabric.client.EventSubscriptionClient;
import org.scray.hyperledger.fabric.client.listener.ContractListener;
import org.scray.ledger.rest.cli.CLIOptions;
import org.scray.ledger.rest.cli.CLIOptionsParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Scray-Ledger Audit-API", version = "1.0.0"
		, description = "This a REST-API to query blocks from Scray-Ledger",
		license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")),
		servers = {@Server(url = "http://localhost:8082"), @Server(url = "http://events.blockchain.research.dev.seeburger.de:8082")},
		tags = {@Tag(name = "Block-API", description = "Resources to query blocks from  Scray-Ledger")}
)

@SecurityScheme(name = "BearerJWT", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT",
description = "Bearer token for the project.")

public class RestApplication {
    public static Map<String, BlockReaderClient> blockClients = new HashMap<String, BlockReaderClient>();
    public static CLIOptions options;

	public static void main(String[] args) throws ParseException {

	    CLIOptionsParser optionsParser = new CLIOptionsParser();

	    options = optionsParser.parseParameters(args);

		SpringApplication.run(RestApplication.class, args);
	}

}
