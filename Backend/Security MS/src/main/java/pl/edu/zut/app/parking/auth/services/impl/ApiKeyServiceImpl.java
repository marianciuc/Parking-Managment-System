package pl.edu.zut.app.parking.auth.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.auth.clients.ParkingClient;
import pl.edu.zut.app.parking.auth.dto.ApiKeyDto;
import pl.edu.zut.app.parking.auth.dto.ApiKeyListItem;
import pl.edu.zut.app.parking.auth.entities.ApiKey;
import pl.edu.zut.app.parking.auth.enums.Scope;
import pl.edu.zut.app.parking.auth.exceptions.ApiKeyNotFoundException;
import pl.edu.zut.app.parking.auth.exceptions.BusinessException;
import pl.edu.zut.app.parking.auth.exceptions.InvalidApiKeyException;
import pl.edu.zut.app.parking.auth.repositories.ApiKeyRepository;
import pl.edu.zut.app.parking.auth.security.utils.SecurityUtils;
import pl.edu.zut.app.parking.auth.services.ApiKeyService;
import pl.edu.zut.app.parking.auth.specifications.ApiKeySpecifications;
import pl.edu.zut.app.parking.auth.utils.ApiKeyGenerator;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final ParkingClient parkingClient;

    @Override
    public ApiKeyDto generateApiKey(UUID parkingId, Set<Scope> scopes) {
        log.info("Generating API key for parkingId={} with scope={}", parkingId, scopes);

        String apiKeyValue = ApiKeyGenerator.generateKey();
        log.debug("Generated API key value: {}", apiKeyValue);

        ResponseEntity<Object> isParkingAvailable = parkingClient.isParkingAvailable(parkingId);

        if (isParkingAvailable.getStatusCode().value() != 200 ) {
            log.error("Parking with ID: {} is not available", parkingId);
            throw new BusinessException("Parking with ID " + parkingId + " is not available");
        }

        ApiKey apiKey = ApiKey.builder()
                .scope(scopes)
                .parkingId(parkingId)
                .scope(scopes)
                .keyValue(apiKeyValue)
                .issuedBy(SecurityUtils.extractUser().getId())
                .build();

        ApiKey savedApiKey = apiKeyRepository.save(apiKey);
        log.info("API key created with ID: {} for parkingId={}", savedApiKey.getId(), parkingId);

        return ApiKeyDto.fromEntity(savedApiKey);
    }

    @Override
    @CacheEvict(value = "apiKeys", key = "#apiKeyId")
    public ApiKeyDto revokeApiKey(UUID apiKeyId) {
        log.info("Revoking API key with ID: {}", apiKeyId);

        ApiKey apiKey = findApiKeyOrThrow(apiKeyId);
        apiKey.setStatus(ApiKey.ApiKeyStatus.REVOKED);

        ApiKeyDto apiKeyDto = ApiKeyDto.fromEntity(apiKeyRepository.save(apiKey));
        log.info("API key with ID: {} has been revoked", apiKeyId);

        return apiKeyDto;
    }

    @Override
    public Page<ApiKeyListItem> findApiKeys(UUID parkingId, ApiKey.ApiKeyStatus status, int page, int size, UUID issuedBy) {
        log.info("Finding API keys for parkingId={}, status={}, page={}, size={}, issuedBy={}",
                parkingId, status, page, size, issuedBy);

        Specification<ApiKey> spec = Specification.where(ApiKeySpecifications.withParkingId(parkingId))
                .and(ApiKeySpecifications.withStatus(status))
                .and(ApiKeySpecifications.withIssuedBy(issuedBy));

        Page<ApiKey> result = apiKeyRepository.findAll(spec, PageRequest.of(page, size));

        log.info("Found {} API keys for the given criteria", result.getNumberOfElements());
        return result.map(ApiKeyListItem::fromEntity);
    }

    @Override
    @Cacheable(value = "apiKeys", key = "#keyId", unless = "#result == null")
    public ApiKeyDto getApiKeyDetails(UUID keyId) {
        log.info("Retrieving details for API key with ID: {}", keyId);

        ApiKey apiKey = findApiKeyOrThrow(keyId);
        log.debug("Retrieved API key details: {}", apiKey);

        return ApiKeyDto.fromEntity(apiKey);
    }

    @Override
    @Cacheable(value = "apiKeys", key = "'keyValue_' + #keyValue", unless = "#result == null")
    public ApiKeyDto validateApiKey(String keyValue) {
        log.info("Validating API key with value: {}", keyValue);

        return apiKeyRepository.findByKeyValue(keyValue)
                .map(apiKey -> {
                    log.debug("API key is valid: {}", apiKey);
                    if (apiKey.isActive()) return ApiKeyDto.fromEntity(apiKey);
                    else {
                        log.error("API key is not active: {}", apiKey);
                        throw new InvalidApiKeyException("API key is not active");
                    }
                })
                .orElseThrow(() -> {
                    log.error("API key validation failed. No API key found with value: {}", keyValue);
                    return new ApiKeyNotFoundException("API key with value " + keyValue + " not found");
                });
    }

    @Cacheable(value = "apiKeys", key = "#id", unless = "#result == null")
    public ApiKey findApiKeyOrThrow(UUID id) {
        log.debug("Looking for API key with ID: {}", id);

        return apiKeyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No API key found with ID: {}", id);
                    return new ApiKeyNotFoundException("API key with ID " + id + " not found");
                });
    }
}