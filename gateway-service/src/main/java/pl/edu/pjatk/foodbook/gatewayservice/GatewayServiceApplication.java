package pl.edu.pjatk.foodbook.gatewayservice;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Autowired
    RouteDefinitionLocator locator;

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
