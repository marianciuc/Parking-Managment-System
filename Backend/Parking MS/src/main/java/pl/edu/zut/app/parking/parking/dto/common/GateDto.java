package pl.edu.zut.app.parking.parking.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.edu.zut.app.parking.parking.entities.Gate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/** DTO for {@link pl.edu.zut.app.parking.parking.entities.Gate} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GateDto(
    UUID id,
    Gate.GageType type,
    Gate.GateStatus status,
    String host,
    String name,
    int port,
    LocalDateTime modificationDate,
    boolean isManualMode)
    implements Serializable {

    /**
     * Converts a {@link Gate} entity to a {@link GateDto}.
     *
     * @param save the {@link Gate} entity to be converted. Can be null.
     * @return a {@link GateDto} representation of the provided {@link Gate} entity, or null if the input is null.
     */
    public static GateDto fromEntity(Gate save) {
        if (save == null) {
            return null;
        }
        return new GateDto(
                save.getId(),
                save.getType(),
                save.getStatus(),
                save.getHost(),
                save.getName(),
                save.getPort(),
                save.getModificationDate(),
                save.isManualMode()
        );
    }

}
