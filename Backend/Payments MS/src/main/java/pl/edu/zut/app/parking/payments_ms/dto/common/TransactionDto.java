package pl.edu.zut.app.parking.payments_ms.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import pl.edu.zut.app.parking.payments_ms.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.payments_ms.entities.ConversionDetails;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionStatus;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionType;

/** DTO for {@link pl.edu.zut.app.parking.payments_ms.entities.Transaction} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionDto(
    UUID id,
    LocalDateTime creationDate,
    LocalDateTime modificationDate,
    AbstractBaseEntity.RecordStatus recordStatus,
    AccountBalanceDto sourceAccount,
    AccountBalanceDto destinationAccount,
    ConversionDetailsDto conversionDetails,
    TransactionStatus status,
    TransactionType type,
    TransactionMetadataDto metadata,
    BankAccountDto bankAccount)
    implements Serializable {

    /**
     * Converts a {@link pl.edu.zut.app.parking.payments_ms.entities.Transaction} entity into a
     * {@link TransactionDto}.
     *
     * @param entity the {@link pl.edu.zut.app.parking.payments_ms.entities.Transaction} entity
     *               to convert; can be null.
     * @return the corresponding {@link TransactionDto} object, or null if the provided entity is null.
     */
    public static TransactionDto fromEntity(pl.edu.zut.app.parking.payments_ms.entities.Transaction entity) {
        if (entity == null) return null;
        return new TransactionDto(
                entity.getId(),
                entity.getCreationDate(),
                entity.getModificationDate(),
                entity.getRecordStatus(),
                AccountBalanceDto.fromEntity(entity.getSourceAccount()),
                AccountBalanceDto.fromEntity(entity.getDestinationAccount()),
                ConversionDetailsDto.fromEntity(entity.getConversionDetails()),
                entity.getStatus(),
                entity.getType(),
                TransactionMetadataDto.fromEntity(entity.getMetadata()),
                BankAccountDto.fromEntity(entity.getBankAccount())
        );
    }

    public static TransactionDto fromEntity(pl.edu.zut.app.parking.payments_ms.entities.Transaction entity,
                                            ConversionDetails conversionDetails) {
        if (entity == null) return null;
        return new TransactionDto(entity.getId(),
                entity.getCreationDate(),
                entity.getModificationDate(),
                entity.getRecordStatus(),
                AccountBalanceDto.fromEntity(entity.getSourceAccount()),
                AccountBalanceDto.fromEntity(entity.getDestinationAccount()),
                ConversionDetailsDto.fromEntity(conversionDetails),
                entity.getStatus(),
                entity.getType(),
                TransactionMetadataDto.fromEntity(entity.getMetadata()),
                BankAccountDto.fromEntity(entity.getBankAccount()));
    }
}
