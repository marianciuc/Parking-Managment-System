package pl.edu.zut.app.parking.auth.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.enums.Possibilities;
import pl.edu.zut.app.parking.auth.enums.UserType;
import pl.edu.zut.app.parking.auth.exceptions.UserNotFoundException;
import pl.edu.zut.app.parking.auth.repositories.UserRepository;
import pl.edu.zut.app.parking.auth.services.UserRepositoryService;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRepositoryServiceImpl implements UserRepositoryService {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository repository;


    @Override
    @CachePut(value = "users", key = "#result.id")
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        log.info("Saving user...");
        User savedUser = repository.save(user);
        log.info("Successfully saved user: {}", savedUser);
        return savedUser;
    }

    @Override
    @Cacheable(value = "usersByEmail", key = "#email")
    public User findByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return repository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with email '{}' not found", email);
                    return new UserNotFoundException(USER_NOT_FOUND);
                });
    }

    @Override
    @Cacheable(value = "users", key = "#uuid")
    public User findById(UUID uuid) {
        log.info("Fetching user by ID: {}", uuid);
        return repository.findById(uuid)
                .orElseThrow(() -> {
                    log.error("User with ID '{}' not found", uuid);
                    return new UserNotFoundException(USER_NOT_FOUND);
                });
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("Checking existence of user by email: {}", email);
        boolean exists = repository.existsByEmail(email);
        log.info("User existence by email '{}': {}", email, exists);
        return exists;
    }

    @Override
    @Cacheable(value = "usersPages", key = "{#specification, #pageable}")
    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        log.info("Fetching users with specification and pageable: spec={}, pageable={}", specification, pageable);
        Page<User> users = repository.findAll(specification, pageable);
        log.info("Fetched {} user(s)", users.getTotalElements());
        return users;
    }

    @Override
    public boolean existsByUserType(UserType userType) {
        return repository.existsByUserType(userType);
    }

}
