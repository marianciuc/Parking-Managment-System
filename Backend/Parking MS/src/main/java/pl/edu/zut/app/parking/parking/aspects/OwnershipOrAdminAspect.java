package pl.edu.zut.app.parking.parking.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.model.JwtTokenAuthentication;
import pl.edu.zut.app.parking.parking.services.ParkingService;

import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OwnershipOrAdminAspect {

    private final ParkingService parkingService;

    @Before("@annotation(pl.edu.zut.app.parking.parking.annotations.CheckOwnershipOrAdmin) && args(parkingId,..)")
    public void checkOwnership(JoinPoint joinPoint, UUID parkingId) {
        log.info("Checking ownership of the parking with id: {}", parkingId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof JwtTokenAuthentication authenticatedUser)) {
            log.info("User is not authenticated");
            throw new AccessDeniedException("User is not authenticated");
        }

        boolean isAdmin = authenticatedUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
        boolean isOwner = parkingService.isOwner(parkingId, authenticatedUser.getId());

        if (!isAdmin && !isOwner) {
            log.info("User is not the owner of the parking and is not an admin");
            throw new AccessDeniedException("User is not the owner of the parking and is not an admin");
        }

        log.info("User with id: {} is the owner of the parking with id: {}", authenticatedUser.getId(), parkingId);
    }
}
