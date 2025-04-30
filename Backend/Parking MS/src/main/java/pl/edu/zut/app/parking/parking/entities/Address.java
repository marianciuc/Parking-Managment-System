package pl.edu.zut.app.parking.parking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address extends AbstractBaseEntity {

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "building_number")
    private String buildingNumber;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "parking_id", referencedColumnName = "id")
    private Parking parking;

    @Column(name = "is_belong_to_any_institution")
    private boolean isBelongToAnyInstitution;

    @Column(name = "institution_name")
    private String institutionName;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + super.getId() +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", buildingNumber='" + buildingNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", parking=" + parking.getId() +
                ", isBelongToAnyInstitution=" + isBelongToAnyInstitution +
                ", institutionName='" + institutionName + '\'' +
                '}';
    }
}
