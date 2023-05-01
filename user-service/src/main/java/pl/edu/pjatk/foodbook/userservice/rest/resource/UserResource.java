package pl.edu.pjatk.foodbook.userservice.rest.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.foodbook.userservice.rest.ApiError;
import pl.edu.pjatk.foodbook.userservice.rest.dto.request.CreateUserInput;
import pl.edu.pjatk.foodbook.userservice.rest.dto.response.UserRepresentation;

@RequestMapping(
    name = "User API",
    path = "/api/v1")
@Tag(name = "User", description = "User related resource")
public interface UserResource {

    @Operation(
        summary = "Get user",
        description = "Get the user by its username")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User was found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserRepresentation.class))),
        @ApiResponse(
            responseCode = "404", description = "User was not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class))
        )
    })
    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserRepresentation> getUserByUsername(
        @Parameter(name = "username", required = true) @PathVariable String username);

    @Operation(
        summary = "Create user",
        description = "Create a User with given input")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "User successfully created",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserRepresentation.class))),
        @ApiResponse(
            responseCode = "400", description = "Bad request. Invalid input",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiError.class))
        )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserRepresentation> saveUser(@RequestBody @Valid CreateUserInput user);
}
