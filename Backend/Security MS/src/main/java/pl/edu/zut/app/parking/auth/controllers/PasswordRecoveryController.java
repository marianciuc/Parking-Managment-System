package pl.edu.zut.app.parking.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.auth.dto.req.PasswordRecoveryRequest;
import pl.edu.zut.app.parking.auth.services.PasswordRecoveryService;

@RestController
@RequestMapping("/api/v1/security/password-recovery")
@RequiredArgsConstructor
public class PasswordRecoveryController {

    private final PasswordRecoveryService passwordRecoveryService;

    @Operation(
            summary = "Send password recovery email",
            description = "Sends an email with a password recovery code to the specified email address.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully sent the password recovery email"),
                    @ApiResponse(responseCode = "404", description = "Invalid email address"),
            }
    )
    @PostMapping
    public void recoverPassword(@Parameter(
            name = "email",
            description = "The email address of the user requesting password recovery.",
            required = true,
            in = ParameterIn.PATH
    ) @RequestParam String email) {
        passwordRecoveryService.recoveryPassword(email);
    }

    @Operation(
            summary = "Verify password recovery code",
            description = "Verify password recovery code and associate new password to user if the code is correct.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password successfully reset for the user."),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired recovery code."),
                    @ApiResponse(responseCode = "404", description = "User not found."),
            }
    )
    @PostMapping("/verify")
    public void verifyPasswordRecovery(@RequestBody PasswordRecoveryRequest request) {
        passwordRecoveryService.resetPassword(request.code(), request.password());
    }
}
