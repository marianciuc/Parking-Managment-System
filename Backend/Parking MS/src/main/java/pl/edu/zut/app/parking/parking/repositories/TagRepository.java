package pl.edu.zut.app.parking.parking.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.zut.app.parking.parking.entities.Tag;

import java.util.List;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID>, JpaSpecificationExecutor<Tag> {

    List<Tag> findByNameContains(String name);

    List<Tag> findByNameContainsIgnoreCase(String name);

    boolean existsByName(String name);

    @Query("SELECT t FROM Tag t WHERE t.id IN :ids")
    List<Tag> findTagsByIds(@Param("ids") List<UUID> ids);

    List<Tag> findAll(Specification<Tag> spec);
}
