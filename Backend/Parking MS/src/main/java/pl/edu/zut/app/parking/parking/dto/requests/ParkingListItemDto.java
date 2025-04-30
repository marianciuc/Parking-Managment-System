package pl.edu.zut.app.parking.parking.dto.requests;

import pl.edu.zut.app.parking.parking.dto.common.AddressDto;
import pl.edu.zut.app.parking.parking.dto.common.OpeningHoursDto;
import pl.edu.zut.app.parking.parking.dto.common.ParkingTagDto;
import pl.edu.zut.app.parking.parking.entities.Parking;

import java.util.List;
import java.util.UUID;

public record ParkingListItemDto(
        UUID id,
        String name,
        String imageUrl,
        AddressDto address,
        Integer capacity,
        Integer occupiedSlots,
        Double rating,
        String recordStatus,
        String parkingStatus,
        Boolean is24h,
        List<OpeningHoursDto> workingTimeList,
        List<ParkingTagDto> tags
) {
    public static ParkingListItemDto fromEntity(Parking parking) {
        return new ParkingListItemDto(
                parking.getId(),
                parking.getName(),
                parking.getImageUrl(),
                AddressDto.fromEntity(parking.getAddress()),
                parking.getCapacityDetails().getCapacity(),
                parking.getCapacityDetails().getOccupiedSpaces(),
                parking.getRating(),
                parking.getRecordStatus().name(),
                parking.getStatus().name(),
                parking.is24h(),
                parking.is24h() ? null : parking.getOpeningHours().stream().map(OpeningHoursDto::fromEntity).toList(),
                parking.getTags().stream().map(ParkingTagDto::fromEntity).toList()
        );
    }
}
