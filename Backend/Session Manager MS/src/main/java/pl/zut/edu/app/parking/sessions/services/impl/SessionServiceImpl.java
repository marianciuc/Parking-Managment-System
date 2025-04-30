package pl.zut.edu.app.parking.sessions.services.impl;

import feign.FeignException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;
import pl.zut.edu.app.parking.sessions.clients.ParkingClient;
import pl.zut.edu.app.parking.sessions.clients.PaymentClient;
import pl.zut.edu.app.parking.sessions.clients.VehicleClient;
import pl.zut.edu.app.parking.sessions.dto.*;
import pl.zut.edu.app.parking.sessions.dto.filters.SessionFilter;
import pl.zut.edu.app.parking.sessions.dto.response.SessionListDTO;
import pl.zut.edu.app.parking.sessions.entities.Session;
import pl.zut.edu.app.parking.sessions.enums.VehicleAccessList;
import pl.zut.edu.app.parking.sessions.exceptions.*;
import pl.zut.edu.app.parking.sessions.kafka.SessionEndedMessageProducer;
import pl.zut.edu.app.parking.sessions.kafka.SessionStartedMessageProducer;
import pl.zut.edu.app.parking.sessions.kafka.messages.SessionPaymentMessage;
import pl.zut.edu.app.parking.sessions.repositories.SessionsRepository;
import pl.zut.edu.app.parking.sessions.services.SessionService;
import pl.zut.edu.app.parking.sessions.specifications.SessionSpecifications;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

  private final VehicleClient vehicleClient;
  private final ParkingClient parkingClient;
  private final SessionsRepository sessionsRepository;
  private final PaymentClient paymentClient;

  private static final String DEFAULT_VEHICLE_ACCESS_LIST = "Regular";
  private static final String SESSION_NOT_FOUND_MESSAGE = "Session not found";
  private static final String SESSION_INVALID_STATUS_MESSAGE = "Session is not in %s status";
  private static final String SESSION_ALREADY_FINISHED_MESSAGE =
      "Session is already finished by administrator. Good luck!";
  private static final String SESSION_IS_NOT_ACTIVE_MESSAGE = "Session is not in ACTIVE status";
  private static final String SESSION_IS_NOT_PREPARED_MESSAGE = "Session is not in PREPARE status";
  private static final String SESSION_IS_NOT_PREPARED_END_MESSAGE =
      "Session is not in PREPARE_END status";
  private static final String DEFAULT_TARIFF_NAME = "N/A";
  private static final String VEHICLE_IS_NOT_ALLOWED_MESSAGE =
      "Vehicle is not allowed in this parking";
  private final SessionStartedMessageProducer sessionStartedMessageProducer;
  private final SessionEndedMessageProducer sessionEndedMessageProducer;

  private VehicleDto findOrCreateVehicle(String plate) {
    try {
      ResponseEntity<VehicleDto> response = vehicleClient.findByPlate(plate);
      return response.getBody();
    } catch (Exception exception) {
      log.error("Error fetching vehicle by plate number");
      return createVehicleOrThrow(plate);
    }
  }

  private VehicleDto createVehicleOrThrow(String plate) {
    ResponseEntity<VehicleDto> createdResponse =
        vehicleClient.create(VehicleDto.buildWithPlateNumber(plate), false);
    if (createdResponse.getStatusCode().isError() || createdResponse.getBody() == null) {
      throw new VehicleCreationException("Error creating vehicle.");
    }
    return createdResponse.getBody();
  }

  private Session findSessionOrThrow(UUID sessionId) {
    return sessionsRepository
        .findById(sessionId)
        .orElseThrow(() -> new SessionNotFoundException(SESSION_NOT_FOUND_MESSAGE));
  }

  private void closeAnotherSessionIfVehicleAlreadyHaveOne(UUID vehicleId) {
    Stream<Session> stream =
        sessionsRepository
            .findByVehicleIdAndStatusIn(
                vehicleId,
                Arrays.asList(Session.SessionStatus.ACTIVE, Session.SessionStatus.PREPARE_END))
            .stream();
    stream.forEach(
        session -> {
          if (session != null) {
            log.info(
                "Closing another session for vehicle id: {}, session id: {}",
                vehicleId,
                session.getId());
            closeInactiveSession(session.getId());
          }
        });
  }

  @Override
  public UUID prepareSession(String plate) {
    log.info("Preparing session for plate {}", plate);

    if (plate == null || plate.length() < 3) {
      throw new InvalidVehiclePlateException(plate);
    }

    UUID parkingId = SecurityContextUtil.extractParkingIdFromSecurityContext();
    VehicleDto vehicleDto = findOrCreateVehicle(plate);

    closeAnotherSessionIfVehicleAlreadyHaveOne(vehicleDto.id());
    log.info("Parking ID: {}, Vehicle ID: {}", parkingId, vehicleDto.id());

    String accessList = validateVehiclePermissions(parkingId, vehicleDto.id(), plate);
    log.info("Vehicle access list: {}", accessList);

    Session session =
        Session.builder()
            .parkingId(parkingId)
            .ownerId(
                vehicleDto.hasOwner().equals(Boolean.TRUE)
                    ? vehicleDto.ownership().ownerId()
                    : null)
            .vehicleId(vehicleDto.id())
            .plateNumber(plate)
            .paidUntil(null)
            .endTime(null)
            .startTime(null)
            .tariffId(null)
            .tariffName(DEFAULT_TARIFF_NAME)
            .vehicleAccessList(accessList)
            .status(Session.SessionStatus.PREPARE)
            .build();

    TariffDto tariffDto = fetchTariff(session, 0, fetchParkingCurrency(parkingId));

    return updateSessionTariff(session, tariffDto).getId();
  }

  private String validateVehiclePermissions(UUID parkingId, UUID vehicleId, String plate) {
    ResponseEntity<Boolean> permissionResponse =
        parkingClient.isVehicleAllowed(parkingId, vehicleId);
    if (!isResponseValid(permissionResponse)) {
      log.error(
          "Vehicle is not allowed in this parking. Plate: {}, Vehicle ID: {}, res: {}",
          plate,
          vehicleId,
          permissionResponse);
      throw new VehicleDeniedException(VEHICLE_IS_NOT_ALLOWED_MESSAGE);
    }

    Boolean isVehicleInWhitelist = false;
    try {
      ResponseEntity<Boolean> whitelistCheckResponse =
          parkingClient.isInWhitelist(parkingId, vehicleId);
      isVehicleInWhitelist = whitelistCheckResponse.getBody();
    } catch (FeignException exception) {
      log.error(
          "Error checking whitelist for vehicle: Plate: {}, Vehicle ID: {}", plate, vehicleId);
    }

    return Boolean.TRUE.equals(isVehicleInWhitelist)
        ? VehicleAccessList.WHITELIST.name()
        : VehicleAccessList.REGULAR.name();
  }

  private boolean isResponseValid(ResponseEntity<Boolean> response) {
    return response != null
        && response.getStatusCode() == HttpStatus.OK
        && Boolean.TRUE.equals(response.getBody());
  }

  @Override
  public void startSession(UUID sessionId) {
    Session session = findSessionOrThrow(sessionId);

    validateSessionStatus(session, Session.SessionStatus.PREPARE);

    session.setStatus(Session.SessionStatus.ACTIVE);
    session.setStartTime(LocalDateTime.now());

    sessionStartedMessageProducer.produce(
        session.getPlateNumber(),
        session.getParkingId(),
        session.getOwnerId(),
        session.getStartTime());
    sessionsRepository.save(session);
  }

  private void closeInactiveSession(UUID sessionId) {
    Session session = findSessionOrThrow(sessionId);

    session.setStatus(Session.SessionStatus.STOPPED_BY_SYSTEM);
    session.setEndTime(LocalDateTime.now());

    sessionsRepository.save(session);
  }

  @Override
  public void cancelSession(UUID sessionId) {
    Session session = findSessionOrThrow(sessionId);

    validateSessionStatus(session, Session.SessionStatus.PREPARE);

    session.setStatus(Session.SessionStatus.CANCELLED);
    session.setEndTime(LocalDateTime.now());

    sessionEndedMessageProducer.produce(
            session.getPlateNumber(),
            session.getParkingId(),
            session.getOwnerId(),
            session.getStartTime(),
            session.getEndTime());
    sessionsRepository.save(session);
  }

  @Override
  public void endSession(String plate) {
    Session session = findActiveSessionEntityByPlate(plate);

    switch (session.getStatus()) {
      case PREPARE_END -> {
        if (!validatePayment(session)) {
          throw new SessionInvalidStatusException("Session has no payment");
        }
        session.setStatus(Session.SessionStatus.FINISHED);
        session.setEndTime(LocalDateTime.now());
        sessionsRepository.save(session);

        sessionEndedMessageProducer.produce(
                session.getPlateNumber(),
                session.getParkingId(),
                session.getOwnerId(),
                session.getStartTime(),
                session.getEndTime());
      }
      case STOPPED_BY_OWNER, STOPPED_BY_SYSTEM -> {
        session.setStatus(Session.SessionStatus.FINISHED);
        session.setEndTime(LocalDateTime.now());
        sessionsRepository.save(session);
      }
      default ->
          throw new SessionInvalidStatusException(
              "Session has invalid status: " + session.getStatus());
    }
  }

  @Override
  public SessionDto findSession(UUID sessionId) {
    Session session = findSessionOrThrow(sessionId);
    return convertToSessionDto(session);
  }

  private Session updateSessionTariff(Session session, TariffDto tariffDto) {
    session.setTariffId(tariffDto.id());
    session.setTariffName(tariffDto.name());
    return sessionsRepository.save(session);
  }

  private TariffDto getOrUpdateTariff(Session session, long durationInMinutes) {
    TariffDto tariffDto =
        fetchTariff(session, durationInMinutes, fetchParkingCurrency(session.getParkingId()));
    if (!Objects.equals(tariffDto.id(), session.getTariffId())) {
      updateSessionTariff(session, tariffDto);
    }
    return tariffDto;
  }

  private SessionDto convertToSessionDto(Session session) {
    String currency = "USD";
    long durationInMinutes =
        Duration.between(
                session.getStartTime(),
                session.getEndTime() != null ? session.getEndTime() : LocalDateTime.now())
            .toMinutes();

    TariffDto tariffDto = getOrUpdateTariff(session, durationInMinutes);

    try {
      currency = fetchParkingCurrency(session.getParkingId());
    } catch (Exception exception) {
      log.error("Error fetching parking currency");
    }

    return SessionDto.from(
        session,
        currency,
        fetchUserPaidAmount(session.getId(), currency),
        calculatePayment(session.getId()),
        calculateTotalAmount(tariffDto, durationInMinutes),
        durationInMinutes);
  }

  @Override
  public SessionDto findActiveSessionByPlate(String plate) {
    Session session = findActiveSessionEntityByPlate(plate);
    return convertToSessionDto(session);
  }

  private Session findActiveSessionEntityByPlate(String plate) {
    return sessionsRepository
        .findByPlateNumberAndStatusIn(
            plate, Set.of(Session.SessionStatus.ACTIVE, Session.SessionStatus.PREPARE_END))
        .orElseThrow(() -> new SessionNotFoundException("Session not found"));
  }

  @Override
  public PrepareEndSessionDto prepareEndSession(String plate) {
    Session session = findActiveSessionEntityByPlate(plate);

    switch (session.getStatus()) {
      case ACTIVE:
        PrepareEndSessionDto prepareEndSessionDto = tryToPayForSession(session);
        log.info("Prepare end session result: {}", prepareEndSessionDto);
        if (prepareEndSessionDto != null && prepareEndSessionDto.isPaidSuccessfully()) {
          session.setStatus(Session.SessionStatus.PREPARE_END);
          sessionsRepository.save(session);
        }
        return prepareEndSessionDto;
      case STOPPED_BY_OWNER:
        throw new SessionAlreadyFinishedException(SESSION_ALREADY_FINISHED_MESSAGE);
      default:
        throw new SessionInvalidStatusException(SESSION_IS_NOT_ACTIVE_MESSAGE);
    }
  }

  @Override
  public void cancelPreparedEndSession(String plate) {
    Session session =
        sessionsRepository
            .findByPlateNumber(plate)
            .orElseThrow(() -> new SessionNotFoundException("Session not found"));

    if (session.getStatus() == Session.SessionStatus.PREPARE_END) {
      session.setStatus(Session.SessionStatus.ACTIVE);
      sessionsRepository.save(session);
    } else {
      throw new SessionInvalidStatusException("Session is not in PREPARE_END status");
    }
  }

  @Override
  public Page<SessionListDTO> findSessions(
      Integer page, Integer size, String sort, String direction, SessionFilter filter) {
    log.info("Filtering sessions with filter: {}", filter);
    return sessionsRepository
        .findByParkingId(
            filter.parkingId(),
            PageRequest.of(
                page, size, Sort.by(SessionSpecifications.getSortDirection(direction), sort)))
        .map(SessionListDTO::fromEntity);
  }

  @Override
  public void addPayment(SessionPaymentMessage message) {

    Session session =
        sessionsRepository
            .findById(message.sessionId())
            .orElseThrow(() -> new SessionNotFoundException("Session not found"));
    boolean isNewPayment = session.getPaidUntil() == null;
    log.info(
        "Payment received for session id: {}, is new payment: {}", session.getId(), isNewPayment);
    TariffDto tariffDto =
        fetchTariff(
            session,
            Duration.between(session.getStartTime(), LocalDateTime.now()).toMinutes(),
            message.code());
    BigDecimal paidMinutes =
        message
            .amount()
            .multiply(BigDecimal.valueOf(60))
            .divide(tariffDto.price(), 2, RoundingMode.FLOOR);
    long paidTime = paidMinutes.longValue();

    LocalDateTime baseTime = isNewPayment ? session.getStartTime() : session.getPaidUntil();
    session.setPaidUntil(baseTime.plusMinutes(paidTime));

    sessionsRepository.save(session);

    log.info(
        "Payment received for session id: {}, amount: {}, is new payment: {}, current paidUntil: {}, calculated paid time: {}",
        session.getId(),
        message.amount(),
        isNewPayment,
        session.getPaidUntil(),
        paidTime);
  }

  private PrepareEndSessionDto tryToPayForSession(Session session) {
    VehicleDto vehicleDto = findOrCreateVehicle(session.getPlateNumber());
    Payment payment = calculatePayment(session.getId());
    log.info("Payment for session id: {}, amount: {}", session.getId(), payment.amount());

    if (payment.remainingTime() == 0) {
      log.info("Session is paid successfully. Session id: {}", session.getId());
      return new PrepareEndSessionDto(true, payment, session.getId());
    }

    if (vehicleDto.hasOwner().equals(Boolean.FALSE)) {
      return notifyManualPayment(payment, session.getId());
    } else if (payment.remainingTime() > 15) {
      ResponseEntity<Boolean> paySession = paymentClient.paySession(session.getId(), payment);

      if (paySession.getStatusCode() == HttpStatus.OK && paySession.getBody() != null) {
        if (Boolean.TRUE.equals(paySession.getBody())) {
          session.setPaidUntil(LocalDateTime.now());
          return new PrepareEndSessionDto(true, payment, session.getId());
        } else {
          return notifyManualPayment(payment, session.getId());
        }
      }
    } else {
      return new PrepareEndSessionDto(true, payment, session.getId());
    }
    return null;
  }

  private PrepareEndSessionDto notifyManualPayment(Payment payment, UUID sessionId) {
    return new PrepareEndSessionDto(false, payment, sessionId);
  }

  @Override
  public Payment calculatePayment(UUID sessionId) {
    Session session = findSessionOrThrow(sessionId);

    if (session.getStatus() == Session.SessionStatus.ACTIVE) {
      if (session.getPaidUntil() != null
          && session.getPaidUntil().plusMinutes(15).isBefore(LocalDateTime.now())) {
        return new Payment(BigDecimal.ZERO, "USD", 0);
      }
    }

    LocalDateTime paymentStartTime =
        session.getPaidUntil() == null ? session.getStartTime() : session.getPaidUntil();
    long sessionDurationInMinutes;

    if (session.getStatus().equals(Session.SessionStatus.ACTIVE)
        || session.getStatus().equals(Session.SessionStatus.PREPARE_END)) {
      sessionDurationInMinutes =
          Duration.between(paymentStartTime, LocalDateTime.now()).toMinutes();
    } else if (session.getEndTime() != null) {
      sessionDurationInMinutes =
          Duration.between(session.getStartTime(), session.getEndTime()).toMinutes();
    } else {
      throw new SessionInvalidStatusException(SESSION_INVALID_STATUS_MESSAGE);
    }

    TariffDto tariffDto =
        fetchTariff(
            session, sessionDurationInMinutes, fetchParkingCurrency(session.getParkingId()));
    String currency = fetchParkingCurrency(session.getParkingId());

    BigDecimal totalAmount = calculateTotalAmount(tariffDto, sessionDurationInMinutes);
    BigDecimal userPaid = fetchUserPaidAmount(sessionId, currency);

    BigDecimal remainingAmount = totalAmount.subtract(userPaid);

    long remainingTime = 0;

    try  {
      remainingTime=
        remainingAmount
            .divide(
                tariffDto.price().divide(BigDecimal.valueOf(60), 2, RoundingMode.FLOOR),
                2,
                RoundingMode.FLOOR)
            .longValue();
    } catch ( ArithmeticException exception ){
      log.error("Error calculating remaining time for session id: {}", sessionId);
      log.error("Error message: {}", exception.getMessage(), exception);
      remainingTime = 0;
    }

    return new Payment(remainingAmount, currency, remainingTime);
  }

  private void validateSessionStatus(Session session, Session.SessionStatus... validStatuses) {
    if (!Arrays.asList(validStatuses).contains(session.getStatus())) {
      throw new SessionInvalidStatusException(
          String.format(SESSION_INVALID_STATUS_MESSAGE, Arrays.toString(validStatuses)));
    }
  }

  private boolean validatePayment(Session session) {
    return session.getPaidUntil() != null
        && LocalDateTime.now().isBefore(session.getPaidUntil().plusMinutes(15));
  }

  private TariffDto fetchTariff(Session session, long sessionDurationInMinutes, String currency)
      throws BusinessException {
    try {
      ResponseEntity<TariffDto> response =
          parkingClient.getActiveTariff(
              session.getParkingId(), session.getVehicleId(), sessionDurationInMinutes, currency);
      if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
        throw new BusinessException("Failed to fetch tariff");
      }
      return response.getBody();
    } catch (FeignException exception) {
      log.error(
          "Error fetching tariff for parking id: {}, vehicle id: {}",
          session.getParkingId(),
          session.getVehicleId());
      log.error("Error message: {}", exception.getMessage(), exception);
      throw new BusinessException("Failed to fetch tariff");
    }
  }

  private String fetchParkingCurrency(UUID parkingId) {
    ResponseEntity<String> response = parkingClient.getParkingDefaultCurrency(parkingId);
    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
      return response.getBody();
    }
    return "USD";
  }

  private BigDecimal calculateTotalAmount(TariffDto tariffDto, long sessionDurationInMinutes) {
    BigDecimal pricePerMinute =
        tariffDto.price().divide(BigDecimal.valueOf(60), BigDecimal.ROUND_FLOOR);
    return pricePerMinute.multiply(BigDecimal.valueOf(sessionDurationInMinutes));
  }

  private BigDecimal fetchUserPaidAmount(UUID sessionId, String currency) {
    ResponseEntity<BigDecimal> response =
        paymentClient.calculateTotalPaidAmount(sessionId, currency.toUpperCase());
    if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
      throw new BusinessException("Failed to fetch user paid amount");
    }
    return response.getBody();
  }
}
