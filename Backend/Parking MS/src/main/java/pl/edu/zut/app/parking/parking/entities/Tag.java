package pl.edu.zut.app.parking.parking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@SuperBuilder
@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tags")
public class Tag extends AbstractBaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "tags")
    private List<Parking> parkingList = new ArrayList<>();
}
