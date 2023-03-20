package pl.edu.pjatk.foodbook.userservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(servers = @Server(url = "http://localhost:8010"))
public class SwaggerConfig {
}
