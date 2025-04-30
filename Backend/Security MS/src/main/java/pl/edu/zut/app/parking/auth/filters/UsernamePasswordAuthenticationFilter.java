/*
 * Copyright (c) 2024  Vladimir Marianciuc. All Rights Reserved.
 *
 * Project: STREAMING SERVICE APP
 * File: UsernamePasswordAuthenticationFilter.java
 *
 */

package pl.edu.zut.app.parking.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.edu.zut.app.parking.auth.dto.common.AuthenticationTokens;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.services.JWTService;

import java.io.IOException;

/**
 * Filter for processing username and password authentication requests.
 */
@Setter
@Slf4j
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationConverter authenticationConverter;
    private final JWTService jwtService;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a UsernamePasswordAuthenticationFilter with the provided parameters.
     *
     * @param authenticationConverter the converter to extract authentication from the request.
     * @param authenticationManager   the manager to handle the authentication process.
     * @param jwtService              the service to generate tokens.
     */
    public UsernamePasswordAuthenticationFilter(AuthenticationConverter authenticationConverter,
                                                AuthenticationManager authenticationManager, JWTService jwtService) {
        super(new AntPathRequestMatcher("/api/v1/security/login", HttpMethod.POST.name()));
        this.authenticationConverter = authenticationConverter;
        this.jwtService = jwtService;
        setAuthenticationManager(authenticationManager);
    }

    /**
     * Attempts to authenticate the user by extracting credentials from the request.
     *
     * @param request  the HTTP request object.
     * @param response the HTTP response object.
     * @return the Authentication object if successful.
     * @throws AuthenticationException if authentication attempt fails.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        Authentication authRequest = authenticationConverter.convert(request);
        if (authRequest == null) {
            throw new AuthenticationException("Failed to convert authentication request") {
            };
        }
        return getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * Handles successful authentication by creating and returning token pairs.
     *
     * @param request    the HTTP request object.
     * @param response   the HTTP response object.
     * @param chain      the filter chain.
     * @param authResult the result of the authentication process.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        AuthenticationTokens authenticationTokens = jwtService.generateAuthenticationTokens((User) authResult.getPrincipal());
        String jsonTokenPair = objectMapper.writeValueAsString(authenticationTokens);

        response.setContentType("application/json");
        response.getWriter().write(jsonTokenPair);
    }
}
