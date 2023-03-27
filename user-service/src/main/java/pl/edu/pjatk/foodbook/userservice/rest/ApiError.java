package pl.edu.pjatk.foodbook.userservice.rest;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@Data
@Builder
public class ApiError {
    private HttpStatusCode status;
    private String message;
    private List<String> errors;
}
