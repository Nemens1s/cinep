package cinep.app.cinep.repository;

import cinep.app.cinep.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingsRepository extends JpaRepository<Rating, Long> {
}
