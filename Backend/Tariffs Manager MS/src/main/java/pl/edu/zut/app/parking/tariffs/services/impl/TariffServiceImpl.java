package pl.edu.zut.app.parking.tariffs.services.impl;

import feign.FeignException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.zut.app.parking.tariffs.clients.ParkingClient;
import pl.edu.zut.app.parking.tariffs.clients.PaymentClient;
import pl.edu.zut.app.parking.tariffs.dto.TariffDto;
import pl.edu.zut.app.parking.tariffs.dto.UpdateTariffRequest;
import pl.edu.zut.app.parking.tariffs.dto.external.responces.ExchangeRateResDto;
import pl.edu.zut.app.parking.tariffs.dto.external.responces.ParkingResDto;
import pl.edu.zut.app.parking.tariffs.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.tariffs.entities.Tariff;
import pl.edu.zut.app.parking.tariffs.enums.Currency;
import pl.edu.zut.app.parking.tariffs.enums.TariffClass;
import pl.edu.zut.app.parking.tariffs.exceptions.*;
import pl.edu.zut.app.parking.tariffs.kafka.messages.ChangeParkingCurrencyMessage;
import pl.edu.zut.app.parking.tariffs.kafka.messages.CreatedParkingMessage;
import pl.edu.zut.app.parking.tariffs.repositories.TariffRepository;
import pl.edu.zut.app.parking.tariffs.services.TariffService;
import pl.edu.zut.app.parking.tariffs.utils.ResponseBodyUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

  private final TariffRepository tariffRepository;
  private final ParkingClient parkingClient;
  private final PaymentClient paymentClient;

  @Override
  @Transactional(
      isolation = Isolation.READ_COMMITTED,
      rollbackFor = {
        TariffAlreadyExistsException.class,
        ParkingNotFoundException.class,
        ExternalServiceException.class
      })
  public TariffDto create(TariffDto tariffDto, UUID parkingId) {
    ResponseEntity<ParkingResDto> parkingResponse = parkingClient.getParkingById(parkingId);
    ParkingResDto parkingResDto =
        ResponseBodyUtil.getResponseBodyOrThrow(
            parkingResponse, "Error fetching " + "parking information");

    checkTariffDuplicate(parkingId, tariffDto.tariffClass());

    ResponseEntity<String> currencyResponse =
        paymentClient.getAccountCurrency(parkingResDto.ownerId());
    String currency =
        ResponseBodyUtil.getResponseBodyOrThrow(
            currencyResponse, "Error fetching account currency");

    Tariff tariff =
        Tariff.builder()
            .parkingId(parkingId)
            .name(tariffDto.name())
            .price(tariffDto.price())
            .currency(Currency.fromCode(currency))
            .description(tariffDto.description())
            .tariffClass(tariffDto.tariffClass())
            .build();

    Tariff saved = tariffRepository.save(tariff);
    return TariffDto.fromEntity(tariffRepository.save(saved));
  }

  @Override
  @Transactional
  public void updateTariffCurrency(UUID tariffId, String currency) {
    Tariff tariff =
        tariffRepository
            .findById(tariffId)
            .orElseThrow(() -> new TariffNotFoundException("Tariff not found"));

    ResponseEntity<ExchangeRateResDto> response =
        paymentClient.exchange(tariff.getCurrency().getCode(), currency, tariff.getPrice());

    if (response.getStatusCode().isError()) {
      throw new ExternalServiceException("Error fetching exchange rate");
    } else if (response.getStatusCode() == HttpStatus.NOT_FOUND || response.getBody() == null) {
      throw new ExternalServiceException("Error fetching exchange rate");
    }

    BigDecimal scaledPrice =
        response
            .getBody()
            .conversionRate()
            .multiply(tariff.getPrice())
            .setScale(1, RoundingMode.UP);

    tariff.setCurrency(Currency.fromCode(currency));
    tariff.setPrice(scaledPrice);

    tariffRepository.save(tariff);
  }

  @Override
  public TariffDto update(UUID id, UpdateTariffRequest tariffDto) {
    Tariff tariff =
        tariffRepository
            .findByParkingIdAndRecordStatus(id, AbstractBaseEntity.RecordStatus.ACTIVE)
            .orElseThrow(() -> new TariffNotFoundException("Tariff not found"));

    if (tariffDto.name() != null && !tariffDto.name().equals(tariff.getName())) {
      tariff.setName(tariffDto.name());
    }

    if (tariffDto.description() != null
        && !tariffDto.description().equals(tariff.getDescription())) {
      tariff.setDescription(tariffDto.description());
    }

    if (tariffDto.tariffClass() != null
        && !tariffDto.tariffClass().equals(tariff.getTariffClass())) {
      checkTariffDuplicate(tariff.getParkingId(), tariffDto.tariffClass());
      tariff.setTariffClass(tariffDto.tariffClass());
    }

    if (tariffDto.price() != null && !tariffDto.price().equals(tariff.getPrice())) {
      tariff.setPrice(tariffDto.price());
    }

    return TariffDto.fromEntity(tariffRepository.save(tariff));
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    log.debug("Deleting tariff with id: {}", id);
    Tariff tariff =
        tariffRepository
            .findById(id)
            .orElseThrow(() -> new TariffNotFoundException("Tariff not found"));

    if (tariff.getRecordStatus() == AbstractBaseEntity.RecordStatus.DELETED) {
      throw new TariffIllegalStateException("Tariff has already been deleted");
    }

    tariff.setRecordStatus(AbstractBaseEntity.RecordStatus.DELETED);
    tariffRepository.save(tariff);
    log.debug("Deleted tariff with id: {}", id);
  }

  @Override
  public List<TariffDto> findAllByTime(UUID parkingId, Long minutes, String currency) {
    log.debug("Fetching all tariffs for parkingId and time: {}", parkingId);
    List<Tariff> sortedTariffs =
        tariffRepository
            .findAllByParkingIdAndRecordStatus(parkingId, AbstractBaseEntity.RecordStatus.ACTIVE)
            .stream()
            .sorted(Comparator.comparingInt(tariff -> tariff.getTariffClass().getMinutes()))
            .toList();

    if (sortedTariffs.isEmpty()) {
      return List.of();
    }

    Tariff theBetterTariff =
        sortedTariffs.stream()
            .filter(tariff -> minutes == null || minutes <= tariff.getTariffClass().getMinutes())
            .findFirst()
            .orElse(null);
    if (theBetterTariff == null) {
      theBetterTariff = sortedTariffs.getLast();
    }

    if (currency != null) {
      try {
        ResponseEntity<ExchangeRateResDto> exchangedAmount =
            paymentClient.exchange(
                String.valueOf(theBetterTariff.getCurrency()),
                currency.toUpperCase(),
                theBetterTariff.getPrice());

        if (exchangedAmount.getStatusCode().isError()) {
          throw new ExternalServiceException("Error fetching exchange rate");
        }
        theBetterTariff.setPrice(
            exchangedAmount.getBody().conversionRate().multiply(theBetterTariff.getPrice()));
        theBetterTariff.setCurrency(Currency.fromCode(currency));
      } catch (FeignException | ExternalServiceException e) {
        log.error("Error fetching exchange rate", e);
        throw new ExternalServiceException("Error fetching exchange rate");
      }
    }

    return List.of(TariffDto.fromEntity(theBetterTariff));
  }

  @Override
  public List<TariffDto> findAllByParkingId(UUID parkingId) {
    return tariffRepository
        .findAllByParkingIdAndRecordStatus(parkingId, AbstractBaseEntity.RecordStatus.ACTIVE)
        .stream()
        .map(TariffDto::fromEntity)
        .toList();
  }

  @Override
  public TariffDto find(UUID id, String currency) {
    log.debug("Fetching tariff with id: {}", id);
    Tariff tariff =
        tariffRepository
            .findByIdAndRecordStatus(id, AbstractBaseEntity.RecordStatus.ACTIVE)
            .orElseThrow(() -> new TariffNotFoundException("Tariff not found"));

    if (currency != null) {
      try {
        ResponseEntity<ExchangeRateResDto> exchangedAmount =
            paymentClient.exchange(
                String.valueOf(tariff.getCurrency()), currency.toUpperCase(), tariff.getPrice());

        log.debug("Exchanged amount: {}", exchangedAmount);
        if (exchangedAmount.getStatusCode().isError()) {
          throw new ExternalServiceException("Error fetching exchange rate");
        }
        tariff.setPrice(exchangedAmount.getBody().conversionRate().multiply(tariff.getPrice()));
        tariff.setCurrency(Currency.fromCode(currency));
      } catch (FeignException | ExternalServiceException e) {
        log.error("Error fetching exchange rate", e);
        throw new ExternalServiceException("Error fetching exchange rate");
      }
    }
    return TariffDto.fromEntity(tariff);
  }

  @Override
  @Transactional
  public void createBaseTariff(CreatedParkingMessage message) {
    TariffDto tariffDto =
        new TariffDto(
            null,
            null,
            null,
            null,
            null,
            message.parkingId(),
            "Default tariff",
            "It's a default tariff for parking",
            TariffClass.FOR_1_HOUR,
            BigDecimal.valueOf(1.0),
            null);
    create(tariffDto, message.parkingId());
  }

  @Override
  public void updateParkingCurrency(ChangeParkingCurrencyMessage message) {
    List<Tariff> tariffsToUpdate = List.of();
    for (UUID parkingId : message.parkingIds()) {
      tariffRepository.findAllByParkingId(parkingId).stream()
          .map(
              tariff -> {
                tariff.setCurrency(Currency.fromCode(message.currency()));
                tariffsToUpdate.add(tariff);
                return tariff;
              })
          .toList();
    }
    tariffRepository.saveAll(tariffsToUpdate);
  }

  private void checkTariffDuplicate(UUID parkingId, TariffClass tariffClass) {
    if (tariffClass.equals(TariffClass.FOR_WHITELISTED)) return;
    log.debug("Checking if tariff with class already exists for parkingId: {}", parkingId);

    if (tariffRepository.existsByParkingIdAndTariffClassAndRecordStatus(
        parkingId, tariffClass, AbstractBaseEntity.RecordStatus.ACTIVE)) {
      log.error("Tariff with class already exists for parkingId: {}", parkingId);
      throw new TariffAlreadyExistsException(
          "Tariff with class already exists for parkingId: " + parkingId);
    }
    log.debug("Tariff with class does not exist for parkingId: {}", parkingId);
  }
}
