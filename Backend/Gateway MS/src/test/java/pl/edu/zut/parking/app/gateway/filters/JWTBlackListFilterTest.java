package pl.edu.zut.parking.app.gateway.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.zut.parking.app.gateway.services.JwtService;
import pl.edu.zut.parking.app.gateway.services.TokenBlackListService;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JWTBlackListFilterTest {

    @Mock
    private TokenBlackListService tokenBlackListService;

    @Mock
    private JwtService jwtService;

    @Test
    void shouldAllowRequestWhenTokenIsNotPresent() throws Exception {
        JWTBlackListFilter filter = new JWTBlackListFilter(tokenBlackListService, jwtService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldBlockRequestWhenTokenIsInBlackList() throws Exception {
        JWTBlackListFilter filter = new JWTBlackListFilter(tokenBlackListService, jwtService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String token = "blacklistedToken";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractBearerToken("Bearer " + token)).thenReturn(Optional.of(token));
        when(tokenBlackListService.isTokenBlocked(token)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void shouldAllowRequestWhenTokenIsNotInBlackList() throws Exception {
        JWTBlackListFilter filter = new JWTBlackListFilter(tokenBlackListService, jwtService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String token = "validToken";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractBearerToken("Bearer " + token)).thenReturn(Optional.of(token));
        when(tokenBlackListService.isTokenBlocked(token)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}