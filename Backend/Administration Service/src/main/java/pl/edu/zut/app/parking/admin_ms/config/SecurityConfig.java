package pl.edu.zut.app.parking.admin_ms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import pl.edu.zut.app.parking.filter.ApiKeyFilter;
import pl.edu.zut.app.parking.filter.AuthenticationFilter;
import pl.edu.zut.app.parking.filter.LoggingFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/error", "/api/v1/account-balance/account" + "/{accountId}/currency")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(new AuthenticationFilter(), CsrfFilter.class)
        .addFilterBefore(new ApiKeyFilter(), AuthenticationFilter.class)
        .addFilterBefore(new LoggingFilter(), ApiKeyFilter.class)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
