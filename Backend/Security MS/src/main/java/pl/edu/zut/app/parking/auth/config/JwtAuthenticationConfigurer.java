package pl.edu.zut.app.parking.auth.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.csrf.CsrfFilter;
import pl.edu.zut.app.parking.auth.filters.JWTAuthenticationFilter;
import pl.edu.zut.app.parking.auth.filters.UsernamePasswordAuthenticationFilter;
import pl.edu.zut.app.parking.auth.security.converters.JWTAuthenticationConverter;
import pl.edu.zut.app.parking.auth.security.converters.UsernamePasswordAuthenticationConverter;
import pl.edu.zut.app.parking.auth.security.providers.CredentialsAuthenticationProvider;
import pl.edu.zut.app.parking.auth.security.providers.JWTAuthenticationProvider;
import pl.edu.zut.app.parking.auth.security.utils.ProviderManager;
import pl.edu.zut.app.parking.auth.services.JWTService;

import java.util.Arrays;


public class JwtAuthenticationConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {
    private final JWTService jwtService;

    private final AuthenticationProvider credentialsAuthenticationProvider;
    private final AuthenticationProvider jwtAuthenticationProvider;

    private final AuthenticationConverter jwtAuthenticationConverter;
    private final AuthenticationConverter usernamePasswordAuthenticationConverter;

    public JwtAuthenticationConfigurer(
            PasswordEncoder passwordEncoder,
            AuthenticationUserDetailsService<Authentication> jwtUserDetailsService,
            UserDetailsService credentialsUserDetailsService,
            JWTService jwtService
    ) {
        this.jwtService = jwtService;
        this.credentialsAuthenticationProvider = new CredentialsAuthenticationProvider(credentialsUserDetailsService, passwordEncoder);
        this.jwtAuthenticationProvider = new JWTAuthenticationProvider(jwtUserDetailsService);
        this.jwtAuthenticationConverter = new JWTAuthenticationConverter(jwtService);
        this.usernamePasswordAuthenticationConverter = new UsernamePasswordAuthenticationConverter();
    }

    @Override
    public void init(HttpSecurity builder) throws Exception {

    }

    @Override
    public void configure(HttpSecurity builder) {
        AuthenticationManager authManager = new ProviderManager(Arrays.asList(credentialsAuthenticationProvider, jwtAuthenticationProvider));
        var usernamePasswordAuthenticationFilter = this.createUsernamePasswordAuthenticationFilter(authManager);
        var jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtAuthenticationConverter, authManager);

        builder
                .addFilterBefore(usernamePasswordAuthenticationFilter, CsrfFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, usernamePasswordAuthenticationFilter.getClass());
    }

    private UsernamePasswordAuthenticationFilter createUsernamePasswordAuthenticationFilter(AuthenticationManager authManager) {
        return new UsernamePasswordAuthenticationFilter(usernamePasswordAuthenticationConverter, authManager, jwtService);
    }
}
