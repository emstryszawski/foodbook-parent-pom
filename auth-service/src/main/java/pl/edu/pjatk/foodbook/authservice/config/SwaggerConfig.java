package pl.edu.pjatk.foodbook.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Auth API",
        description = "API for user authentication & authorization",
        version = "0.0.1"
    ),
    servers = @Server(url = "http://localhost:9000"))
public class SwaggerConfig {

}
