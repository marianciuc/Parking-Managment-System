package pl.edu.zut.app.parking.auth.services;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.enums.Possibilities;
import pl.edu.zut.app.parking.auth.enums.UserType;

import java.util.Optional;
import java.util.UUID;

/**
 * UserRepositoryService is an interface providing various user management operations
 * for interacting with the user repository. It defines methods for retrieving,
 * saving, and validating user data.
 */
public interface UserRepositoryService {
    User save(User user);
    User findByEmail(String email);
    User findById(UUID uuid);
    boolean existsByEmail(@NotEmpty(message = "Email cannot be empty") @Email(message = "Email should be valid") String email);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    boolean existsByUserType(UserType userType);
}
