package pl.edu.zut.app.parking.owners.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.edu.zut.app.parking.owners.entities.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ParkingOwnerDto(
        UUID id,
        String firstName,
        String middleName,
        String lastName,
        UUID userId,
        String NIP,
        String phoneNumber,
        String phoneNumberCode,
        AddressDto address,
        boolean isRegistrationCompleted,
        LocalDate dateOfBirth,
        LocalDateTime creationDate,
        LocalDateTime modificationDate
) {
    public static ParkingOwnerDto fromEntity(Owner ownerById) {
        return new ParkingOwnerDto(
                ownerById.getId(),
                ownerById.getFirstName(),
                ownerById.getMiddleName(),
                ownerById.getLastName(),
                ownerById.getUserId(),
                ownerById.getNip(),
                ownerById.getPhoneNumber(),
                ownerById.getPhoneNumberCode(),
                ownerById.getAddress() != null ? AddressDto.fromEntity(ownerById.getAddress()): null,
                ownerById.isRegistrationCompleted(),
                ownerById.getDateOfBirth(),
                ownerById.getCreationDate(),
                ownerById.getModificationDate()
        );
    }
}
