package pl.edu.zut.app.parking.payments_ms.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import pl.edu.zut.app.parking.payments_ms.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.payments_ms.entities.BankAccount;

/** DTO for {@link pl.edu.zut.app.parking.payments_ms.entities.BankAccount} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BankAccountDto(
    UUID id,
    LocalDateTime creationDate,
    LocalDateTime modificationDate,
    AbstractBaseEntity.RecordStatus recordStatus,
    String iban,
    String fullname,
    String bankName,
    String country)
    implements Serializable {

  /**
   * Converts a {@link BankAccount} entity into a {@link BankAccountDto}.
   *
   * @param entity the {@link BankAccount} entity to be converted; can be null.
   * @return the corresponding {@link BankAccountDto} object, or null if the provided entity is
   *     null.
   */
  public static BankAccountDto fromEntity(BankAccount entity) {
    if (entity == null) return null;
    return new BankAccountDto(
        entity.getId(),
        entity.getCreationDate(),
        entity.getModificationDate(),
        entity.getRecordStatus(),
        entity.getIban(),
        entity.getFullname(),
        entity.getBankName(),
        entity.getCountry());
  }
}
