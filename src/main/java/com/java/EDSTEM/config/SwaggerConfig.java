package com.java.EDSTEM.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Review API")
                        .version("1.0.0")
                        .description("API documentation for the Restaurant Review System.")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@example.com")
                        )
                );
    }
}
