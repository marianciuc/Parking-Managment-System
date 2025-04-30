package pl.edu.zut.parking.app.gateway.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.zut.parking.app.gateway.services.JwtService;
import pl.edu.zut.parking.app.gateway.services.TokenBlackListService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JwtService jwtService;
    private final TokenBlackListService tokenBlackListService;
    private static final int TOKEN_EXPIRATION_TIME = 36000;

    @PostMapping("/api/v1/security/block-token")
    public String blockToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        Optional<String> token = jwtService.extractBearerToken(authHeader);
        if (token.isEmpty()) {
            return "Authorization header is missing.";

        }
        tokenBlackListService.blockToken(String.valueOf(token), TOKEN_EXPIRATION_TIME);
        return "Token has been successfully blocked.";
    }
}
