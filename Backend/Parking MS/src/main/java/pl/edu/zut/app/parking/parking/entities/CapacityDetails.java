package pl.edu.zut.app.parking.parking.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "capacity_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CapacityDetails extends AbstractBaseEntity {

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "occupied_spaces")
    private int occupiedSpaces;

    @Column(name = "places_for_disabled")
    private int placesForDisabled;

    @Column(name = "occupied_places_for_disabled")
    private int occupiedPlacesForDisabled;

    @Column(name = "places_for_electric_cars")
    private int placesForElectricCars;

    @Column(name = "occupied_places_for_electric_cars")
    private int occupiedPlacesForElectricCars;

    @OneToOne(mappedBy = "capacityDetails")
    private Parking parking;
}
