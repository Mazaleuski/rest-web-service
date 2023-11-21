package com.example.restwebservice.controllers;

import com.example.restwebservice.dto.UserDto;
import com.example.restwebservice.exceptions.AuthorizationException;
import com.example.restwebservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
@Tag(name = "user", description = "User Endpoints")

public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Find all users",
            description = "Find all registered users in shop",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "All users were found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Users not found - server error"
            )
    })

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(
            summary = "Find certain user",
            description = "Find certain existed user in shop by id",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was found by id",
                    content = @Content(schema = @Schema(contentSchema = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User not found - forbidden operation",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@Parameter(required = true, description = "User id") @PathVariable @Positive int id) {
        return Optional.ofNullable(userService.getUserById(id))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Create user",
            description = "Create new user",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User was created",
                    content = @Content(schema = @Schema(contentSchema = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not created - server error"
            )
    })

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login user",
            description = "Authorization registered users in shop",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User was login",
                    content = @Content(schema = @Schema(contentSchema = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User was not login",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody @Valid UserDto userDto) throws AuthorizationException {
        return new ResponseEntity<>(userService.loginUser(userDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Update user",
            description = "Update existed user",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was updated",
                    content = @Content(schema = @Schema(contentSchema = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not updated - server error",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user",
            description = "Delete existed user",
            tags = {"user"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "User not deleted - server error"
            )
    })

    @DeleteMapping("/{id}")
    public void deleteUser(@Parameter(required = true, description = "User id") @PathVariable @Positive int id) {
        userService.deleteUser(id);
    }
}
