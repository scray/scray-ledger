package org.scray.ledger.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-29T13:50:34.103Z[GMT]")
@Configuration
public class SwaggerDocumentationConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Scray-Ledger Event-API")
                .description("This a REST-API to access events from Scray-Ledger")
                .termsOfService("")
                .version("1.0.0")
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0"))
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .email("")));
    }

}
