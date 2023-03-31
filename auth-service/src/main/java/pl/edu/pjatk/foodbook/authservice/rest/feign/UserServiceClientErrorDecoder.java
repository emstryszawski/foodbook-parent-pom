package pl.edu.pjatk.foodbook.authservice.rest.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import pl.edu.pjatk.foodbook.authservice.rest.ApiError;
import pl.edu.pjatk.foodbook.authservice.rest.exception.UserServiceClientException;

import java.io.BufferedReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.stream.Stream;

public class UserServiceClientErrorDecoder implements ErrorDecoder {

    private final ModelMapper modelMapper = new ModelMapper();

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        Reader reader = response.body().asReader(Charset.defaultCharset());
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();
        Stream.of(bufferedReader.readLine()).forEach(stringBuilder::append);
        ApiError error = objectMapper.readValue(stringBuilder.toString(), ApiError.class);
        String errorAsString = objectMapper.writeValueAsString(error);
        return new UserServiceClientException(errorAsString);
    }
}
