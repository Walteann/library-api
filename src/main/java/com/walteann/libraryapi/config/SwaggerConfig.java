package com.walteann.libraryapi.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("spring-api-library-public")
                .packagesToScan("com.walteann.libraryapi.api.resource")
                // .pathsToMatch("com.walteann.libraryapi.api.resource")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Library API")
                        .description("API Projeto de controle de aluguel de livros")
                        .version("v0.0.1")
                        .contact(contact()));
    }

    private Contact contact() {
        Contact teste = new Contact();
        teste.setEmail("walteann3@gmail.com");
        teste.setName("Walteann Costa");
        teste.setUrl("https://github.com/Walteann");
        return teste;
    }

}
