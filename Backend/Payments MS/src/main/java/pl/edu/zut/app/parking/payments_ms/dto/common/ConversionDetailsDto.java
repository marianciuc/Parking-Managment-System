package pl.edu.zut.app.parking.payments_ms.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import pl.edu.zut.app.parking.payments_ms.entities.ConversionDetails;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;

/** DTO for {@link pl.edu.zut.app.parking.payments_ms.entities.ConversionDetails} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ConversionDetailsDto(
    BigDecimal sourceAmount,
    Currency sourceCurrency,
    BigDecimal destinationAmount,
    Currency destinationCurrency,
    BigDecimal conversionRate,
    LocalDateTime currencyDate)
    implements Serializable {

  /**
   * Converts a {@link ConversionDetails} entity into a {@link ConversionDetailsDto}.
   *
   * @param conversionDetails the {@link ConversionDetails} entity to convert; can be null.
   * @return the corresponding {@link ConversionDetailsDto} object, or null if the provided entity
   *     is null.
   */
  public static ConversionDetailsDto fromEntity(ConversionDetails conversionDetails) {
    if (conversionDetails == null) {
      return null;
    }
    return new ConversionDetailsDto(
        conversionDetails.getSourceAmount(),
        conversionDetails.getSourceCurrency(),
        conversionDetails.getDestinationAmount(),
        conversionDetails.getDestinationCurrency(),
        conversionDetails.getConversionRate(),
        conversionDetails.getCurrencyDate());
  }
}
