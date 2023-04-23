package pl.edu.pjatk.foodbook.gatewayservice.feign;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class GatewayFeignConfig {

    @Bean
    public HttpMessageConverters feignHttpMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        return new HttpMessageConverters(true, converters);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestInterceptor -> requestInterceptor.header("X-Gateway", "localhost:8080");
    }

    @Bean
    public FeignBuilderCustomizer feignBuilderCustomizer(RequestInterceptor requestInterceptor) {
        return builder -> builder.requestInterceptor(requestInterceptor);
    }
}
