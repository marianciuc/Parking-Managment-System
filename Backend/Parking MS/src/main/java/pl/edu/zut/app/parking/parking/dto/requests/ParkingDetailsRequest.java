package pl.edu.zut.app.parking.parking.dto.requests;

import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.enums.AccessType;
import pl.edu.zut.app.parking.parking.enums.ParkingStatus;

import java.util.List;
import java.util.UUID;

/**
 * A record used to request detailed information about a parking entity.
 * This data structure encapsulates properties such as identifiers, location details,
 * metadata, parking availability, and filtering parameters.
 */
public record ParkingDetailsRequest(
        UUID id,
        String name,
        String address,
        String city,
        String postalCode,
        String countryCode,
        List<UUID> tags,
        Double latitude,
        Double longitude,
        Double radiusInMeters,
        Boolean is24h,
        Boolean isOpen,
        Boolean isNotOccupied,
        Boolean includeDisabledPlaces,
        Boolean includeElectricPlaces,
        ParkingStatus parkingStatus,
        AbstractBaseEntity.RecordStatus recordStatus,
        AccessType accessType
) {
}
