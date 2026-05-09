package com.proiect.restaurant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI cafeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cafe Ordering & Reservation System API")
                        .description("RESTful API for managing cafe reservations, orders, and menu items")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Cafe API Support")
                                .email("support@cafe.com")));
    }
}
