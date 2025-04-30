package pl.edu.zut.app.parking.auth.dto.req;

public record Credentials (
        String email,
        String password
) {
}
