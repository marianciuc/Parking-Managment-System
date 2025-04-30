package pl.edu.zut.app.parking.auth.dto;

public record OAuthTokenResponse (
        String access_token,
        String id_token
) {
}
