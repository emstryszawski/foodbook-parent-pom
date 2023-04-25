package pl.edu.pjatk.foodbook.foodbookservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FoodbookServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodbookServiceApplication.class, args);
    }
}
