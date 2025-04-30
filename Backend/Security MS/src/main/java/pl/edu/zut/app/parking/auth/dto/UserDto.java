package pl.edu.zut.app.parking.auth.dto;

import pl.edu.zut.app.parking.auth.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.enums.UserType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        LocalDateTime creationDate,
        LocalDateTime lastModifiedDate,
        AbstractBaseEntity.RecordStatus recordStatus,
        List<String> possibility,
        UserType userType
) {
    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getCreationDate(),
                user.getModificationDate(),
                user.getRecordStatus(),
                user.getUserPossibilities().stream().map(Enum::name).toList(),
                user.getUserType()
        );
    }
}
