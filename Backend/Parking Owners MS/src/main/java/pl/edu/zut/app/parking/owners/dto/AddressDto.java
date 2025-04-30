package pl.edu.zut.app.parking.owners.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.edu.zut.app.parking.owners.entities.Address;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressDto(
        String country,
        String city,
        String street,
        String houseNumber,
        String apartmentNumber,
        String postalCode
) {
    public static AddressDto fromEntity(Address address) {
        return new AddressDto(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouseNumber(),
                address.getApartmentNumber(),
                address.getPostalCode()
        );
    }
}
