package pl.edu.zut.app.parking.auth.dto.req;

public record PasswordRecoveryRequest (
        String code,
        String password
) {
}
