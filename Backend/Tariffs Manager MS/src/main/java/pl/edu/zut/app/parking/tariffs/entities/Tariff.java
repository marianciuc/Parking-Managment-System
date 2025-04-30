package pl.edu.zut.app.parking.tariffs.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.edu.zut.app.parking.tariffs.enums.Currency;
import pl.edu.zut.app.parking.tariffs.enums.TariffClass;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@Entity
@Table(name = "tariffs")
public class Tariff extends AbstractBaseEntity {

    @Column(name = "parking_id", nullable = false)
    private UUID parkingId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 500, columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "tariff_class", nullable = false)
    private TariffClass tariffClass;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
}
