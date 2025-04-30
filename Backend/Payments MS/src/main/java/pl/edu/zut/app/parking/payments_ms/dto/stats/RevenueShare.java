package pl.edu.zut.app.parking.payments_ms.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;

/**
 * Represents the revenue sharing details for parking locations within a specified time range. This
 * record provides information about the revenue distribution across individual parkings, total
 * revenue, time period, and related currency details.
 */
public record RevenueShare(
    HashMap<UUID, BigDecimal> revenueSharePerParking,
    BigDecimal totalRevenue,
    LocalDateTime from,
    LocalDateTime to,
    String currencySymbol,
    Currency currency) {}
