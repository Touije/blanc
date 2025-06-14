package com.ranblanc.blanc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de Swagger/OpenAPI pour documenter l'API REST.
 * Cette classe configure l'interface Swagger UI accessible via /swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configure l'objet OpenAPI avec les informations sur l'API.
     * 
     * @return L'objet OpenAPI configuré
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API RanBlanc")
                        .version("1.0")
                        .description("Documentation de l'API de réservation de ressources RanBlanc")
                        .termsOfService("https://ranblanc.com/terms")
                        .contact(new Contact()
                                .name("Support RanBlanc")
                                .url("https://ranblanc.com/contact")
                                .email("contact@ranblanc.com"))
                        .license(new License()
                                .name("Licence Open Source")
                                .url("https://ranblanc.com/license")))
                // Configuration de la sécurité pour Swagger UI
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}