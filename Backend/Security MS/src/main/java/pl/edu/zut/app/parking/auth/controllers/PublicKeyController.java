package pl.edu.zut.app.parking.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.zut.app.parking.auth.services.JWTService;

@RestController
@RequestMapping("/api/v1/security/jwt")
@RequiredArgsConstructor
public class PublicKeyController {

    private final JWTService jwtService;

    @Operation(
            summary = "Get JWT Public Key",
            description = "Retrieves the public key used for cryptographic operations such as verifying JSON Web Tokens (JWTs).",
            tags = {"JWT", "Security"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the public key", content = {@Content(mediaType = "text/plain")}),
            @ApiResponse(responseCode = "500", description = "Server error while retrieving the public key", content = @Content)
    })
    @GetMapping(value = "/public-key", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getPublicKey() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(jwtService.getPublicKey());
    }
}