package pl.edu.zut.app.parking.payments_ms.controllers;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.payments_ms.handlers.StripeEventHandler;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {

  @Value("${stripe.webhook.secret}")
  private String endpointSecret;

  private final StripeEventHandler stripeEventHandler;

  @PostMapping
  public ResponseEntity<String> handleStripeWebhook(
      @RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
    Event event;

    log.info("Received Stripe event payload");
    try {
      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (SignatureVerificationException e) {
      log.error("Invalid signature for Stripe webhook");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
    }

    log.info("Received Stripe event: type={}, id={}", event.getType(), event.getId());

    switch (event.getType()) {
      case "payment_intent.succeeded":
        stripeEventHandler.handlePaymentSucceeded(event);
        break;
      case "payment_intent.payment_failed":
        stripeEventHandler.handlePaymentFailed(event);
        break;
      case "payout.failed":
        stripeEventHandler.handlePayoutFailed(event);
        break;
      case "payout.paid":
        stripeEventHandler.handlePayoutPaid(event);
        break;
      default:
        log.warn("Unhandled Stripe event type: {}", event.getType());
        break;
    }
    return ResponseEntity.ok("Webhook received");
  }
}
