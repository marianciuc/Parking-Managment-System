package pl.edu.zut.app.parking.parking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.zut.app.parking.parking.enums.AccessType;
import pl.edu.zut.app.parking.parking.enums.ParkingStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "parking", indexes = {@jakarta.persistence.Index(name = "idx_parking_name", columnList = "name")})
public class Parking extends AbstractBaseEntity {

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "capacity_details_id")
    private CapacityDetails capacityDetails;

    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningHours> openingHours;

    @Builder.Default
    @Column(name = "is_pinned")
    private boolean isPinned = false;

    @Builder.Default
    @Column(name = "is_24h")
    private boolean is24h = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type")
    private AccessType accessType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ParkingStatus status;

    @Column(name = "rating")
    private double rating;

    @Column(name = "reviews_count")
    private int reviewsCount;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "parking_tags",
            joinColumns = @JoinColumn(name = "parking_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Blacklist> blacklists = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Whitelist> whitelists = new ArrayList<>();

}
