package pl.edu.zut.app.parking.admin_ms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/** DTO for {@link pl.edu.zut.app.parking.admin_ms.entity.Administrator} */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AdministratorDto(
    UUID id,
    LocalDateTime creationDate,
    LocalDateTime modificationDate,
    String firstname,
    String lastname,
    String profileImageUrl,
    String phoneNumber,
    UUID systemUserId,
    LocalDate hireDate,
    LocalDate terminationDate,
    LocalDate dateOfBirth,
    boolean isActiveAdministrator)
    implements Serializable {

  /**
   * Maps an {@link pl.edu.zut.app.parking.admin_ms.entity.Administrator} entity to an {@code
   * AdministratorDto}.
   *
   * @param administrator the {@code Administrator} entity to be converted. It can be {@code null}.
   * @return a new instance of {@code AdministratorDto} containing the mapped data from the provided
   *     {@code Administrator} entity, or {@code null} if the input is {@code null}.
   */
  public static AdministratorDto fromEntity(
      pl.edu.zut.app.parking.admin_ms.entity.Administrator administrator) {
    if (administrator == null) return null;

    return new AdministratorDto(
        administrator.getId(),
        administrator.getCreationDate(),
        administrator.getModificationDate(),
        administrator.getFirstname(),
        administrator.getLastname(),
        administrator.getProfileImageUrl(),
        administrator.getPhoneNumber(),
        administrator.getSystemUserId(),
        administrator.getHireDate(),
        administrator.getTerminationDate(),
        administrator.getDateOfBirth(),
        administrator.isActiveAdministrator());
  }
}
