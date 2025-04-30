package pl.edu.zut.app.parking.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordRequest(
        @NotNull
        @NotEmpty
        String oldPassword,

        @NotNull
        @NotEmpty
        String newPassword
) {
}
