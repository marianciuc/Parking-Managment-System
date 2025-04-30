package pl.edu.zut.app.parking.auth.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.zut.app.parking.auth.entities.ApiKey;

import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {
    Optional<ApiKey> findByKeyValue(String key);

    Page<ApiKey> findAll(Specification<ApiKey> spec, Pageable of);
}
