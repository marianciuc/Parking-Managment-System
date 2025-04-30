package pl.edu.zut.app.parking.auth.services.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import pl.edu.zut.app.parking.auth.dto.common.AuthToken;
import pl.edu.zut.app.parking.auth.dto.common.AuthenticationTokens;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.exceptions.ForbiddenException;
import pl.edu.zut.app.parking.auth.factories.AccessTokenFactory;
import pl.edu.zut.app.parking.auth.factories.AuthenticationTokenFactory;
import pl.edu.zut.app.parking.auth.factories.RefreshTokenFactory;
import pl.edu.zut.app.parking.auth.factories.TokenFactory;
import pl.edu.zut.app.parking.auth.security.AuthenticatedUser;
import pl.edu.zut.app.parking.auth.serializers.*;
import pl.edu.zut.app.parking.auth.services.JWTService;
import pl.edu.zut.app.parking.auth.services.UserRepositoryService;

import java.text.ParseException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {

    private final RSAKey privateJWK;
    private final RSAKey publicJWK;
    private final JWSSigner signer;
    private final JWEEncrypter encrypter;
    private final JWEDecrypter decrypter;
    private final JWSVerifier verifier;


    private final UserRepositoryService userRepositoryService;
    private final String publicKey;
    private AccessJWETokenStringDeserializer accessTokenDeserializer;
    private AccessJWSTokenStringSerializer accessTokenSerializer;
    private RefreshJWSTokenStringDeserializer refreshTokenDeserializer;
    private RefreshJWETokenStringSerializer refreshTokenSerializer;
    private AccessTokenFactory accessTokenFactory;
    private RefreshTokenFactory refreshTokenFactory;

    public JWTServiceImpl(String privateKey, String publicKey, UserRepositoryService userRepositoryService) throws ParseException, JOSEException {
        this.privateJWK = RSAKey.parse(privateKey);
        this.publicJWK = RSAKey.parse(publicKey);
        this.publicKey = publicKey;

        this.signer = new RSASSASigner(privateJWK);
        this.encrypter = new RSAEncrypter(publicJWK);
        this.decrypter = new RSADecrypter(privateJWK);
        this.verifier = new RSASSAVerifier(publicJWK);

        initSerializersAndFactories();
        this.userRepositoryService = userRepositoryService;
    }

    private void initSerializersAndFactories() {
        this.accessTokenDeserializer = new AccessJWETokenStringDeserializer(verifier);
        this.accessTokenSerializer = new AccessJWSTokenStringSerializer(signer).algorithm(JWSAlgorithm.RS256);
        this.refreshTokenDeserializer = new RefreshJWSTokenStringDeserializer(decrypter);
        this.refreshTokenSerializer = new RefreshJWETokenStringSerializer(encrypter)
                .jweAlgorithm(JWEAlgorithm.RSA_OAEP_256)
                .encryptionMethod(EncryptionMethod.A256GCM);
        this.accessTokenFactory = new AccessTokenFactory();
        this.refreshTokenFactory = new RefreshTokenFactory();
    }

    @Override
    public AuthToken createAccessToken(AuthToken refreshAuthToken) {
        return this.accessTokenFactory.apply(refreshAuthToken);
    }

    @Override
    public AuthToken createRefreshToken(Authentication authentication) {
        return this.refreshTokenFactory.apply(authentication);
    }

    @Override
    public User getPrincipal(String token) {
        AuthToken deserializedToken = accessTokenDeserializer.apply(token);
        return this.userRepositoryService.findById(UUID.fromString(deserializedToken.subject()));
    }

    @Override
    public String getPublicKey() {
        return this.publicKey;
    }

    @Override
    public AuthToken accessTokenStringDeserialize(String token) {
        return this.accessTokenDeserializer.apply(token);
    }

    public AuthToken refreshTokenStringDeserialize(String token) {
        return this.refreshTokenDeserializer.apply(token);
    }

    @Override
    public String serializeAccessToken(AuthToken authToken) {
        return this.accessTokenSerializer.apply(authToken);
    }

    @Override
    public String serializeRefreshToken(AuthToken authToken) {
        return this.refreshTokenSerializer.apply(authToken);
    }

    @Override
    public AuthenticationTokens generateAuthenticationTokens(User user) {
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                user, user.getPassword(), user.getAuthorities()
        );
        return generateTokens(authentication);
    }

    @Override
    public AuthenticationTokens generateAuthenticationTokens(Authentication authentication) {
        return generateTokens(authentication);
    }

    @Override
    public AuthenticationTokens refreshAuthenticationTokens(Authentication authentication) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        if (authenticatedUser.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("REFRESH_TOKEN"))) {
            return generateTokens(authentication);
        }
        throw new ForbiddenException("User does not have permission to refresh tokens");
    }

    private AuthenticationTokens generateTokens(Authentication authentication) {
        AuthToken refreshAuthToken = this.createRefreshToken(authentication);
        AuthToken accessAuthToken = this.accessTokenFactory.apply(refreshAuthToken);
        return new AuthenticationTokens(
                this.serializeAccessToken(accessAuthToken),
                accessAuthToken.expiresAt().toString(),
                this.serializeRefreshToken(refreshAuthToken),
                refreshAuthToken.expiresAt().toString()
        );
    }
}
