package pl.edu.pjatk.foodbook.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import pl.edu.pjatk.foodbook.authservice.swagger.user.api.UserControllerApiClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(
    clients = UserControllerApiClient.class
)
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
