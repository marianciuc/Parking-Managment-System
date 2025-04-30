package pl.edu.zut.app.parking.parking.dto.common;

import pl.edu.zut.app.parking.parking.entities.Tag;

import java.time.LocalDateTime;
import java.util.UUID;

public record ParkingTagDto(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ParkingTagDto fromEntity(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Tag cannot be null");
        }
        return new ParkingTagDto(
                tag.getId(),
                tag.getName(),
                tag.getDescription(),
                tag.getCreationDate(),
                tag.getModificationDate()
        );
    }
}
