package pl.edu.zut.app.parking.payments_ms.dto.stats;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;

/**
 * Represents statistical data of incomes grouped by days of the week. This record provides details
 * about income figures distributed across specific days in a specified time period, along with
 * associated currency information.
 *
 * <p>Fields:
 *
 * <p>- `incomesPerDay`: A HashMap where each key is a {@link DayOfWeek} representing a day of the
 * week, and the corresponding value is a {@link BigDecimal} representing the total income for that
 * day. - `totalIncomes`: The total income sum for the specified time period. - `from`: The start
 * date and time of the period for which incomes are being recorded. - `to`: The end date and time
 * of the period for which incomes are being recorded. - `currencySymbol`: The symbol of the
 * currency in which the incomes are measured. - `currency`: The currency in which the incomes are
 * represented, as a {@link Currency} object.
 */
public record IncomesByDayStats(
    HashMap<DayOfWeek, BigDecimal> incomesPerDay,
    BigDecimal totalIncomes,
    LocalDateTime from,
    LocalDateTime to,
    String currencySymbol,
    Currency currency) {}
