package pl.edu.zut.app.parking.auth.security.providers;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.exceptions.AccountExpiredException;
import pl.edu.zut.app.parking.auth.exceptions.AccountNotSupportedLoginByCredentialsException;
import pl.edu.zut.app.parking.auth.exceptions.DisabledException;
import pl.edu.zut.app.parking.auth.exceptions.LockedException;

/**
 * Implementation of {@link AuthenticationProvider} that validates username and password
 * credentials.
 */
public class CredentialsAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  /**
   * Constructs a CredentialsAuthenticationProvider with the provided {@link UserDetailsService} and
   * {@link PasswordEncoder}.
   *
   * @param userDetailsService the service to load user details.
   * @param passwordEncoder the encoder to validate passwords.
   */
  public CredentialsAuthenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Attempts to authenticate a user based on provided authentication credentials. This method
   * validates the user's email and password, checks the account's status, and grants the user the
   * appropriate authorities if the credentials are correct.
   *
   * @param authentication the authentication request object containing the user's credentials, such
   *     as email and password.
   * @return an authenticated {@link UsernamePasswordAuthenticationToken} containing user details
   *     and granted authorities if the authentication process is successful.
   * @throws LockedException if the user's account is locked.
   * @throws AccountExpiredException if the user's account is expired.
   * @throws DisabledException if the user's account is disabled.
   * @throws BadCredentialsException if the provided credentials are invalid.
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = authentication.getName();
    String password = authentication.getCredentials().toString();

    User userDetails = (User) userDetailsService.loadUserByUsername(email);

    if (!userDetails.isAccountNonLocked()) {
      throw new LockedException("Account is locked");
    }
    if (!userDetails.isAccountNonExpired()) {
      throw new AccountExpiredException("Account is expired");
    }
    if (!userDetails.isEnabled()) {
      throw new DisabledException("Account is disabled");
    }

    if (Boolean.FALSE.equals(userDetails.getIsSupportingLoginByCredentials())) {
      throw new AccountNotSupportedLoginByCredentialsException();
    }

    if (passwordEncoder.matches(password, userDetails.getPassword())) {
      return new UsernamePasswordAuthenticationToken(
          userDetails, password, userDetails.getAuthorities());
    } else {
      throw new BadCredentialsException("Invalid username or password");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
