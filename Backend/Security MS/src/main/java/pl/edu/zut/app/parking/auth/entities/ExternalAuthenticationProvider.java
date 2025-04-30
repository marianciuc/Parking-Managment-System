package pl.edu.zut.app.parking.auth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@Table(name = "external_authentication_providers")
@Entity
public class ExternalAuthenticationProvider extends AbstractBaseEntity {

    @Column(name = "issuer", nullable = false)
    private String issuer;

    @Column(name = "subject_id", nullable = false)
    private String subjectId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public boolean matches(String issuer, String subjectId) {
        return this.getIssuer().equals(issuer) && this.getSubjectId().equals(subjectId);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) return false;
        return (subjectId == ((ExternalAuthenticationProvider) other).subjectId) && issuer.equals(((ExternalAuthenticationProvider) other).issuer);
    }
}
