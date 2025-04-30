package pl.edu.zut.app.parking.parking.aspects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.zut.app.parking.model.JwtTokenAuthentication;
import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.repositories.ParkingRepository;
import pl.edu.zut.app.parking.parking.services.ParkingService;
import pl.edu.zut.app.parking.parking.services.impl.ParkingDetailsServiceImpl;

@SpringBootTest
class OwnershipAspectTest {

  @InjectMocks private OwnershipAspect ownershipAspect;

  @Mock private ParkingService parkingService;

  @Mock private SecurityContext securityContext;

  @Mock private Authentication authentication;
  @Mock private ParkingDetailsServiceImpl parkingDetailsServiceImpl;
  @Mock private ParkingRepository parkingRepository;

  @Test
  void shouldThrowAccessDeniedExceptionWhenUserIsNotAuthenticated() {
    when(securityContext.getAuthentication()).thenReturn(null);
    SecurityContextHolder.setContext(securityContext);

    UUID parkingId = UUID.randomUUID();

    assertThrows(
        AccessDeniedException.class, () -> ownershipAspect.checkOwnership(null, parkingId));
  }

  @Test
  void shouldThrowAccessDeniedExceptionWhenPrincipalIsNotAuthenticatedUser() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(new Object());
    SecurityContextHolder.setContext(securityContext);

    UUID parkingId = UUID.randomUUID();

    assertThrows(
        AccessDeniedException.class, () -> ownershipAspect.checkOwnership(null, parkingId));
  }

  @Test
  void shouldThrowAccessDeniedExceptionWhenUserIsNotOwner() {
    UUID userId = UUID.randomUUID();
    UUID parkingId = UUID.randomUUID();
    JwtTokenAuthentication authenticatedUser =
        new JwtTokenAuthentication(userId.toString(), "testUser");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(authenticatedUser);
    when(parkingRepository.findByIdAndRecordStatusIsNot(
            any(UUID.class), any(AbstractBaseEntity.RecordStatus.class)))
        .thenReturn(Optional.of(new Parking()));
    when(parkingService.isOwner(parkingId, userId)).thenReturn(false);
    SecurityContextHolder.setContext(securityContext);

    assertThrows(
        AccessDeniedException.class, () -> ownershipAspect.checkOwnership(null, parkingId));
  }

  @Test
  void shouldPassWhenUserIsOwner() {
    UUID userId = UUID.randomUUID();
    UUID parkingId = UUID.randomUUID();
    JwtTokenAuthentication authenticatedUser =
        new JwtTokenAuthentication(userId.toString(), "testUser");

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(authenticatedUser);
    when(parkingRepository.findByIdAndRecordStatusIsNot(
            any(UUID.class), any(AbstractBaseEntity.RecordStatus.class)))
        .thenReturn(Optional.of(new Parking()));
    when(parkingService.isOwner(any(UUID.class), any(UUID.class))).thenReturn(true);
    SecurityContextHolder.setContext(securityContext);

    ownershipAspect.checkOwnership(null, parkingId);

    verify(parkingService, times(1)).isOwner(parkingId, userId);
  }
}
