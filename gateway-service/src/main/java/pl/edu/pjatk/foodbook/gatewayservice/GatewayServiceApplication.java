package pl.edu.pjatk.foodbook.gatewayservice;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import pl.edu.pjatk.foodbook.gatewayservice.feign.clients.AuthClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(
    clients = {
        AuthClient.class
    }
)
public class GatewayServiceApplication {

    final RouteDefinitionLocator locator;

    public GatewayServiceApplication(RouteDefinitionLocator locator) {
        this.locator = locator;
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Bean
    public List<GroupedOpenApi> apis() {
        List<GroupedOpenApi> groups = new ArrayList<>();
        Optional.ofNullable(locator.getRouteDefinitions()
            .collectList()
            .block()
        ).ifPresent(definitions -> definitions.stream()
            .filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
            .forEach(routeDefinition -> {
                String name = routeDefinition.getId().replaceAll("-service", "");
                groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());
            })
        );

        return groups;
    }
}
