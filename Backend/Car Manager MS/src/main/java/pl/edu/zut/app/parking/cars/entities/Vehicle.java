package pl.edu.zut.app.parking.cars.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@RequiredArgsConstructor
@Table(name = "vehicles", indexes = {
        @Index(name = "plate_number_index", columnList = "plate_number", unique = true),
        @Index(name = "ownership_id_index", columnList = "ownership_id")
})
public class Vehicle extends AbstractBaseEntity{

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "color")
    private String color;

    @Column(name = "year")
    private Integer yearOfProduction;

    @Column(name = "vin")
    private String vin;

    @Column(name = "vehicle_class")
    private String vehicleClass;

    @Column(name = "plate_number", nullable = false, unique = true)
    private String plateNumber;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "has_owner", nullable = false)
    private boolean hasOwner = false;

    @OneToOne(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ownership ownership;

    public void removeOwnership() {
        if (this.ownership != null) {
            this.ownership.setVehicle(null);
            this.ownership = null;
        }
    }
}
