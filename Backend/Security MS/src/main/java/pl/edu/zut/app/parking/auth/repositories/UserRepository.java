package pl.edu.zut.app.parking.auth.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.enums.Possibilities;
import pl.edu.zut.app.parking.auth.enums.UserType;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<User> findAll(Specification<User> specification, Pageable pageable);

    boolean existsByUserPossibilitiesIn(Collection<Set<Possibilities>> userPossibilities);

    boolean existsByUserType(UserType userType);
}
