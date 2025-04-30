package pl.edu.zut.app.parking.payments_ms.dto.stats;

import pl.edu.zut.app.parking.payments_ms.entities.AccountBalanceStats;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Represents statistical details regarding a bank account's financial data.
 * The record contains information about the current balance, blocked amount,
 * and the total withdrawn amount.
 */
public record AccountBalanceStatsDto(
        BigDecimal currentBalance,
        BigDecimal blockedAmount,
        BigDecimal withdrawnAmount,
        String currencySymbol,
        Currency currency
) {
    public static AccountBalanceStatsDto fromEntity(AccountBalanceStats accountBalanceStats, Currency currency) {
        return new AccountBalanceStatsDto(
                accountBalanceStats.getCurrentBalance(),
                accountBalanceStats.getBlockedAmount(),
                accountBalanceStats.getWithdrawnAmount(),
                currency.getSymbolKey(),
                currency
        );
    }
}
