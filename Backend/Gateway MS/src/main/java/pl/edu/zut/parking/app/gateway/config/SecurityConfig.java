package pl.edu.zut.parking.app.gateway.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.zut.parking.app.gateway.clients.SecurityServiceClient;
import pl.edu.zut.parking.app.gateway.filters.JWTBlackListFilter;

import java.text.ParseException;
import java.util.Objects;

@Configuration
public class SecurityConfig {

    private final SecurityServiceClient securityServiceClient;

    public SecurityConfig(SecurityServiceClient securityServiceClient) {
        this.securityServiceClient = securityServiceClient;
    }

    @Bean
    public JWSVerifier jwsVerifier() throws ParseException, JOSEException {
        String publicKeyContent = securityServiceClient.getPublicKey();
        Objects.requireNonNull(publicKeyContent, "Public key cannot be null");

        RSAKey parsedRSAKey = RSAKey.parse(publicKeyContent);
        return new RSASSAVerifier(parsedRSAKey);
    }

    @Bean
    public HttpMessageConverters httpMessageConverters() {
        return new HttpMessageConverters();
    }

    @Bean
    public FilterRegistrationBean<JWTBlackListFilter> jwtBlackListFilterRegistration(JWTBlackListFilter jwtBlackListFilter) {
        FilterRegistrationBean<JWTBlackListFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtBlackListFilter);
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
