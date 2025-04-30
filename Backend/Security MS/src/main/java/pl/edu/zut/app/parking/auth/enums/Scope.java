package pl.edu.zut.app.parking.auth.enums;


import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum class defining the operational scopes for API keys in the parking system.
 * Each scope determines the level of permission granted.
 * {@link #ADMIN} - Can do everything.
 * {@link #PARKING} - Can do parking related operations.
 *
 */
@Getter

public enum Scope {
    ADMIN("Can do everything"), // This scope allows all operations without restrictions.
    PARKING("Can do parking related operations"); // This scope is limited to parking-related operations.

    private final String description;

    Scope(String description ) {
        this.description = description;
    }
    /**
     * Returns a list of all Scope descriptions.
     *
     * @return A list of all descriptions available in the Scope enum.
     */
    public static List<String> getAllDescriptions() {
        return Arrays.stream(values())
                     .map(Scope::getDescription)
                     .collect(Collectors.toList());
    }
}
