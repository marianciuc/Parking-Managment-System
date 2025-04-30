package pl.edu.zut.app.parking.payments_ms.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import pl.edu.zut.app.parking.payments_ms.entities.AccountBalance;
import pl.edu.zut.app.parking.payments_ms.entities.ConversionDetails;
import pl.edu.zut.app.parking.payments_ms.enums.AccountType;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;

/** DTO for {@link pl.edu.zut.app.parking.payments_ms.entities.AccountBalance} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountBalanceDto(
    UUID id,
    LocalDateTime creationDate,
    LocalDateTime modificationDate,
    Currency preferredCurrency,
    BigDecimal balance,
    UUID owenerId,
    AccountType accountType,
    String stripeAccountId,
    Boolean isVerifiedAccount)
    implements Serializable {

  /**
   * Converts an {@link AccountBalance} entity into an {@link AccountBalanceDto}.
   *
   * @param entity the {@link AccountBalance} entity to convert; can be null.
   * @return the corresponding {@link AccountBalanceDto}
   */
  public static AccountBalanceDto fromEntity(AccountBalance entity) {
    if (entity == null) {
      return null;
    }
    return new AccountBalanceDto(
        entity.getId(),
        entity.getCreationDate(),
        entity.getModificationDate(),
        entity.getPreferredCurrency(),
        entity.getBalance(),
        entity.getOwnerId(),
        entity.getAccountType(),
        entity.getStripeAccountId(),
        entity.getIsVerifiedAccount());
  }

  /**
   * Converts an {@link AccountBalance} entity into an {@link AccountBalanceDto} using the provided
   * conversion details.
   *
   * @param entity the {@link AccountBalance} entity to be converted; can be null.
   * @param conversionDetails the {@link ConversionDetails} containing details about currency
   *     conversion; can be null.
   * @return the corresponding {@link AccountBalanceDto}, or null if either {@code entity} or {@code
   *     conversionDetails} is null.
   */
  public static AccountBalanceDto fromEntity(
      AccountBalance entity, ConversionDetails conversionDetails) {
    if (entity == null || conversionDetails == null) {
      return null;
    }
    return new AccountBalanceDto(
        entity.getId(),
        entity.getCreationDate(),
        entity.getModificationDate(),
        entity.getPreferredCurrency(),
        conversionDetails.getConversionRate().multiply(entity.getBalance()),
        entity.getOwnerId(),
        entity.getAccountType(),
        entity.getStripeAccountId(),
        entity.getIsVerifiedAccount());
  }
}
