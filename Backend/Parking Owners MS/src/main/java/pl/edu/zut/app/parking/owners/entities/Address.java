package pl.edu.zut.app.parking.owners.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "addresses")
public class Address extends BaseEntity{

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "street", length = 200)
    private String street;

    @Column(name = "house_number", length = 20)
    private String houseNumber;

    @Column(name = "apartment_number", length = 20)
    private String apartmentNumber;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "additional_info", columnDefinition = "TEXT")
    private String additionalInfo;

    @JsonBackReference
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private Owner owner;
}
