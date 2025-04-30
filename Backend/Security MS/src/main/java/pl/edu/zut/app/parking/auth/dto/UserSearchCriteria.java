package pl.edu.zut.app.parking.auth.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import pl.edu.zut.app.parking.auth.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.auth.enums.UserType;

public record UserSearchCriteria(
        @NotNull(message = "Page number cannot be null")
        @Min(value = 0, message = "Page number must be greater than or equal to 0")
        Integer page,
        @NotNull(message = "Page size cannot be null")
        @Min(value = 1, message = "Page size must be greater than or equal to 1")
        Integer size,
        String email,
        AbstractBaseEntity.RecordStatus recordStatus,
        UserType userType
) {
}
