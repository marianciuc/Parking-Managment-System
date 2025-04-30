package pl.edu.zut.app.parking.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.auth.dto.common.AuthenticationTokens;
import pl.edu.zut.app.parking.auth.services.OAuth2Service;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class OAuth2CallbackController {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/google/callback")
    public ResponseEntity<AuthenticationTokens> handleGoogleCallback(
            @RequestParam Map<String, String> params
    ) {
        log.info("Received request with params: " + params);
        String authorizationCode = params.get("code");
        if (authorizationCode == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(oAuth2Service.handleGoogleAuthenticatorResponse(authorizationCode));
    }
}
