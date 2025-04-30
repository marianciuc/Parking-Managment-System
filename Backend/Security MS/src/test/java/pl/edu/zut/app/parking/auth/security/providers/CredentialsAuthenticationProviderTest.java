package pl.edu.zut.app.parking.auth.security.providers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.exceptions.AccountExpiredException;
import pl.edu.zut.app.parking.auth.exceptions.AccountNotSupportedLoginByCredentialsException;
import pl.edu.zut.app.parking.auth.exceptions.DisabledException;
import pl.edu.zut.app.parking.auth.exceptions.LockedException;

class CredentialsAuthenticationProviderTest {

  @Test
  void shouldAuthenticateWhenCredentialsAreValid() {
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    CredentialsAuthenticationProvider provider =
        new CredentialsAuthenticationProvider(userDetailsService, passwordEncoder);

    String email = "test@example.com";
    String password = "validPassword";

    User user = mock(User.class);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(user);
    when(user.isAccountNonLocked()).thenReturn(true);
    when(user.isAccountNonExpired()).thenReturn(true);
    when(user.getIsSupportedExternalAuthenticationProvider()).thenReturn(true);
    when(user.getIsSupportingLoginByCredentials()).thenReturn(true);
    when(user.isEnabled()).thenReturn(true);
    when(user.getPassword()).thenReturn("hashedPassword");
    when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(true);

    assertDoesNotThrow(
        () -> provider.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
  }

  @Test
  void shouldThrowLockedExceptionWhenAccountIsLocked() {
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    CredentialsAuthenticationProvider provider =
        new CredentialsAuthenticationProvider(userDetailsService, passwordEncoder);

    String email = "test@example.com";
    String password = "password";

    User user = mock(User.class);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(user);
    when(user.isAccountNonLocked()).thenReturn(false);

    assertThrows(
        LockedException.class,
        () -> provider.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
  }

  @Test
  void shouldThrowAccountExpiredExceptionWhenAccountIsExpired() {
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    CredentialsAuthenticationProvider provider =
        new CredentialsAuthenticationProvider(userDetailsService, passwordEncoder);

    String email = "test@example.com";
    String password = "password";

    User user = mock(User.class);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(user);
    when(user.isAccountNonLocked()).thenReturn(true);
    when(user.isAccountNonExpired()).thenReturn(false);
    when(user.getIsSupportedExternalAuthenticationProvider()).thenReturn(true);

    assertThrows(
        AccountExpiredException.class,
        () -> provider.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
  }

  @Test
  void shouldThrowDisabledExceptionWhenAccountIsDisabled() {
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    CredentialsAuthenticationProvider provider =
        new CredentialsAuthenticationProvider(userDetailsService, passwordEncoder);

    String email = "test@example.com";
    String password = "password";

    User user = mock(User.class);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(user);
    when(user.isAccountNonLocked()).thenReturn(true);
    when(user.isAccountNonExpired()).thenReturn(true);
    when(user.getIsSupportedExternalAuthenticationProvider()).thenReturn(true);
    when(user.isEnabled()).thenReturn(false);

    assertThrows(
        DisabledException.class,
        () -> provider.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
  }

  @Test
  void shouldThrowBadCredentialsExceptionWhenPasswordIsInvalid() {
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    CredentialsAuthenticationProvider provider =
        new CredentialsAuthenticationProvider(userDetailsService, passwordEncoder);

    String email = "test@example.com";
    String password = "invalidPassword";

    User user = mock(User.class);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(user);
    when(user.isAccountNonLocked()).thenReturn(true);
    when(user.isAccountNonExpired()).thenReturn(true);
    when(user.isEnabled()).thenReturn(true);
    when(user.getIsSupportedExternalAuthenticationProvider()).thenReturn(true);
    when(user.getPassword()).thenReturn("hashedPassword");
    when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(false);

    assertThrows(
        AccountNotSupportedLoginByCredentialsException.class,
        () -> provider.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
  }

  @Test
  void shouldThrowBadCredentialsExceptionWhenUserIsNotSupportedLoginByCredentials() {
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    CredentialsAuthenticationProvider provider =
        new CredentialsAuthenticationProvider(userDetailsService, passwordEncoder);

    String email = "test@example.com";
    String password = "invalidPassword";

    User user = mock(User.class);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(user);
    when(user.isAccountNonLocked()).thenReturn(true);
    when(user.isAccountNonExpired()).thenReturn(true);
    when(user.isEnabled()).thenReturn(true);
    when(user.getPassword()).thenReturn(null);
    when(user.getIsSupportedExternalAuthenticationProvider()).thenReturn(false);
    when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(false);

    assertThrows(
        AccountNotSupportedLoginByCredentialsException.class,
        () -> provider.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
  }
}
