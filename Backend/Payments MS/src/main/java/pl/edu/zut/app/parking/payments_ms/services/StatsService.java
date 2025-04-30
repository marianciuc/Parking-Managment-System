package pl.edu.zut.app.parking.payments_ms.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import pl.edu.zut.app.parking.payments_ms.dto.stats.*;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.exceptions.AccountBalanceNotFoundException;

/**
 * Interface for providing statistical services related to account balance, incomes, revenues,
 * and parking session calculations. This service supports data retrieval grouped by various
 * criterias like days of the week, specific time ranges, parking locations, and currencies.
 */
public interface StatsService {

  /**
   * Retrieves statistical details of an account balance for the specified owner.
   *
   * @param ownerId The unique identifier of the owner whose account balance statistics are to be
   *     retrieved.
   * @param converted A flag indicating whether the account balance should be converted to a
   *     different currency.
   * @return An AccountBalanceStatsDto object containing details such as current balance, blocked
   *     amount, withdrawn amount, currency symbol, and currency.
   * @throws AccountBalanceNotFoundException if the account balance not found
   */
  AccountBalanceStatsDto getAccountBalanceStats(UUID ownerId, Boolean converted)
      throws AccountBalanceNotFoundException;

  /**
   * Retrieves statistical data of incomes grouped by days of the week for a specific owner and a
   * specified time period.
   *
   * @param ownerId The unique identifier of the owner whose income statistics are to be retrieved.
   * @param converted A flag indicating whether the incomes should be converted to a different
   *     currency.
   * @param from The starting date and time for the income statistics.
   * @param to The ending date and time for the income statistics.
   * @return An IncomesByDayStats object containing total income, incomes grouped by day of the
   *     week, and associated currency details for the specified period.
   */
  IncomesByDayStats getIncomesByDayStats(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to);

  /**
   * Retrieves statistical income data grouped by parking lot for a specific owner and a specified
   * time period. The resulting statistics contain the total income and data grouped by days of the
   * week for each parking lot.
   *
   * @param ownerId The unique identifier of the owner whose income statistics are to be retrieved.
   * @param converted A flag indicating whether the incomes should be converted to a different
   *     currency.
   * @param from The starting date and time for the income statistics.
   * @param to The ending date and time for the income statistics.
   * @param parkingId The unique identifier of the parking lot for which statistics are to be
   *     retrieved.
   * @return A HashMap where the key is the parking lot's unique identifier (UUID) and the value is
   *     an IncomesByDayStats object containing total income, incomes grouped by day of the week,
   *     and associated currency details for the specified time period.
   */
  HashMap<UUID, IncomesByDayStats> getIncomesByDayStatsGroupedByParking(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to, UUID parkingId);

  /**
   * Retrieves the revenue sharing details for a specific owner within a specified time range.
   *
   * @param ownerId The unique identifier of the owner for whom the revenue share details are to be
   *     retrieved.
   * @param converted A flag indicating whether the revenue should be converted to a different
   *     currency.
   * @param from The starting date and time for the revenue share calculation.
   * @param to The ending date and time for the revenue share calculation.
   * @return A RevenueShare object containing revenue distribution per parking, total revenue, time
   *     period, and currency details.
   */
  RevenueShare getRevenueShare(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to);

  /**
   * Retrieves statistical income data grouped by hours of the day for a specific owner within a
   * specified time range.
   *
   * @param ownerId The unique identifier of the owner whose income statistics are to be retrieved.
   * @param converted A flag indicating whether the incomes should be converted to a different
   *     currency.
   * @param from The starting date and time for the income statistics.
   * @param to The ending date and time for the income statistics.
   * @return An IncomesByDayTimeStats object containing income distribution across hourly intervals,
   *     the associated currency, and its standardized code for the specified time period.
   */
  IncomesByDayTimeStats getIncomesByDayTime(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to);

  /**
   * Retrieves statistical income data grouped by specific hours of the day and grouped by parking
   * lot for a specific owner within a specified time range.
   *
   * @param ownerId The unique identifier of the owner whose income statistics are to be retrieved.
   * @param converted A flag indicating whether the incomes should be converted to a different
   *     currency.
   * @param from The starting date and time for the income statistics.
   * @param to The ending date and time for the income statistics.
   * @param parkingId The unique identifier of the parking lot for which statistics are to be
   *     retrieved.
   * @return A HashMap where the key is the parking lot's unique identifier (UUID), and the value is
   *     an IncomesByDayTimeStats object containing income distribution across hourly intervals and
   *     associated currency details for the specified time period.
   */
  HashMap<UUID, IncomesByDayTimeStats> getIncomesByDayTimeGroupedByParking(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to, UUID parkingId);

  /**
   * Calculates the total amount for a given parking session in the specified currency.
   *
   * @param sessionId The unique identifier of the parking session.
   * @param currency The currency in which the total amount should be calculated.
   * @return The total amount for the parking session as a BigDecimal.
   */
  BigDecimal calculateSessionTotalAmount(UUID sessionId, Currency currency);

  /**
   * Retrieves a detailed list of revenue data for a specific parking lot owned by an owner within a
   * specified time range.
   *
   * @param ownerId The unique identifier of the owner whose revenue details are to be retrieved.
   * @param parkingId The unique identifier of the parking lot for which the revenue details are to
   *     be retrieved.
   * @param converted A flag indicating whether the revenue amounts should be converted to a
   *     different currency.
   * @param from The starting date and time for the revenue reporting period.
   * @param to The ending date and time for the revenue reporting period.
   * @return A HashMap where the key is a parking lot's unique identifier (UUID) and the value is a
   *     list of ParkingRevenue records containing revenue information such as date, amount,
   *     currency, and currency symbol for the specified period.
   */
  HashMap<UUID, List<ParkingRevenue>> getReneview(
      UUID ownerId, UUID parkingId, Boolean converted, LocalDateTime from, LocalDateTime to);
}
