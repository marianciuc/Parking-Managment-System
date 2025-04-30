package pl.edu.zut.app.parking.payments_ms.services.impl;

import com.stripe.exception.StripeException;
import java.util.UUID;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import pl.edu.zut.app.parking.payments_ms.clients.ParkingClient;
import pl.edu.zut.app.parking.payments_ms.clients.SessionsClient;
import pl.edu.zut.app.parking.payments_ms.dto.common.AccountBalanceDto;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.dto.external.SessionDto;
import pl.edu.zut.app.parking.payments_ms.dto.external.SessionPaymentOrderDto;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionType;
import pl.edu.zut.app.parking.payments_ms.exceptions.InvalidSessionStatus;
import pl.edu.zut.app.parking.payments_ms.exceptions.ParkingNotFoundException;
import pl.edu.zut.app.parking.payments_ms.exceptions.SessionPaymentException;
import pl.edu.zut.app.parking.payments_ms.integrations.StripeIntegrationService;
import pl.edu.zut.app.parking.payments_ms.services.AccountBalanceService;
import pl.edu.zut.app.parking.payments_ms.services.SessionPaymentsService;
import pl.edu.zut.app.parking.payments_ms.services.TransactionService;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionPaymentsServiceImpl implements SessionPaymentsService {

  private final TransactionService transactionService;
  private final ParkingClient parkingClient;
  private final SessionsClient sessionsClient;
  private final AccountBalanceService accountBalanceService;
  private final StripeIntegrationService stripeIntegrationService;

  @Override
  public String createSessionPaymentIntent(UUID sessionId, SessionPaymentOrderDto req) throws SessionPaymentException {
    log.info("Creating session payment intent for session id: {}", sessionId);
    try {
      UUID parkingId = validateSessionAndGetParkingId(sessionId);
      UUID parkingAccountId = getParkingBusinessAccount(parkingId).id();
      TransactionDto transactionDto =
          transactionService.createSessionPaymentTransaction(
              TransactionType.SESSION_EXTERNAL_PAYMENT,
              sessionId,
              req.amount(),
              req.currency(),
              parkingId,
              parkingAccountId,
              null);
      return stripeIntegrationService.createPaymentIntent(
          req.amount(), req.currency(), transactionDto.id());
    } catch (ParkingNotFoundException | StripeException e) {
      log.error("Error during session payment intent creation: {}", e.getMessage(), e);
      throw new SessionPaymentException("Could not create payment intent", e);
    }
  }

  @Override
  public TransactionDto processSessionPayment(
      UUID sessionId, SessionPaymentOrderDto orderDto, UUID carOwnerId) throws SessionPaymentException {
    log.info("Processing session payment for session id: {}", sessionId);
    try {
      UUID parkingId = validateSessionAndGetParkingId(sessionId);
      UUID carOwnerAccountId = accountBalanceService.getOwnerAccountBalance(carOwnerId, false).id();
      UUID parkingAccountId = getParkingBusinessAccount(parkingId).id();

      return transactionService.createSessionPaymentTransaction(
          TransactionType.SESSIONS_PAYMENT,
          sessionId,
          orderDto.amount(),
          orderDto.currency(),
          parkingId,
          parkingAccountId,
          carOwnerAccountId);
    } catch (ParkingNotFoundException e) {
      throw new SessionPaymentException("Cold not proccess payment", e);
    }
  }

  private AccountBalanceDto getParkingBusinessAccount(UUID parkingId)
      throws ParkingNotFoundException {
    log.info("Getting parking business account for parking id: {}", parkingId);
    try {
      ResponseEntity<UUID> parkingDtoResponseEntity = parkingClient.getParkingOwenrId(parkingId);
      log.info("parking: {}", parkingDtoResponseEntity.getBody());
      if (parkingDtoResponseEntity.getStatusCode().is2xxSuccessful()
          && parkingDtoResponseEntity.getBody() != null) {
        return accountBalanceService.getOwnerAccountBalance(
            parkingDtoResponseEntity.getBody(), false);
      } else {
        throw new ParkingNotFoundException("Parking not found");
      }
    } catch (HttpClientErrorException | FeignException e) {
      log.error("Failed to retrieve parking business account: {}", e.getMessage(), e);
      throw new ParkingNotFoundException("Could not retrieve parking business account", e);
    }
  }

  private UUID validateSessionAndGetParkingId(UUID sessionId)
      throws ParkingNotFoundException, InvalidSessionStatus {
    ResponseEntity<SessionDto> response = sessionsClient.getSession(sessionId);

    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
      throw new ParkingNotFoundException("Invalid response from SessionsClient for sessionId");
    }

    SessionDto session = response.getBody();
    if ("PREPARE_END".equals(session.status()) || "ACTIVE".equals(session.status())) {
      return session.parkingId();
    }
    throw new InvalidSessionStatus("Invalid session status: " + session.status());
  }
}
