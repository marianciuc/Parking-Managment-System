package pl.edu.zut.app.parking.auth.security.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import pl.edu.zut.app.parking.auth.security.AuthenticatedUserDetails;

class SecurityUtilsTest {

  /**
   * Unit tests for the SecurityUtils class.
   *
   * The extractUser() method extracts and returns the authenticated user details from the
   * security context. If the authentication is either not an instance of
   * PreAuthenticatedAuthenticationToken or does not contain a valid AuthenticatedUserDetails
   * principal, the method throws AccessDeniedException.
   */
  @Test
  void testExtractUserWithValidAuthenticatedUserDetails() {
    // Arrange
    UUID testUserId = UUID.randomUUID();
    AuthenticatedUserDetails userDetails = Mockito.mock(AuthenticatedUserDetails.class);
    when(userDetails.getId()).thenReturn(testUserId);

    Authentication authentication = new PreAuthenticatedAuthenticationToken(userDetails, null);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    // Act
    AuthenticatedUserDetails result = SecurityUtils.extractUser();

    // Assert
    assertNotNull(result);
    assertEquals(testUserId, result.getId());
    verify(securityContext).getAuthentication();
  }

  @Test
  void testExtractUserWithInvalidAuthenticationType() {
    // Arrange
    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    // Act & Assert
    assertThrows(AccessDeniedException.class, SecurityUtils::extractUser);
    verify(securityContext).getAuthentication();
  }

  @Test
  void testExtractUserWithInvalidPrincipal() {
    // Arrange
    Authentication authentication =
        new PreAuthenticatedAuthenticationToken("InvalidPrincipal", null);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    // Act & Assert
    assertThrows(AccessDeniedException.class, SecurityUtils::extractUser);
    verify(securityContext).getAuthentication();
  }

  @Test
  void testExtractUserWithNoAuthentication() {
    // Arrange
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(null);
    SecurityContextHolder.setContext(securityContext);

    // Act & Assert
    assertThrows(AccessDeniedException.class, SecurityUtils::extractUser);
    verify(securityContext).getAuthentication();
  }
}
