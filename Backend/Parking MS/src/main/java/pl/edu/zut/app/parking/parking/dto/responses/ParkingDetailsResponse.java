package pl.edu.zut.app.parking.parking.dto.responses;

import pl.edu.zut.app.parking.parking.dto.common.AddressDto;
import pl.edu.zut.app.parking.parking.dto.common.OpeningHoursDto;
import pl.edu.zut.app.parking.parking.dto.common.ParkingCapacityDto;
import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.enums.ParkingStatus;

import java.util.List;
import java.util.UUID;

public record ParkingDetailsResponse(
        UUID id,
        String name,
        String imageUrl,
        AddressDto address,
        String parkingStatus,
        String description,
        Boolean is24h,
        ParkingCapacityDto capacity,
        boolean isPinned,
        double rating,
        String recordStatus,
        UUID ownerId,
        List<OpeningHoursDto> openingHours
) {
    public static ParkingDetailsResponse fromEntity(Parking parking) {
        if (parking == null) {
            return null;
        }
        return new ParkingDetailsResponse(
                parking.getId(),
                parking.getName(),
                parking.getImageUrl(),
                AddressDto.fromEntity(parking.getAddress()),
                parking.getStatus().name(),
                parking.getDescription(),
                parking.is24h(),
                ParkingCapacityDto.fromEntity(parking.getCapacityDetails()),
                parking.isPinned(),
                parking.getRating(),
                parking.getRecordStatus().name(),
                parking.getOwnerId(),
                parking.getOpeningHours() == null ? List.of() :
                        parking.getOpeningHours().stream().map(OpeningHoursDto::fromEntity).toList()
        );
    }

    public static ParkingDetailsResponse fromEntity(Parking parking, AddressDto update) {
        return new ParkingDetailsResponse(
                parking.getId(),
                parking.getName(),
                parking.getImageUrl(),
                update,
                parking.getStatus().name(),
                parking.getDescription(),
                parking.is24h(),
                ParkingCapacityDto.fromEntity(parking.getCapacityDetails()),
                parking.isPinned(),
                parking.getRating(),
                parking.getRecordStatus().name(),
                parking.getOwnerId(),
                parking.getOpeningHours() == null ? List.of() :
                        parking.getOpeningHours().stream().map(OpeningHoursDto::fromEntity).toList()
        );
    }
}
