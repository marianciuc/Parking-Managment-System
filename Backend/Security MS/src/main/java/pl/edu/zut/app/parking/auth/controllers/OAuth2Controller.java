package pl.edu.zut.app.parking.auth.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.auth.services.OAuth2Service;

@Slf4j
@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

  private final OAuth2Service oAuth2Service;

  @GetMapping("/google/url")
  public ResponseEntity<String> getGoogleAuthUrl() {
    log.info("Get Google Auth URL");
    return ResponseEntity.ok(oAuth2Service.getGoogleAuthenticatorUrl());
  }

  @GetMapping("/apple/url")
  public ResponseEntity<String> getAppleAuthUrl() {
    return ResponseEntity.ok(oAuth2Service.getAppleAuthenticatorUrl());
  }
}
