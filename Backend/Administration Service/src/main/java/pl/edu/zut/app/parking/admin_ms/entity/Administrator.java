package pl.edu.zut.app.parking.admin_ms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@SuperBuilder
@Table(name = "administrators")
public class Administrator extends AbstractBaseEntity {

  @Column(name = "firstname")
  private String firstname;

  @Column(name = "lastname")
  private String lastname;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @Column(name = "phone_numebr")
  private String phoneNumber;

  @Column(name = "position")
  private String position;

  @Column(name = "hire_date")
  private LocalDate hireDate;

  @Column(name = "termination_date")
  private LocalDate terminationDate;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "system_user_id")
  private UUID systemUserId;

  @Column(name = "is_active_administrator")
  private boolean isActiveAdministrator;

  /**
   * Checks whether the administrator has permission to access the system. The administrator must be
   * active and should not have a termination date set.
   *
   * @return {@code true} if the administrator is active and has no termination date, {@code false}
   *     otherwise.
   */
  public boolean hasPermissionToAccess() {
    return isActiveAdministrator && terminationDate == null;
  }
}
