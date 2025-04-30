package pl.edu.zut.app.parking.auth.config;

import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.edu.zut.app.parking.auth.services.JWTService;
import pl.edu.zut.app.parking.auth.services.UserRepositoryService;
import pl.edu.zut.app.parking.auth.services.impl.JWTServiceImpl;
import pl.edu.zut.app.parking.auth.services.impl.TokenAuthenticationUserDetailsService;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationConfigurer jwtAuthenticationConfigurer) throws Exception {
    http.apply(jwtAuthenticationConfigurer);
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            config ->
                config
                    .requestMatchers(
                        "/api/v1/security/register/*",
                        "/api/v1/security/jwt/public-key",
                        "/api/v1/api-keys/validate/*",
                        "/api/v1/security/password-recovery/*",
                        "/api/oauth2/google/url",
                        "/api/oauth2/google/callback",
                        "/error")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
      TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService,
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder,
      JWTService jwtService) {
    return new JwtAuthenticationConfigurer(
        passwordEncoder, tokenAuthenticationUserDetailsService, userDetailsService, jwtService);
  }

  @Bean
  public JWTService jwtService(
      @Value("${jwt.private-key}") String privateKey,
      @Value("${jwt.public-key}") String publicKey,
      UserRepositoryService userRepositoryService)
      throws ParseException, JOSEException {
    return new JWTServiceImpl(privateKey, publicKey, userRepositoryService);
  }
}
