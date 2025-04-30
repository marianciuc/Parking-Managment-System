package pl.edu.zut.app.parking.payments_ms.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.payments_ms.dto.stats.*;
import pl.edu.zut.app.parking.payments_ms.services.StatsService;

@RestController
@RequestMapping("/api/v1/transactions/stats")
@RequiredArgsConstructor
public class TransactionsStatsController {

  private final StatsService statsService;

  @GetMapping("/account-balance/{ownerId}")
  public ResponseEntity<AccountBalanceStatsDto> getAccountBalanceStats(
      @PathVariable UUID ownerId, @RequestParam Boolean convert) {
    return ResponseEntity.ok(statsService.getAccountBalanceStats(ownerId, convert));
  }

  @GetMapping("/incomes/days")
  public ResponseEntity<IncomesByDayStats> getIncomesByDays(
      @RequestParam UUID ownerId,
      @RequestParam Boolean convert,
      @RequestParam LocalDateTime from,
      @RequestParam LocalDateTime to) {
    return ResponseEntity.ok(statsService.getIncomesByDayStats(ownerId, convert, from, to));
  }

  @GetMapping("/incomes/days/group-by-parking")
  public ResponseEntity<HashMap<UUID, IncomesByDayStats>> getIncomesByDays(
      @RequestParam UUID ownerId,
      @RequestParam Boolean convert,
      @RequestParam(required = false) UUID parkingId,
      @RequestParam LocalDateTime from,
      @RequestParam LocalDateTime to) {
    return ResponseEntity.ok(
        statsService.getIncomesByDayStatsGroupedByParking(ownerId, convert, from, to, parkingId));
  }

  @GetMapping("/revenue-share")
  public ResponseEntity<RevenueShare> getRevenueShare(
      @RequestParam UUID ownerId,
      @RequestParam Boolean convert,
      @RequestParam LocalDateTime from,
      @RequestParam LocalDateTime to) {
    return ResponseEntity.ok(statsService.getRevenueShare(ownerId, convert, from, to));
  }

  @GetMapping("/incomes/days/time")
  public ResponseEntity<IncomesByDayTimeStats> getIncomesByDaytime(
      @RequestParam UUID ownerId,
      @RequestParam Boolean convert,
      @RequestParam LocalDateTime from,
      @RequestParam LocalDateTime to) {
    return ResponseEntity.ok(statsService.getIncomesByDayTime(ownerId, convert, from, to));
  }

  @GetMapping("/incomes/days/time/group-by-parking")
  public ResponseEntity<HashMap<UUID, IncomesByDayTimeStats>> getIncomesByDayTimeGroupedByParking(
      @RequestParam UUID ownerId,
      @RequestParam Boolean convert,
      @RequestParam LocalDateTime from,
      @RequestParam LocalDateTime to,
      @RequestParam(required = false) UUID parkingId) {
    return ResponseEntity.ok(
        statsService.getIncomesByDayTimeGroupedByParking(ownerId, convert, from, to, parkingId));
  }

  @GetMapping("/revenue")
  public ResponseEntity<HashMap<UUID, List<ParkingRevenue>>> getReneview(
      @RequestParam UUID ownerId,
      @RequestParam LocalDateTime from,
      @RequestParam LocalDateTime to,
      @RequestParam Boolean convert,
      @RequestParam(required = false) UUID parkingId) {
    return ResponseEntity.ok(statsService.getReneview(ownerId, parkingId, convert, from, to));
  }
}
