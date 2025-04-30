package pl.edu.zut.app.parking.parking.dto.requests;

import pl.edu.zut.app.parking.parking.dto.common.AddressDto;

public record ParkingCreateRequest(
        String name,
        AddressDto address
) {
}
