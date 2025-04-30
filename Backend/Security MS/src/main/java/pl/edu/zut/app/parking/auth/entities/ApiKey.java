package pl.edu.zut.app.parking.auth.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.edu.zut.app.parking.auth.enums.Scope;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@SuperBuilder
@RequiredArgsConstructor
@Table(name = "api_keys")
public class ApiKey extends AbstractBaseEntity {

    @Column(name = "key_value", nullable = false, unique = true, length = 255)
    private String keyValue;

    @Column(name = "parking_id", nullable = false)
    private UUID parkingId;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "api_key_scopes", joinColumns = @JoinColumn(name = "key_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    private Set<Scope> scope = EnumSet.noneOf(Scope.class);

    @Column(name = "issued_by", nullable = true)
    private UUID issuedBy;

    @Column(name = "revoked_by", nullable = true)
    private UUID revokedBy;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ApiKeyStatus status = ApiKeyStatus.ACTIVE;

    public enum ApiKeyStatus {
        ACTIVE,
        INACTIVE,
        REVOKED
    }

    public void revoke(UUID revokedByUserId) {
        this.revokedBy = revokedByUserId;
        this.status = ApiKeyStatus.REVOKED;
    }

    public void activate() {
        this.status = ApiKeyStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == ApiKeyStatus.ACTIVE;
    }
}
