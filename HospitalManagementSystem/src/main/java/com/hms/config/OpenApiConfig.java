package com.hms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/** Title and description shown at the top of Swagger UI. */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hospitalOpenApi() {
        return new OpenAPI().info(new Info()
                .title("RestHMS: Hospital Management REST API")
                .version("1.0")
                .description("CRUD for doctors and patients (Spring Boot, Spring Data JPA). "
                        + "The live demo runs on an in-memory database with sample data that resets on restart.")
                .contact(new Contact().name("Abhiram Kamarajugadda")
                        .url("https://github.com/abhiramkamarajugadda/RestHMS")));
    }
}
