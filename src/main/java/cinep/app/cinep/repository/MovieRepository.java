package cinep.app.cinep.repository;

import cinep.app.cinep.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("FROM Movie WHERE originalTitle = ?1")
    List<Movie> findByTitle(String title);

    @Query("FROM Movie WHERE theatre = ?1")
    List<Movie> findByTheatre(String theatre);

}
