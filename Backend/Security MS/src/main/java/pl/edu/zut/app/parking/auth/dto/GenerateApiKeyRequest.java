package pl.edu.zut.app.parking.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pl.edu.zut.app.parking.auth.enums.Scope;

import java.util.HashSet;
import java.util.Set;

public record GenerateApiKeyRequest(
        @NotNull
        @NotEmpty
        @Size(min = 1, max = 255)
        Set<Scope> scopes
) {
    public GenerateApiKeyRequest {
        if (scopes != null && scopes.size() != new HashSet<>(scopes).size()) {
            throw new IllegalArgumentException("Scope list must contain only unique elements.");
        }
    }
}
