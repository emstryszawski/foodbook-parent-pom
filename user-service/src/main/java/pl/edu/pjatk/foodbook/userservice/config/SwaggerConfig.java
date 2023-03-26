package pl.edu.pjatk.foodbook.userservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "User API",
        description = "API for managing user data",
        version = "0.0.1"
    ),
    servers = @Server(url = "http://localhost:8010"))
public class SwaggerConfig {
}
