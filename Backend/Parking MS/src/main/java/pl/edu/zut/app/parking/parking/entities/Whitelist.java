package pl.edu.zut.app.parking.parking.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "whitelist")
public class Whitelist extends AbstractVehicleList {
    @Column(name = "tariff_id")
    private UUID tariffId;

    @Column(name = "tariff_name")
    private String tariffName;
}

