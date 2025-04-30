package pl.edu.zut.app.parking.payments_ms.services;

import java.util.UUID;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.dto.external.SessionPaymentOrderDto;
import pl.edu.zut.app.parking.payments_ms.exceptions.SessionPaymentException;

/** Interface defining operations for managing payments related to parking sessions. */
public interface SessionPaymentsService {

  /**
   * Creates a payment intent for the specified parking session.
   *
   * @param sessionId The unique identifier of the parking session for which the payment intent is
   *     created.
   * @param req An object containing the details of the session payment, including the amount and
   *     currency.
   * @return A string representing the payment intent identifier.
   * @throws SessionPaymentException if an error occurs while creating the payment intent.
   */
  String  createSessionPaymentIntent(UUID sessionId, SessionPaymentOrderDto req)
      throws SessionPaymentException;

  /**
   * Processes the payment for a specific parking session by using the provided payment order
   * details. This method handles the transaction and updates the session's payment status
   * accordingly.
   *
   * @param sessionId The unique identifier of the parking session for which the payment is being
   *     processed.
   * @param orderDto An object containing the payment order details, including the amount and
   *     currency.
   * @param carOwnerId The unique identifier of the car owner associated with the payment.
   * @return A {@link TransactionDto} object that represents the details of the processed
   *     transaction.
   * @throws SessionPaymentException if an error occurs during payment processing, such as
   *     insufficient funds or issues with the session status.
   */
  TransactionDto processSessionPayment(
      UUID sessionId, SessionPaymentOrderDto orderDto, UUID carOwnerId)
      throws SessionPaymentException;
}
