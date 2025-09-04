package com.example.notesbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point for the Notes Backend.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Notes API",
                version = "0.1.0",
                description = "REST API for user-authenticated note keeping with CRUD operations."
        )
)
@Tag(name = "Notes Backend", description = "Notes API services")
@SpringBootApplication
public class NotesBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesBackendApplication.class, args);
    }
}
