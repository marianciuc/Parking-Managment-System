package pl.edu.zut.app.parking.payments_ms.handlers;

import com.stripe.model.Event;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.payments_ms.services.TransactionService;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripeEventHandler {

  private final TransactionService transactionsService;

  /**
   * Handles the "payment succeeded" event triggered by Stripe, extracting relevant information
   * about the successful payment such as the transaction ID, amount, and currency. The transaction
   * status is then updated to success in the associated transaction service.
   *
   * @param event the Stripe event containing details of the successful payment
   */
  public void handlePaymentSucceeded(Event event) {
    try {
      log.info("Payment succeeded event received, {}", event);
      var paymentIntent = (com.stripe.model.PaymentIntent) event.getData().getObject();

      UUID transactionId = UUID.fromString(paymentIntent.getDescription());
      Long amount = paymentIntent.getAmount();
      String currency = paymentIntent.getCurrency();

      log.info(
          "Payment succeeded: Transaction ID = {}, Amount = {} {}",
          transactionId,
          amount,
          currency);
      transactionsService.updateTransactionStatus(transactionId, true, null);
    } catch (Exception e) {
      log.error("Error handling payment succeeded event", e);
    }
  }

  /**
   * Handles the "payment failed" event triggered by Stripe, extracting relevant information about
   * the failed payment such as the transaction ID and failure message. The transaction status is
   * then updated to failed in the associated transaction service.
   *
   * @param event the Stripe event containing details of the failed payment
   */
  public void handlePaymentFailed(Event event) {
    var paymentIntent =
        (com.stripe.model.PaymentIntent)
            event.getDataObjectDeserializer().getObject().orElseThrow();

    UUID transactionId = UUID.fromString(paymentIntent.getDescription());
    String failureMessage =
        paymentIntent.getLastPaymentError() != null
            ? paymentIntent.getLastPaymentError().getMessage()
            : "Unknown error";

    log.info("Payment failed: Transaction ID = {}, Error = {}", transactionId, failureMessage);
    transactionsService.updateTransactionStatus(transactionId, false, failureMessage);
  }

  /**
   * Handles the "payout failed" event triggered by Stripe. The method retrieves the payout details
   * from the event, logs information about the failed payout, and updates the transaction status in
   * the associated transaction service to reflect the failure along with the failure message.
   *
   * @param event the Stripe event containing details of the failed payout
   */
  public void handlePayoutFailed(Event event) {
    var payout =
        (com.stripe.model.Payout) event.getDataObjectDeserializer().getObject().orElseThrow();
    log.info(
        "Payout failed: Payout ID = {}, Error = {}", payout.getId(), payout.getFailureMessage());

    transactionsService.updateTransactionStatus(
        UUID.fromString(payout.getId()), false, payout.getFailureMessage());
  }

  /**
   * Handles the "payout paid" event triggered by Stripe. This method retrieves the payout details
   * from the event, logs the payout ID of the successfully completed payout, and updates the
   * transaction status in the associated transaction service to reflect the success.
   *
   * @param event the Stripe event containing details of the completed payout
   */
  public void handlePayoutPaid(Event event) {
    var payout =
        (com.stripe.model.Payout) event.getDataObjectDeserializer().getObject().orElseThrow();
    log.info("Payout paid: Payout ID = {}", payout.getId());

    UUID transactionId = UUID.fromString(payout.getDescription());

    transactionsService.updateTransactionStatus(transactionId, true, null);
  }
}
