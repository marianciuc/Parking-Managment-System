package pl.edu.zut.app.parking.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.auth.dto.ApiKeyDto;
import pl.edu.zut.app.parking.auth.dto.ApiKeyListItem;
import pl.edu.zut.app.parking.auth.dto.GenerateApiKeyRequest;
import pl.edu.zut.app.parking.auth.entities.ApiKey;
import pl.edu.zut.app.parking.auth.services.ApiKeyService;

@Slf4j
@RestController
@RequestMapping("/api/v1/api-keys")
@RequiredArgsConstructor
public class ApiKeysController {

  private final ApiKeyService apiKeyService;

  @Operation(
      summary = "Generate an API key for a Parking",
      description =
          "Generates a new API key for the specified parking with the provided permissions (scopes). Requires ADMIN role.",
      tags = {"API Keys"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "API key successfully generated",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiKeyDto.class))),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
  })
  @PostMapping("/generate/{parkingId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiKeyDto> generateApiKey(
      @Parameter(
              description = "The UUID of the parking for which the API key will " + "be generated")
          @PathVariable
          UUID parkingId,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "The details of the API key, including its scopes",
              required = true,
              content = @Content(schema = @Schema(implementation = GenerateApiKeyRequest.class)))
          @RequestBody
          GenerateApiKeyRequest request) {
    return ResponseEntity.ok(apiKeyService.generateApiKey(parkingId, request.scopes()));
  }

  @Operation(
      summary = "Revoke an API key",
      description = "Revokes an existing API key by its unique identifier. Requires ADMIN role.",
      tags = {"API Keys"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "API key successfully revoked",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiKeyDto.class))),
    @ApiResponse(responseCode = "404", description = "API key not found", content = @Content),
    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
  })
  @PostMapping("/revoke/{keyId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiKeyDto> revokeApiKey(
      @Parameter(description = "The UUID of the API key to be revoked", required = true)
          @PathVariable
          UUID keyId) {
    return ResponseEntity.ok(apiKeyService.revokeApiKey(keyId));
  }

  @Operation(
      summary = "List all API keys",
      description =
          "Retrieves a paginated list of API keys based on query parameters such as parkingId, issuedBy, and status. Requires ADMIN role.",
      tags = {"API Keys"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "List of API keys retrieved successfully",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Page.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid query parameters",
        content = @Content),
    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
  })
  @GetMapping("/search")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Page<ApiKeyListItem>> listApiKeys(
      @Parameter(description = "Filter by parkingId (optional)")
          @RequestParam(name = "parkingId", required = false)
          UUID parkingId,
      @Parameter(description = "Filter by the UUID of the user who issued the API key (optional)")
          @RequestParam(name = "issuedBy", required = false)
          UUID issuedBy,
      @Parameter(description = "Page index for the result set (default: 0)")
          @RequestParam(name = "page", required = false, defaultValue = "0")
          Integer page,
      @Parameter(description = "Page size for the result set (default: 10)")
          @RequestParam(name = "size", required = false, defaultValue = "10")
          Integer size,
      @Parameter(description = "Filter by the status of the API key (optional)")
          @RequestParam(name = "status", required = false, defaultValue = "ACTIVE")
          ApiKey.ApiKeyStatus status) {
    log.info(
        "listApiKeys: parkingId={}, issuedBy={}, page={}, size={}, status={}",
        parkingId,
        issuedBy,
        page,
        size,
        status);
    return ResponseEntity.ok(apiKeyService.findApiKeys(parkingId, status, page, size, issuedBy));
  }

  @Operation(
      summary = "Get details of an API key",
      description =
          "Retrieves detailed information about a specific API key by its unique identifier. Requires ADMIN role.",
      tags = {"API Keys"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "API key details retrieved successfully",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiKeyDto.class))),
    @ApiResponse(responseCode = "404", description = "API key not found", content = @Content),
    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
  })
  @GetMapping("/details/{keyId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiKeyDto> getApiKeyDetails(
      @Parameter(description = "The UUID of the API key to retrieve details for", required = true)
          @PathVariable
          UUID keyId) {
    return ResponseEntity.ok(apiKeyService.getApiKeyDetails(keyId));
  }

  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "API key is valid",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiKeyDto.class))),
    @ApiResponse(
        responseCode = "404",
        description = "API key is invalid or not found",
        content = @Content)
  })
  @Operation(
      summary = "Validate an API key",
      description =
          "Validates the given API key value and returns the associated details if valid.",
      tags = {"API Keys"})
  @GetMapping("/validate/{keyValue}")
  public ResponseEntity<ApiKeyDto> validateApiKey(
      @Parameter(description = "The value of the API key to validate", required = true)
          @PathVariable
          String keyValue) {
    return ResponseEntity.ok(apiKeyService.validateApiKey(keyValue));
  }
}
