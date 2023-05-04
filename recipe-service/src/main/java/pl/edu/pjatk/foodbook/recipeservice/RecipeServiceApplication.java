package pl.edu.pjatk.foodbook.recipeservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.edu.pjatk.foodbook.recipeservice.swagger.product.api.ProductControllerApiClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableJpaRepositories
@EnableFeignClients(
    clients = ProductControllerApiClient.class
)
public class RecipeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeServiceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
