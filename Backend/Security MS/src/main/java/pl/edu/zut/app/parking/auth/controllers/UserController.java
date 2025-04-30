package pl.edu.zut.app.parking.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.auth.dto.ChangePasswordRequest;
import pl.edu.zut.app.parking.auth.dto.UserDto;
import pl.edu.zut.app.parking.auth.dto.UserSearchCriteria;
import pl.edu.zut.app.parking.auth.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.auth.enums.UserType;
import pl.edu.zut.app.parking.auth.services.UserService;

import java.util.UUID;

@Tag(name = "User Management", description = "APIs for managing users including search, get, block, and unblock operations")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Search users by criteria",
            description = "Retrieve a paginated list of users based on optional search criteria such as email, page, size, record status, and user type.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid search criteria", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/search")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @Parameter(name = "email", description = "Email address to filter users", in = ParameterIn.QUERY)
            @RequestParam(name = "email", required = false) String email,
            @Parameter(name = "page", description = "Page number for pagination (default is 0)", schema = @Schema(defaultValue = "0"), in = ParameterIn.QUERY)
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @Parameter(name = "size", description = "Page size for pagination (default is 10)", schema = @Schema(defaultValue = "10"), in = ParameterIn.QUERY)
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @Parameter(name = "recordStatus", description = "Status of the record (e.g., ACTIVE, DELETED, BANNED)", in = ParameterIn.QUERY)
            @RequestParam(name = "recordStatus", required = false) AbstractBaseEntity.RecordStatus recordStatus,
            @Parameter(name = "userType", description = "Type of the user (e.g., DRIVER, PARKING_OWNER, ADMIN)", in = ParameterIn.QUERY)
            @RequestParam(name = "userType", required = false) UserType userType
    ) {
        UserSearchCriteria searchCriteria = new UserSearchCriteria(page, size, email, recordStatus, userType);
        return ResponseEntity.ok(userService.searchUsers(searchCriteria));
    }

    @Operation(
            summary = "Get user details",
            description = "Retrieve details of a user by their unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the user's details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@Parameter(name = "userId", description = "Unique identifier of the user",
            required = true, in = ParameterIn.PATH) @PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @Operation(
            summary = "Block a user",
            description = "Block a user by their unique ID. This operation requires ADMIN privileges.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully blocked the user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{userId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> blockUser(
            @Parameter(name = "userId", description = "Unique identifier of the user to be blocked", required = true, in = ParameterIn.PATH)
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(userService.ban(userId));
    }

    @Operation(
            summary = "Unblock a user",
            description = "Unblock a user by their unique ID. This operation requires ADMIN privileges.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully unblocked the user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content)
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{userId}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> unblockUser(
            @Parameter(name = "userId", description = "Unique identifier of the user to be unblocked", required = true, in = ParameterIn.PATH)
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(userService.unban(userId));
    }

    @PutMapping
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}
