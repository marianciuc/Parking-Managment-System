package pl.edu.zut.app.parking.auth.services;

import org.springframework.data.domain.Page;
import pl.edu.zut.app.parking.auth.dto.ApiKeyDto;
import pl.edu.zut.app.parking.auth.dto.ApiKeyListItem;
import pl.edu.zut.app.parking.auth.entities.ApiKey;
import pl.edu.zut.app.parking.auth.enums.Scope;

import java.util.Set;
import java.util.UUID;

/**
 * ApiKeyService is an interface responsible for managing API keys. It provides methods
 * for generating, revoking, retrieving, and validating API keys, as well as listing API keys
 * based on specific criteria.
 */
public interface ApiKeyService {

    /**
     * Generates a new API key for a specific parking entity with the specified set of scopes.
     *
     * @param parkingId the unique identifier of the parking entity for which the API key is generated
     * @param scopes    the set of scopes assigned to the API key
     * @return the generated API key dto
     * @throws pl.edu.zut.app.parking.auth.exceptions.AuthenticationException if the user is not authenticated
     * @throws pl.edu.zut.app.parking.auth.exceptions.BusinessException if the parking does not exist
     */
    ApiKeyDto generateApiKey(UUID parkingId, Set<Scope> scopes);

    /**
     * Revokes an existing API key with the specified identifier.
     *
     * @param apiKeyId the unique identifier of the API key to be revoked
     * @return the revoked API key dto
     * @throws pl.edu.zut.app.parking.auth.exceptions.BusinessException if the API key does not exist
     * @throws pl.edu.zut.app.parking.auth.exceptions.AuthenticationException if the user is not authenticated
     */
    ApiKeyDto revokeApiKey(UUID apiKeyId);

    /**
     * Retrieves a paginated list of API keys that match the provided criteria.
     *
     * @param parkingId the unique identifier of the parking entity whose API keys are being queried
     * @param status the status of the API keys to filter (e.g., ACTIVE, INACTIVE, REVOKED)
     * @param page the number of the page to retrieve (zero-based index)
     * @param size the size of the page to retrieve (number of items per page)
     * @param issuedBy the unique identifier*/
    Page<ApiKeyListItem> findApiKeys(UUID parkingId, ApiKey.ApiKeyStatus status, int page, int size, UUID issuedBy);

    /**
     * Retrieves the details of an API key based on the provided key identifier.
     *
     * @param keyId the unique identifier of the API key to retrieve
     * @return an {@code ApiKeyDto} containing the details of the specified API key
     */
    ApiKeyDto getApiKeyDetails(UUID keyId);

    /**
     * Validates the provided API key and returns its details if valid.
     *
     * @param keyValue the string representation of the API key to validate
     * @return an {@code ApiKeyDto} containing the details of the validated API key
     *         if the key is valid; otherwise, an exception may be thrown
     */
    ApiKeyDto validateApiKey(String keyValue);
}
