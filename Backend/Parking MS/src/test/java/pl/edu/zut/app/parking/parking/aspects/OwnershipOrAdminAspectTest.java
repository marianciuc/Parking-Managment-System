package pl.edu.zut.app.parking.parking.aspects;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.zut.app.parking.model.JwtTokenAuthentication;
import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.repositories.ParkingRepository;
import pl.edu.zut.app.parking.parking.services.ParkingService;


import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class OwnershipOrAdminAspectTest {

    @InjectMocks
    private OwnershipOrAdminAspect ownershipOrAdminAspect;

    @Mock
    private ParkingService parkingService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private ParkingRepository parkingRepository;

    @Test
    void testCheckOwnership_WhenUserNotAuthenticated_ThrowsAccessDeniedException() {
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        UUID parkingId = UUID.randomUUID();

        assertThrows(AccessDeniedException.class, () -> ownershipOrAdminAspect.checkOwnership(null, parkingId));
    }

    @Test
    void testCheckOwnership_WhenUserIsNotAdminOrOwner_ThrowsAccessDeniedException() {
        UUID parkingId = UUID.randomUUID();
        JwtTokenAuthentication authenticatedUser = new JwtTokenAuthentication(UUID.randomUUID().toString(), "HUESOS");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(parkingService.isOwner(parkingId, authenticatedUser.getId())).thenReturn(false);
        when(parkingRepository.findByIdAndRecordStatusIsNot(
                any(UUID.class), any(AbstractBaseEntity.RecordStatus.class)))
                .thenReturn(Optional.of(new Parking()));
        SecurityContextHolder.setContext(securityContext);

        assertThrows(AccessDeniedException.class, () -> ownershipOrAdminAspect.checkOwnership(null, parkingId));
    }

    @Test
    void testCheckOwnership_WhenUserIsAdmin_AllowsAccess() {
        UUID parkingId = UUID.randomUUID();
        JwtTokenAuthentication authenticatedUser = new JwtTokenAuthentication(UUID.randomUUID().toString(), "ADMIN");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(parkingRepository.findByIdAndRecordStatusIsNot(
                any(UUID.class), any(AbstractBaseEntity.RecordStatus.class)))
                .thenReturn(Optional.of(new Parking()));
        SecurityContextHolder.setContext(securityContext);

        ownershipOrAdminAspect.checkOwnership(null, parkingId);
    }

    @Test
    void testCheckOwnership_WhenUserIsOwner_AllowsAccess() {
        UUID parkingId = UUID.randomUUID();
        JwtTokenAuthentication authenticatedUser = new JwtTokenAuthentication(UUID.randomUUID().toString(), "HUESOS");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(parkingService.isOwner(parkingId, authenticatedUser.getId())).thenReturn(true);
        when(parkingRepository.findByIdAndRecordStatusIsNot(
                any(UUID.class), any(AbstractBaseEntity.RecordStatus.class)))
                .thenReturn(Optional.of(new Parking()));

        SecurityContextHolder.setContext(securityContext);

        ownershipOrAdminAspect.checkOwnership(null, parkingId);
    }
}