package pl.edu.zut.app.parking.payments_ms.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import pl.edu.zut.app.parking.payments_ms.entities.TransactionMetadata;

/** DTO for {@link pl.edu.zut.app.parking.payments_ms.entities.TransactionMetadata} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionMetadataDto(
    String stripePaymentId, UUID subscriptionOrderId, UUID sessionId, UUID parkingId)
    implements Serializable {

  /**
   * Converts a {@link TransactionMetadata} entity to a {@link TransactionMetadataDto}.
   *
   * @param metadata the {@link TransactionMetadata} entity to convert; can be null.
   */
  public static TransactionMetadataDto fromEntity(TransactionMetadata metadata) {
    if (metadata == null) {
      return null;
    }
    return new TransactionMetadataDto(
        metadata.getStripePaymentId(),
        metadata.getSubscriptionOrderId(),
        metadata.getSessionId(),
        metadata.getParkingId());
  }
}
