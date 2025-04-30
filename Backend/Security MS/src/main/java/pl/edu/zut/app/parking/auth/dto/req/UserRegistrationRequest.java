package pl.edu.zut.app.parking.auth.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pl.edu.zut.app.parking.auth.enums.Possibilities;

import java.util.List;

public record UserRegistrationRequest (
        @NotEmpty(message = "Password cannot be empty")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,

        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email
) {
}
