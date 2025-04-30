package pl.edu.zut.app.parking.auth.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.edu.zut.app.parking.auth.dto.ApiKeyDto;
import pl.edu.zut.app.parking.auth.dto.ApiKeyListItem;
import pl.edu.zut.app.parking.auth.entities.ApiKey;
import pl.edu.zut.app.parking.auth.enums.Scope;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiKeyServiceTest {

    @Test
    void shouldGenerateApiKey() {
        ApiKeyService apiKeyService = mock(ApiKeyService.class);
        UUID parkingId = UUID.randomUUID();
        Set<Scope> scopes = Set.of(Scope.ADMIN, Scope.PARKING);

        ApiKeyDto expectedApiKeyDto = new ApiKeyDto(
                UUID.randomUUID(),
                "generated-key",
                parkingId,
                scopes.stream().map(Scope::name).collect(Collectors.toSet()),
                ApiKey.ApiKeyStatus.ACTIVE.name(),
                UUID.randomUUID(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(apiKeyService.generateApiKey(parkingId, scopes)).thenReturn(expectedApiKeyDto);

        ApiKeyDto result = apiKeyService.generateApiKey(parkingId, scopes);

        assertNotNull(result);
        assertEquals(expectedApiKeyDto, result);
    }

    @Test
    void shouldRevokeApiKey() {
        ApiKeyService apiKeyService = mock(ApiKeyService.class);
        UUID apiKeyId = UUID.randomUUID();

        ApiKeyDto expectedApiKeyDto = new ApiKeyDto(
                apiKeyId,
                "revoked-key",
                UUID.randomUUID(),
                Set.of(String.valueOf(Scope.ADMIN)),
                ApiKey.ApiKeyStatus.REVOKED.name(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(apiKeyService.revokeApiKey(apiKeyId)).thenReturn(expectedApiKeyDto);

        ApiKeyDto result = apiKeyService.revokeApiKey(apiKeyId);

        assertNotNull(result);
        assertEquals(expectedApiKeyDto, result);
    }

    @Test
    void shouldFindApiKeys() {
        ApiKeyService apiKeyService = mock(ApiKeyService.class);
        UUID parkingId = UUID.randomUUID();
        ApiKey.ApiKeyStatus status = ApiKey.ApiKeyStatus.ACTIVE;
        int page = 0;
        int size = 10;
        UUID issuedBy = UUID.randomUUID();

        ApiKeyListItem listItem = new ApiKeyListItem(
                UUID.randomUUID(),
                Set.of(Scope.ADMIN),
                ApiKey.ApiKeyStatus.ACTIVE,
                UUID.randomUUID(),
                null,
                UUID.randomUUID()
        );

        Page<ApiKeyListItem> expectedPage = new PageImpl<>(List.of(listItem));

        when(apiKeyService.findApiKeys(parkingId, status, page, size, issuedBy))
                .thenReturn(expectedPage);

        Page<ApiKeyListItem> result = apiKeyService.findApiKeys(parkingId, status, page, size, issuedBy);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(listItem, result.getContent().get(0));
    }

    @Test
    void shouldGetApiKeyDetails() {
        ApiKeyService apiKeyService = mock(ApiKeyService.class);
        UUID keyId = UUID.randomUUID();

        ApiKeyDto expectedApiKeyDto = new ApiKeyDto(
                keyId,
                "detail-key",
                UUID.randomUUID(),
                Set.of(String.valueOf(Scope.ADMIN)),
                ApiKey.ApiKeyStatus.ACTIVE.name(),
                UUID.randomUUID(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(apiKeyService.getApiKeyDetails(keyId)).thenReturn(expectedApiKeyDto);

        ApiKeyDto result = apiKeyService.getApiKeyDetails(keyId);

        assertNotNull(result);
        assertEquals(expectedApiKeyDto, result);
    }

    @Test
    void shouldValidateApiKey() {
        ApiKeyService apiKeyService = mock(ApiKeyService.class);
        String keyValue = "valid-key";

        ApiKeyDto expectedApiKeyDto = new ApiKeyDto(
                UUID.randomUUID(),
                keyValue,
                UUID.randomUUID(),
                Set.of(String.valueOf(Scope.PARKING)),
                ApiKey.ApiKeyStatus.ACTIVE.name(),
                UUID.randomUUID(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(apiKeyService.validateApiKey(keyValue)).thenReturn(expectedApiKeyDto);

        ApiKeyDto result = apiKeyService.validateApiKey(keyValue);

        assertNotNull(result);
        assertEquals(expectedApiKeyDto, result);
    }
}