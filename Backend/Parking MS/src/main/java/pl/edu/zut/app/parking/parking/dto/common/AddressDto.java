package pl.edu.zut.app.parking.parking.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.entities.Address;

import java.time.LocalDateTime;
import java.util.UUID;


public record AddressDto(
        UUID id,

        @NotBlank(message = "Country code must not be blank")
        @Size(min = 2, max = 3, message = "Country code must be between 2 and 3 characters")
        String countryCode,

        @NotBlank(message = "City must not be blank")
        @Size(max = 100, message = "City name must not exceed 100 characters")
        String city,

        @NotBlank(message = "Postal code must not be blank")
        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        @NotBlank(message = "Street must not be blank")
        @Size(max = 150, message = "Street name must not exceed 150 characters")
        String street,

        @NotBlank(message = "Building number must not be blank")
        @Size(max = 20, message = "Building number must not exceed 20 characters")
        String buildingNumber,

        @NotNull(message = "Latitude must not be null")
        Double latitude,

        @NotNull(message = "Longitude must not be null")
        Double longitude,

        @NotNull(message = "isBelongToAnyInstitution must not be null")
        Boolean isBelongToAnyInstitution,

        @Size(max = 100, message = "Institution name must not exceed 100 characters")
        String institutionName,

        AbstractBaseEntity.RecordStatus recordStatus,
        LocalDateTime creationDate,
        LocalDateTime modificationDate
) {
    public static AddressDto fromEntity(Address address) {
        return new AddressDto(
                address.getId(),
                address.getCountryCode(),
                address.getCity(),
                address.getPostalCode(),
                address.getStreet(),
                address.getBuildingNumber(),
                address.getLatitude(),
                address.getLongitude(),
                address.isBelongToAnyInstitution(),
                address.getInstitutionName(),
                address.getRecordStatus(),
                address.getCreationDate(),
                address.getModificationDate()
        );
    }
}
