package pl.edu.zut.app.parking.payments_ms.dto;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * A record representing the response from an exchange rate API.
 *
 * This record contains details such as a disclaimer message, license information,
 * a timestamp indicating when the rates were fetched, the base currency, and a
 * mapping of currency codes to their respective exchange rates.
 */
public record ExchangeRateApiResponse(
    String disclaimer,
    String license,
    long timestamp,
    String base,
    HashMap<String, BigDecimal> rates) {}
