package pl.edu.zut.app.parking.parking.services;

import pl.edu.zut.app.parking.parking.dto.common.WhitelistDto;

import java.util.UUID;


public interface WhitelistService extends AccessListService<WhitelistDto> {
    WhitelistDto getWhitelistByVehicleIdAndParkingId(UUID vehicleId, UUID parkingId);

    Boolean check(UUID parkingId, UUID vehicleId);
}
