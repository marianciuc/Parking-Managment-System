package pl.edu.zut.app.parking.parking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "blacklist")
public class Blacklist extends AbstractVehicleList {

    @Column(name = "reason")
    private String reason;
}
