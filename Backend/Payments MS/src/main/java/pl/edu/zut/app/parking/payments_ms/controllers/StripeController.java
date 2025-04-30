package pl.edu.zut.app.parking.payments_ms.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The StripeController class provides endpoints for interacting with
 * Stripe payment functionalities related to client-side operations.
 * This controller focuses on exposing the public key used for Stripe
 * API interactions.
 */
@RestController
@RequestMapping("/api/v1/stripe")
public class StripeController {

  @Value("${stripe.api.public-key}")
  private String stripePublicKey;

  /**
   * Retrieves the public key for Stripe payment processing.
   * This key is used on the client side to allow interaction with the Stripe API.
   *
   * @return ResponseEntity containing the Stripe public key as a String.
   */
  @GetMapping("/public-key")
  ResponseEntity<String> getStripePublicKey() {
    return ResponseEntity.ok(stripePublicKey);
  }
}
