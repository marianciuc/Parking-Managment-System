package pl.edu.zut.app.parking.owners.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "owners")
public class Owner extends BaseEntity{

    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private UUID userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nip", unique = true)
    private String nip;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type")
    private OwnerType ownerType;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "phone_number_code")
    private String phoneNumberCode;

    @JsonManagedReference

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Address address;

    @Column(name = "registration_completed", nullable = false)
    private boolean registrationCompleted;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public enum OwnerType {
        INDIVIDUAL,
        COMPANY
    }
}
