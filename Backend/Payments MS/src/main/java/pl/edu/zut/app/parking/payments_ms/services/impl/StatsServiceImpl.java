package pl.edu.zut.app.parking.payments_ms.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.payments_ms.dto.common.AccountBalanceDto;
import pl.edu.zut.app.parking.payments_ms.dto.stats.AccountBalanceStatsDto;
import pl.edu.zut.app.parking.payments_ms.dto.stats.IncomesByDayStats;
import pl.edu.zut.app.parking.payments_ms.dto.stats.IncomesByDayTimeStats;
import pl.edu.zut.app.parking.payments_ms.dto.stats.ParkingRevenue;
import pl.edu.zut.app.parking.payments_ms.dto.stats.RevenueShare;
import pl.edu.zut.app.parking.payments_ms.entities.AccountBalance;
import pl.edu.zut.app.parking.payments_ms.entities.AccountBalanceStats;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionStatus;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionType;
import pl.edu.zut.app.parking.payments_ms.exceptions.AccountBalanceNotFoundException;
import pl.edu.zut.app.parking.payments_ms.repositories.AccountBalanceStatsRepository;
import pl.edu.zut.app.parking.payments_ms.repositories.TransactionsRepository;
import pl.edu.zut.app.parking.payments_ms.services.AccountBalanceService;
import pl.edu.zut.app.parking.payments_ms.services.ExchangeService;
import pl.edu.zut.app.parking.payments_ms.services.StatsService;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

  @PersistenceContext private EntityManager entityManager;
  private final AccountBalanceService accountBalanceService;
  private final AccountBalanceStatsRepository accountBalanceStatsRepository;
  private final TransactionsRepository transactionsRepository;
  private final ExchangeService exchangeService;

  private final Currency SYSTEM_CURRENCY = Currency.USD;

  private void updateAccountBalanceStats(UUID accountBalanceId)
      throws AccountBalanceNotFoundException {
    AccountBalanceStats accountBalanceStats =
        accountBalanceStatsRepository
            .findByAccountBalance_Id(accountBalanceId)
            .orElseGet(
                () -> {
                  try {
                    AccountBalanceDto accountBalanceDto =
                        accountBalanceService.getAccountBalance(accountBalanceId, false);

                    return accountBalanceStatsRepository.save(
                        AccountBalanceStats.builder()
                            .accountBalance(
                                entityManager.getReference(AccountBalance.class, accountBalanceId))
                            .blockedAmount(BigDecimal.ZERO)
                            .currentBalance(accountBalanceDto.balance())
                            .withdrawnAmount(BigDecimal.ZERO)
                            .build());
                  } catch (Exception e) {
                    log.error("Exception occurred: {}", e.getMessage(), e);
                    throw new AccountBalanceNotFoundException(e.getMessage());
                  }
                });

    BigDecimal blockedBalance =
        transactionsRepository
            .findAllByTypeAndStatusAndSourceAccountId(
                TransactionType.WITHDRAWAL, TransactionStatus.PENDING, accountBalanceId)
            .stream()
            .map(transaction -> transaction.getConversionDetails().getSourceAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal withdrawnAmount =
        transactionsRepository
            .findAllByTypeAndStatusAndDestinationAccountId(
                TransactionType.WITHDRAWAL, TransactionStatus.COMPLETED, accountBalanceId)
            .stream()
            .map(transaction -> transaction.getConversionDetails().getSourceAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    accountBalanceStats.setBlockedAmount(blockedBalance);
    accountBalanceStats.setWithdrawnAmount(withdrawnAmount);
    accountBalanceStats.setCurrentBalance(
        accountBalanceService.getAccountBalance(accountBalanceId, false).balance());
    accountBalanceStatsRepository.save(accountBalanceStats);
  }

  @Override
  public AccountBalanceStatsDto getAccountBalanceStats(UUID ownerId, Boolean converted)
      throws AccountBalanceNotFoundException {
    AccountBalanceStats accountBalanceStats =
        accountBalanceStatsRepository
            .findOneByAccountBalance_OwenerId(ownerId)
            .orElseGet(
                () -> {
                  try {
                    AccountBalanceDto accountBalanceDto =
                        accountBalanceService.getOwnerAccountBalance(ownerId, false);
                    updateAccountBalanceStats(accountBalanceDto.id());
                    return accountBalanceStatsRepository
                        .findByAccountBalance_Id(accountBalanceDto.id())
                        .orElse(null);
                  } catch (Exception e) {
                    log.error("Exception occurred: {}", e.getMessage(), e);
                    throw new AccountBalanceNotFoundException(e.getMessage());
                  }
                });
    if (accountBalanceStats == null) {
      return getAccountBalanceStats(ownerId, converted);
    }
    if (converted != null && converted) {
      BigDecimal convertedBalance =
          exchangeService
              .exchange(
                  accountBalanceStats.getAccountBalance().getSystemCurrency(),
                  accountBalanceStats.getAccountBalance().getPreferredCurrency(),
                  accountBalanceStats.getCurrentBalance())
              .getDestinationAmount();

      BigDecimal convertedBlockedAmount =
          exchangeService
              .exchange(
                  accountBalanceStats.getAccountBalance().getSystemCurrency(),
                  accountBalanceStats.getAccountBalance().getPreferredCurrency(),
                  accountBalanceStats.getBlockedAmount())
              .getDestinationAmount();

      BigDecimal convertedWithdrawn =
          exchangeService
              .exchange(
                  accountBalanceStats.getAccountBalance().getSystemCurrency(),
                  accountBalanceStats.getAccountBalance().getPreferredCurrency(),
                  accountBalanceStats.getWithdrawnAmount())
              .getDestinationAmount();

      return new AccountBalanceStatsDto(
          convertedBalance,
          convertedBlockedAmount,
          convertedWithdrawn,
          accountBalanceStats.getAccountBalance().getPreferredCurrency().getSymbolKey(),
          accountBalanceStats.getAccountBalance().getPreferredCurrency());
    }
    return AccountBalanceStatsDto.fromEntity(
        accountBalanceStats, accountBalanceStats.getAccountBalance().getSystemCurrency());
  }

  @Override
  public IncomesByDayStats getIncomesByDayStats(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to) {
    try {
      List<Object[]> resultList =
          entityManager
              .createNativeQuery(
                  "SELECT EXTRACT(DOW FROM t.creation_date), SUM(t.destination_amount) "
                      + "FROM transactions t "
                      + "WHERE t.destination_account_id = :ownerId "
                      + "AND (t.type = :type OR t.type = :type2) "
                      + "AND t.status = :status "
                      + "AND t.creation_date BETWEEN :from AND :to "
                      + "GROUP BY EXTRACT(DOW FROM t.creation_date)")
              .setParameter("ownerId", ownerId)
              .setParameter("type", TransactionType.SESSIONS_PAYMENT.name())
              .setParameter("type2", TransactionType.SESSION_EXTERNAL_PAYMENT.name())
              .setParameter("status", TransactionStatus.COMPLETED.name())
              .setParameter("from", from)
              .setParameter("to", to)
              .getResultList();

      HashMap<DayOfWeek, BigDecimal> stats = new HashMap<>();

      BigDecimal totalIncome = BigDecimal.ZERO;
      Currency currency = Currency.USD;

      for (Object[] row : resultList) {
        Integer dayOfWeek = ((Number) row[0]).intValue();
        BigDecimal amount = (BigDecimal) row[1];
        totalIncome = totalIncome.add(amount);

        if (Boolean.TRUE.equals(converted)) {
          Currency systemCurrency = Currency.USD;
          currency = accountBalanceService.getOwnerCurrency(ownerId);
          amount =
              exchangeService.exchange(systemCurrency, currency, amount).getDestinationAmount();
        }

        stats.put(DayOfWeek.of(dayOfWeek), amount);
      }

      return new IncomesByDayStats(stats, totalIncome, from, to, currency.getSymbol(), currency);
    } catch (Exception e) {
      log.error("Exception occurred: {}", e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public HashMap<UUID, IncomesByDayStats> getIncomesByDayStatsGroupedByParking(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to, UUID parkingId) {
    return null;
  }

  @Override
  public RevenueShare getRevenueShare(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to) {
    try {
      List<Object[]> resultList =
          entityManager
              .createQuery(
                  "SELECT t.metadata.parkingId, SUM(t.conversionDetails.destinationAmount) "
                      + "FROM Transaction t "
                      + "WHERE t.destinationAccount.ownerId = :ownerId "
                      + "AND (t.type = :type OR t.type = :type2) "
                      + "AND t.status = :status "
                      + "AND t.metadata.parkingId IS NOT NULL "
                      + "AND t.creationDate BETWEEN :from AND :to "
                      + "GROUP BY t.metadata.parkingId",
                  Object[].class)
              .setParameter("ownerId", ownerId)
              .setParameter("type", TransactionType.SESSIONS_PAYMENT)
              .setParameter("type2", TransactionType.SESSION_EXTERNAL_PAYMENT)
              .setParameter("status", TransactionStatus.COMPLETED)
              .setParameter("from", from)
              .setParameter("to", to)
              .getResultList();

      log.info("Result list: {}", resultList);

      HashMap<UUID, BigDecimal> stats = new HashMap<>();

      BigDecimal totalIncome = BigDecimal.ZERO;
      Currency currency = Currency.USD;

      for (Object[] row : resultList) {
        UUID parkingId = (UUID) row[0];
        BigDecimal amount = (BigDecimal) row[1];
        totalIncome = totalIncome.add(amount);

        if (Boolean.TRUE.equals(converted)) {
          Currency systemCurrency = Currency.USD;
          currency = accountBalanceService.getOwnerCurrency(ownerId);
          amount =
              exchangeService.exchange(systemCurrency, currency, amount).getDestinationAmount();
        }

        stats.put(parkingId, amount);
      }

      return new RevenueShare(stats, totalIncome, from, to, currency.getSymbol(), currency);
    } catch (Exception e) {
      log.error("Exception occurred: {}", e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public IncomesByDayTimeStats getIncomesByDayTime(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to) {
    try {
      List<Object[]> resultList =
          entityManager
              .createQuery(
                  "SELECT FUNCTION('EXTRACT', FUNCTION('DOW', t.creationDate)), SUM(t.conversionDetails.destinationAmount) "
                      + "FROM Transaction t "
                      + "WHERE t.destinationAccount.ownerId = :ownerId "
                      + "AND (t.type = :type OR t.type = :type2) "
                      + "AND t.status = :status "
                      + "AND t.creationDate BETWEEN :from AND :to "
                      + "GROUP BY FUNCTION('EXTRACT', FUNCTION('DOW', t.creationDate))",
                  Object[].class)
              .setParameter("ownerId", ownerId)
              .setParameter("type", TransactionType.SESSIONS_PAYMENT)
              .setParameter("type2", TransactionType.SESSION_EXTERNAL_PAYMENT)
              .setParameter("status", TransactionStatus.COMPLETED)
              .setParameter("from", from)
              .setParameter("to", to)
              .getResultList();

      HashMap<Integer, BigDecimal> stats = new HashMap<>();

      Currency currency = Currency.USD;

      for (Object[] row : resultList) {
        Integer hour = (Integer) row[0];
        BigDecimal amount = (BigDecimal) row[1];

        if (Boolean.TRUE.equals(converted)) {
          Currency systemCurrency = Currency.USD;
          currency = accountBalanceService.getOwnerCurrency(ownerId);
          amount =
              exchangeService.exchange(systemCurrency, currency, amount).getDestinationAmount();
        }

        stats.put(hour, amount);
      }
      return new IncomesByDayTimeStats(stats, currency, currency.getSymbol());
    } catch (Exception e) {
      log.error("Exception occurred: {}", e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public HashMap<UUID, IncomesByDayTimeStats> getIncomesByDayTimeGroupedByParking(
      UUID ownerId, Boolean converted, LocalDateTime from, LocalDateTime to, UUID parkingId) {

    StringBuilder queryBuilder =
        new StringBuilder(
            "SELECT t.metadata.parkingId, HOUR(t.creationDate), SUM(t.conversionDetails.destinationAmount) "
                + "FROM Transaction t "
                + "WHERE t.destinationAccount.ownerId = :ownerId "
                + "AND (t.type = :type OR t.type = :type2) "
                + "AND t.status = :status "
                + "AND t.metadata.parkingId IS NOT NULL "
                + "AND t.creationDate BETWEEN :from AND :to ");

    if (parkingId != null) {
      queryBuilder.append(" AND t.metadata.parkingId = :parkingId");
    }

    queryBuilder.append(" GROUP BY t.metadata.parkingId, HOUR(t.creationDate)");

    List<Object[]> resultList =
        entityManager
            .createQuery(queryBuilder.toString(), Object[].class)
            .setParameter("ownerId", ownerId)
            .setParameter("type", TransactionType.SESSIONS_PAYMENT)
            .setParameter("type2", TransactionType.SESSION_EXTERNAL_PAYMENT)
            .setParameter("status", TransactionStatus.COMPLETED)
            .setParameter("from", from)
            .setParameter("to", to)
            .setParameter("parkingId", parkingId)
            .getResultList();

    HashMap<UUID, IncomesByDayTimeStats> result = new HashMap<>();
    Currency currency = Currency.USD;

    for (Object[] row : resultList) {
      UUID retrievedParkingId = (UUID) row[0];
      Integer hour = (Integer) row[1];
      BigDecimal amount = (BigDecimal) row[2];

      if (Boolean.TRUE.equals(converted)) {
        Currency systemCurrency = Currency.USD;
        currency = accountBalanceService.getOwnerCurrency(ownerId);
        amount = exchangeService.exchange(systemCurrency, currency, amount).getDestinationAmount();
      }

      result.putIfAbsent(
          retrievedParkingId,
          new IncomesByDayTimeStats(new HashMap<>(), currency, currency.getSymbol()));
      result.get(retrievedParkingId).incomes().put(hour, amount);
    }

    for (UUID key : result.keySet()) {
      IncomesByDayTimeStats stats = result.get(key);

      for (int hour = 0; hour < 24; hour++) {
        stats.incomes().putIfAbsent(hour, BigDecimal.ZERO);
      }
    }
    return result;
  }

  @Override
  public BigDecimal calculateSessionTotalAmount(UUID sessionId, Currency currency) {
    log.info("Calculating total amount for sessionId {}", sessionId);
    BigDecimal result =
        entityManager
            .createQuery(
                "SELECT SUM(t.conversionDetails.destinationAmount) "
                    + "FROM Transaction t "
                    + "WHERE (t.type = :type OR t.type = :type2) "
                    + "AND t.status = :status "
                    + "AND t.metadata.sessionId = :sessionId",
                BigDecimal.class)
            .setParameter("type", TransactionType.SESSIONS_PAYMENT)
            .setParameter("type2", TransactionType.SESSION_EXTERNAL_PAYMENT)
            .setParameter("status", TransactionStatus.COMPLETED)
            .setParameter("sessionId", sessionId)
            .getSingleResult();

    if (result == null) {
      result = BigDecimal.ZERO;
    }

    if (!currency.equals(Currency.USD)) {
      result = exchangeService.exchange(SYSTEM_CURRENCY, currency, result).getDestinationAmount();
    }

    log.info("Total amount for sessionId {}: {}", sessionId, result);
    return result;
  }

  @Override
  public HashMap<UUID, List<ParkingRevenue>> getReneview(
      UUID ownerId, UUID parkingId, Boolean converted, LocalDateTime from, LocalDateTime to) {

    StringBuilder queryBuilder =
        new StringBuilder(
            "SELECT t.metadata.parkingId, SUM(t.conversionDetails.destinationAmount), FUNCTION('DATE', t.creationDate) "
                + "FROM Transaction t "
                + "WHERE t.destinationAccount.ownerId = :ownerId "
                + "AND (t.type = :type OR t.type = :type2)"
                + "AND t.status = :status "
                + "AND t.creationDate BETWEEN :from AND :to");

    queryBuilder.append(
        " GROUP BY t.metadata.parkingId, FUNCTION('DATE', t.creationDate) ORDER BY FUNCTION('DATE', t.creationDate) DESC");

    List<Object[]> results =
        entityManager
            .createQuery(queryBuilder.toString(), Object[].class)
            .setParameter("ownerId", ownerId)
            .setParameter("type", TransactionType.SESSION_EXTERNAL_PAYMENT)
            .setParameter("type2", TransactionType.SESSIONS_PAYMENT)
            .setParameter("status", TransactionStatus.COMPLETED)
            .setParameter("from", from)
            .setParameter("to", to)
            .getResultList();

    HashMap<UUID, List<ParkingRevenue>> revenueMap = new HashMap<>();

    BigDecimal amount;
    Currency targetCurrency = SYSTEM_CURRENCY;

    for (Object[] result : results) {
      UUID retrievedParkingId = (UUID) result[0];
      amount = (BigDecimal) result[1];
      LocalDate localDate = ((LocalDate) result[2]);

      if (Boolean.TRUE.equals(converted)) {
        targetCurrency = accountBalanceService.getOwnerCurrency(ownerId);
        amount =
            exchangeService
                .exchange(SYSTEM_CURRENCY, targetCurrency, amount)
                .getDestinationAmount();
      }

      ParkingRevenue revenue =
          new ParkingRevenue(localDate, amount, targetCurrency, targetCurrency.getSymbol());

      revenueMap.computeIfAbsent(retrievedParkingId, k -> new ArrayList<>()).add(revenue);
    }

    LocalDate endDate = to.toLocalDate();

    for (UUID parkId : revenueMap.keySet()) {
      List<ParkingRevenue> dailyRevenues = revenueMap.get(parkId);

      Set<LocalDate> existingDates =
          dailyRevenues.stream().map(ParkingRevenue::date).collect(Collectors.toSet());

      LocalDate currentDate = from.toLocalDate();
      while (!currentDate.isAfter(endDate)) {
        if (!existingDates.contains(currentDate)) {
          dailyRevenues.add(
              new ParkingRevenue(
                  currentDate, BigDecimal.ZERO, targetCurrency, targetCurrency.getSymbol()));
        }
        currentDate = currentDate.plusDays(1);
      }
    }

    return revenueMap;
  }
}
