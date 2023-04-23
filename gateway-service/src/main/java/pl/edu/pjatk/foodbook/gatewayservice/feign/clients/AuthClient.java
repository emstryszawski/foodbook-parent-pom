package pl.edu.pjatk.foodbook.gatewayservice.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import pl.edu.pjatk.foodbook.authservice.swagger.auth.api.AuthControllerApi;
import pl.edu.pjatk.foodbook.gatewayservice.feign.FeignHttpMessageConvertersConfig;

@FeignClient(
    name = "AuthApi",
    url = "http://localhost:9000",
    configuration = FeignHttpMessageConvertersConfig.class
)
public interface AuthClient extends AuthControllerApi {

}
