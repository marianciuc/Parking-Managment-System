package pl.edu.zut.app.parking.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.auth.dto.common.AuthenticationTokens;
import pl.edu.zut.app.parking.auth.dto.req.UserRegistrationRequest;
import pl.edu.zut.app.parking.auth.enums.UserType;
import pl.edu.zut.app.parking.auth.factories.UserRegistrationServiceFactory;
import pl.edu.zut.app.parking.auth.services.JWTService;
import pl.edu.zut.app.parking.auth.services.UserRegistrationService;

/**
 * Controller class for handling authentication-related requests.
 */
@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class AuthController {

    private final UserRegistrationServiceFactory userRegistrationServiceFactory;
    private final JWTService jwtService;


    @Operation(
            summary = "Register a new user",
            description = "Register a new user of a specific type (e.g., DRIVER, ADMIN, etc.) with the provided user registration details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully registered, and authentication tokens are issued",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationTokens.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body or user type",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request",
                            content = @Content
                    )
            }
    )
    @PostMapping("/register/{userType}")
    public ResponseEntity<AuthenticationTokens> register(
            @Valid @RequestBody UserRegistrationRequest request,
            @Parameter(
                    name = "userType",
                    description = "The type of user to be registered (e.g., DRIVER, ADMIN, etc.)",
                    required = true,
                    in = ParameterIn.PATH
            )
            @PathVariable UserType userType
    ) {
        UserRegistrationService userRegistrationService =
                userRegistrationServiceFactory.getRegistrationService(userType);
        return ResponseEntity.ok(userRegistrationService.registerUser(request));
    }

    @Operation(
            summary = "Refresh JWT tokens",
            description = "Refresh the authentication and refresh tokens using the user's existing refresh token. Requires 'REFRESH_TOKEN' role.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication tokens successfully refreshed",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationTokens.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "The user does not have the required role or access is forbidden",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized request",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/jwt/refresh")
    @PreAuthorize("hasRole('REFRESH_TOKEN')")
    public ResponseEntity<AuthenticationTokens> refreshToken(Authentication authentication) {
        return ResponseEntity.ok(jwtService.refreshAuthenticationTokens(authentication));
    }
}
