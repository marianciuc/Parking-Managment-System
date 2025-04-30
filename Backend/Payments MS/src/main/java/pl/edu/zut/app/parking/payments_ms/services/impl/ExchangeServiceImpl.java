package pl.edu.zut.app.parking.payments_ms.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.zut.app.parking.payments_ms.dto.ExchangeRateApiResponse;
import pl.edu.zut.app.parking.payments_ms.entities.ConversionDetails;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.exceptions.ExchangeException;
import pl.edu.zut.app.parking.payments_ms.exceptions.ExchangingProcessException;
import pl.edu.zut.app.parking.payments_ms.exceptions.InvalidExchangeArgumentsException;
import pl.edu.zut.app.parking.payments_ms.services.ExchangeService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${org.openexchangerates.url}")
  private String exchangeUrl;

  @Value("${org.openexchangerates.app_id}")
  private String appId;

  private Map<String, BigDecimal> exchangeRatesCache;

  @PostConstruct
  public void initializeExchangeRates() {
    updateExchangeRates();
  }

  @Override
  public ConversionDetails exchange(Currency from, Currency to, BigDecimal amount)
      throws ExchangeException {

    log.info("Getting exchange rate from {} to {}", from, to);

    BigDecimal destinationAmount = convert(amount, from, to);
    BigDecimal rate = amount.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : destinationAmount.divide(amount, 6,
            RoundingMode.HALF_EVEN);

    return ConversionDetails.builder()
        .conversionRate(rate)
        .sourceAmount(amount)
        .sourceCurrency(from)
        .destinationAmount(destinationAmount)
        .destinationCurrency(to)
        .currencyDate(LocalDateTime.now())
        .build();
  }

  private BigDecimal convert(BigDecimal amount, Currency from, Currency to)
      throws InvalidExchangeArgumentsException {
    log.info("Converting {} {} to {}", amount, from, to);
    for (int attempts = 0; attempts < 3; attempts++) {
      BigDecimal rateToBaseCurrency = exchangeRatesCache.get(from.getCode().toUpperCase());
      BigDecimal rateFromBaseCurrency = exchangeRatesCache.get(to.getCode().toUpperCase());

      if (from.equals(to)) {
        return amount;
      }
      if (amount.compareTo(BigDecimal.ZERO) == 0) {
        return BigDecimal.ZERO;
      }

      if (from.equals(Currency.USD)) {
        rateToBaseCurrency = BigDecimal.ONE;
      }
      if (to.equals(Currency.USD)) {
        rateFromBaseCurrency = BigDecimal.ONE;
      }

      if (rateToBaseCurrency != null && rateFromBaseCurrency != null) {
        BigDecimal amountInBaseCurrency =
            amount.divide(rateToBaseCurrency, 6, RoundingMode.HALF_EVEN);
        log.info(
            "Exchange rate from {} to {}: {}/{}",
            from,
            to,
            rateToBaseCurrency,
            rateFromBaseCurrency);
        return amountInBaseCurrency
            .multiply(rateFromBaseCurrency)
            .setScale(2, RoundingMode.HALF_EVEN);
      }

      log.warn("Exchange rates not found for {} and {}, retrying...", from, to);
      try{
        updateExchangeRates();
      } catch (Exception e) {
        log.error("Error updating exchange rates: {}", e.getMessage(), e);
      }
    }

    log.error("Failed to retrieve exchange rates after multiple attempts");
    throw new InvalidExchangeArgumentsException(
        "Failed to retrieve exchange rates after multiple attempts");
  }

  @Scheduled(fixedRate = 60_000_000)
  public void updateExchangeRates() {
    log.info("Updating exchange rates");
    try {
      String url = buildUrlForAllCurrencies();
      ExchangeRateApiResponse apiResponse = callExchangeApi(url);
      exchangeRatesCache = apiResponse.rates();
      log.info("Exchange rates updated: {}", exchangeRatesCache);
    } catch (Exception e) {
      log.error("Error updating exchange rates: {}", e.getMessage(), e);
    }
  }

  private String buildUrlForAllCurrencies() {
    return UriComponentsBuilder.fromUriString(exchangeUrl)
        .queryParam("app_id", appId)
        .queryParam("base", Currency.USD.getCode())
        .toUriString();
  }

  private ExchangeRateApiResponse callExchangeApi(String url) throws ExchangingProcessException {
    ResponseEntity<ExchangeRateApiResponse> responseEntity =
        restTemplate.getForEntity(url, ExchangeRateApiResponse.class);
    log.info("Exchange API response: {}", responseEntity);
    if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
      return responseEntity.getBody();
    }

    log.error("Exchange API returned error status code: {}", responseEntity.getStatusCodeValue());
    throw new ExchangingProcessException(
        "Exchange API returned error status code:" + responseEntity.getStatusCodeValue());
  }

  private String buildUrl(Currency from, Currency to) {
    return UriComponentsBuilder.fromHttpUrl(exchangeUrl)
        .queryParam("app_id", appId)
        .queryParam("base", from.getCode())
        .queryParam("symbols", to.getCode())
        .toUriString();
  }

  private void validateInputs(Currency from, Currency to, BigDecimal amount)
      throws InvalidExchangeArgumentsException {
    if (from == null || to == null || amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new InvalidExchangeArgumentsException(
          "Invalid input parameters: from = " + from + ", to = " + to + ", amount = " + amount);
    }
  }
}
