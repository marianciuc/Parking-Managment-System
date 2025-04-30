package pl.edu.zut.parking.app.gateway.services;

import pl.edu.zut.parking.app.gateway.dto.Token;

import java.util.Optional;


/**
 * Service interface for handling JWT (JSON Web Token) operations, such as extracting tokens
 * from HTTP headers and parsing them into {@link Token} objects.
 */
public interface JwtService {

    /**
     * Extracts a Bearer token from the provided HTTP Authorization header.
     *
     * @param header the value of the HTTP Authorization header, which is expected to contain a Bearer token.
     * @return an {@link Optional} containing the extracted token if it is present and valid; otherwise, an empty {@link Optional}.
     */
    Optional<String> extractBearerToken(String header);

    /**
     * Parses the given JWT string into a {@link Token} object.
     *
     * @param token the JSON Web Token (JWT) string to be parsed.
     * @return a {@link Token} object containing token details upon successful parsing.
     * @throws IllegalArgumentException if the token is invalid or cannot be parsed.
     */
    Token parseToken(String token);
}
