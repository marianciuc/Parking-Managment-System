package pl.edu.zut.app.parking.auth.enums;


import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Enum class for user possibilities
 */
@Getter
public enum Possibilities {
    ADMIN("Can do everything", List.of(UserType.ADMIN)),
    LOGIN_IN_ADMIN_PANEL("Can login in admin panel", List.of(UserType.ADMIN)),
    LOGIN_IN_DRIVER_PANEL("Can login in customer panel", List.of(UserType.DRIVER)),
    LOGIN_IN_ORGANIZATION_PANEL("Can login in organization panel", List.of(UserType.ORGANIZATION_PERSONAL)),
    LOGIN_IN_PARKING_PANEL("Can login in parking panel", List.of(UserType.PARKING_OWNER)),
    CREATE_PARKING("Can create parking", List.of(UserType.PARKING_OWNER)),
    DELETE_PARKING("Can delete parking", List.of(UserType.ADMIN, UserType.ORGANIZATION_PERSONAL)),
    UPDATE_PARKING("Can update parking", List.of(UserType.PARKING_OWNER, UserType.ADMIN)),
    PARKING_DELETE_REQUEST("Can request parking deletion", List.of(UserType.PARKING_OWNER)),
    CREATE_PARKING_TAG("Can create parking tag", List.of(UserType.ADMIN, UserType.ORGANIZATION_PERSONAL)),
    DELETE_PARKING_TAG("Can delete parking tag", List.of(UserType.ADMIN, UserType.ORGANIZATION_PERSONAL)),
    UPDATE_PARKING_TAG("Can update parking tag", List.of(UserType.ADMIN, UserType.ORGANIZATION_PERSONAL));

    private final String description;
    private final List<UserType> userTypes;

    Possibilities( String description, List<UserType> userTypes ) {
        this.description = description;
        this.userTypes = userTypes;
    }

    /**
     * Retrieves all possibilities available for the given {@link UserType}.
     *
     * @param userType The users type to filter possibilities.
     * @return A set of possibilities applicable for the user type.
     */
    public static EnumSet<Possibilities> getPossibilitiesByUserType(UserType userType) {
        if (userType == null) return EnumSet.noneOf(Possibilities.class);

        return Arrays.stream(Possibilities.values())
                .filter(possibility -> possibility.userTypes.contains(userType))
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Possibilities.class)));
    }

    public SimpleGrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }
}