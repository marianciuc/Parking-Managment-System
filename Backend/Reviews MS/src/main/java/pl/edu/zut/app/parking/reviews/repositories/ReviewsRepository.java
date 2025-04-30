package pl.edu.zut.app.parking.reviews.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.zut.app.parking.reviews.entities.Review;

import java.util.Optional;
import java.util.UUID;

public interface ReviewsRepository extends JpaRepository<Review, UUID> {
    Optional<Review> findByAuthorIdAndParkingId(UUID authorId, UUID parkingId);
    Page<Review> findAll(Specification<Review> specification, org.springframework.data.domain.Pageable pageable);
}
