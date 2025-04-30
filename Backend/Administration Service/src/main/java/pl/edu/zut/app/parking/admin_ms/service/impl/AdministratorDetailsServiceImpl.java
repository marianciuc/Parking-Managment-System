package pl.edu.zut.app.parking.admin_ms.service.impl;

import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.admin_ms.AdministratorSpecifications;
import pl.edu.zut.app.parking.admin_ms.dto.AdministratorDto;
import pl.edu.zut.app.parking.admin_ms.dto.AdministratorUpdateRequest;
import pl.edu.zut.app.parking.admin_ms.entity.Administrator;
import pl.edu.zut.app.parking.admin_ms.exception.AdministratorAlreadyExistsException;
import pl.edu.zut.app.parking.admin_ms.exception.AdministratorNotFountException;
import pl.edu.zut.app.parking.admin_ms.repository.AdministratorRepository;
import pl.edu.zut.app.parking.admin_ms.service.AdministratorDetailsService;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdministratorDetailsServiceImpl implements AdministratorDetailsService {

  private final AdministratorRepository administratorRepository;

  private static final String DEFAULT_FIRST_NAME = "John";
  private static final String DEFAULT_LAST_NAME = "Doe";
  private static final String DEFAULT_POSITION = "Administrator";

  @Override
  public void createAdministrator(UUID userId) throws AdministratorAlreadyExistsException {
    if (administratorRepository.existsBySystemUserId(userId)) {
      log.error("Administrator with userId {} already exists", userId);
      throw new AdministratorAlreadyExistsException(
          "Administrator whith userId: " + userId + " already exists. ");
    }
    Administrator administrator =
        Administrator.builder()
            .systemUserId(userId)
            .dateOfBirth(LocalDate.EPOCH)
            .hireDate(LocalDate.now())
            .firstname(DEFAULT_FIRST_NAME)
            .lastname(DEFAULT_LAST_NAME)
            .position(DEFAULT_POSITION)
            .profileImageUrl(null)
            .phoneNumber(null)
            .isActiveAdministrator(true)
            .build();
    administratorRepository.save(administrator);
  }

  @Override
  public AdministratorDto updateAdministrator(
      UUID userId, AdministratorUpdateRequest administratorUpdateRequest)
      throws AdministratorNotFountException {
    Administrator administrator = getBySystemUserIdOrThrow(userId);

    if (administratorUpdateRequest.firstname() != null) {
      administrator.setFirstname(administratorUpdateRequest.firstname());
    }
    if (administratorUpdateRequest.lastname() != null) {
      administrator.setLastname(administratorUpdateRequest.lastname());
    }
    if (administratorUpdateRequest.phoneNumber() != null) {
      administrator.setPhoneNumber(administratorUpdateRequest.phoneNumber());
    }
    if (administratorUpdateRequest.profileImageUrl() != null) {
      administrator.setProfileImageUrl(administratorUpdateRequest.profileImageUrl());
    }
    return AdministratorDto.fromEntity(administratorRepository.save(administrator));
  }

  @Override
  public boolean administratorHasPermission(UUID userId) throws AdministratorNotFountException{
    Administrator administrator = getBySystemUserIdOrThrow(userId);
    return administrator.hasPermissionToAccess();
  }

  private Administrator getBySystemUserIdOrThrow(UUID userId) throws AdministratorNotFountException{
    return administratorRepository
        .findBySystemUserId((userId))
        .orElseThrow(() -> new AdministratorNotFountException("Administrator not found."));
  }

  @Override
  public AdministratorDto terminateAdministrator(UUID userId)
      throws AdministratorNotFountException {
    Administrator administrator = getBySystemUserIdOrThrow(userId);
    administrator.setTerminationDate(LocalDate.now());
    administrator.setActiveAdministrator(false);
    return AdministratorDto.fromEntity(administratorRepository.save(administrator));
  }

  @Override
  public Page<AdministratorDto> findAllAdministrators(
      int page, int size, String sort, String direction, String firstName, String lastName) {
    Sort.Direction directionEnum = Sort.Direction.ASC;
    if (direction != null) {
      try{
        directionEnum = Sort.Direction.valueOf(direction.toUpperCase());
      } catch (IllegalArgumentException e){
        log.error("Invalid sort direction: {}", direction);
      }
    }
    Pageable pageable = PageRequest.of(page, size).withSort(directionEnum, sort);
    return administratorRepository
        .findAll(AdministratorSpecifications.build(firstName, lastName, null), pageable)
        .map(AdministratorDto::fromEntity);
  }

  @Override
  public AdministratorDto getAdministratorDetails(UUID userId)
      throws AdministratorNotFountException {
    return AdministratorDto.fromEntity(getBySystemUserIdOrThrow(userId));
  }
}
