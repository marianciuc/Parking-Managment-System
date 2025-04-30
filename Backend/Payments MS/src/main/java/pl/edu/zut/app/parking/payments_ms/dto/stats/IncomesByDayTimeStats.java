package pl.edu.zut.app.parking.payments_ms.dto.stats;

import pl.edu.zut.app.parking.payments_ms.enums.Currency;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Represents the statistical data of incomes grouped by specific hours of the day.
 * This record provides information about income distribution across hourly intervals,
 * including the currency details and its corresponding code.
 *
 * Fields:
 *
 * - `incomes`: A HashMap where the key represents the hour of the day (0-23),
 *    and the value is the total income for that hour in the specified currency.
 * - `currency`: The currency in which the incomes are recorded, represented as a `Currency` object.
 * - `currencyCode`: The standardized code (e.g., ISO 4217 code such as PLN, USD, EUR) of the specified currency.
 */
public record IncomesByDayTimeStats(
        HashMap<Integer, BigDecimal> incomes,
        Currency currency,
        String currencyCode
) {}
