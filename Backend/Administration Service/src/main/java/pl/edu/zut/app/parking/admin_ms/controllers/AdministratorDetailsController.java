package pl.edu.zut.app.parking.admin_ms.controllers;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.admin_ms.dto.AdministratorDto;
import pl.edu.zut.app.parking.admin_ms.dto.AdministratorUpdateRequest;
import pl.edu.zut.app.parking.admin_ms.service.AdministratorDetailsService;
import pl.edu.zut.app.parking.exception.ForbiddenException;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;

@RestController
@RequestMapping("/api/v1/administrators")
@RequiredArgsConstructor
public class AdministratorDetailsController {

  private final AdministratorDetailsService administratorDetailsService;

  @GetMapping("/permissions")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<Boolean> getPermissions() {
    UUID userId = SecurityContextUtil.extractUserIdFromSecurityContext();
    boolean hasPermission = administratorDetailsService.administratorHasPermission(userId);
    if (Boolean.FALSE.equals(hasPermission)) {
      throw new ForbiddenException("User has no permission to access this resource");
    }
    return ResponseEntity.ok().build();
  }

  @GetMapping("/details")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<AdministratorDto> getDetails() {
    UUID userId = SecurityContextUtil.extractUserIdFromSecurityContext();
    return ResponseEntity.ok(administratorDetailsService.getAdministratorDetails(userId));
  }

  @GetMapping("/{userId}/details")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<AdministratorDto> getDetails(@PathVariable UUID userId) {
    return ResponseEntity.ok(administratorDetailsService.getAdministratorDetails(userId));
  }

  @PutMapping("/{userId}/details")
  @PreAuthorize("hasAnyAuthority('UPDATE_ADMIN_PROFILE', 'ROOT_ADMIN')")
  public ResponseEntity<AdministratorDto> updateDetails(
      @PathVariable UUID userId, @RequestBody AdministratorUpdateRequest request) {
    return ResponseEntity.ok(administratorDetailsService.updateAdministrator(userId, request));
  }

  @GetMapping("/search")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<Page<AdministratorDto>> search(
      @RequestParam(required = false) UUID userId,
      @RequestParam(required = false, name = "page", defaultValue = "0") int page,
      @RequestParam(required = false, name = "size", defaultValue = "10") int size,
      @RequestParam(required = false, name = "sort", defaultValue = "id") String sort,
      @RequestParam(required = false, name = "direction", defaultValue = "ASC") String direction,
      @RequestParam String firstName,
      @RequestParam String lastName) {
    return ResponseEntity.ok(
        administratorDetailsService.findAllAdministrators(
            page, size, sort, direction, firstName, lastName));
  }

  @PutMapping("/{administratorId}/terminate")
  @PreAuthorize("hasAnyAuthority('TERMINATE_ADMIN', 'ROOT_ADMIN')")
  public ResponseEntity<AdministratorDto> terminateAdministrator(@PathVariable UUID administratorId) {
    return ResponseEntity.ok(administratorDetailsService.terminateAdministrator(administratorId));
  }
}
