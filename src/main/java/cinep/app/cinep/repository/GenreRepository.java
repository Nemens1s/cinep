package cinep.app.cinep.repository;

import cinep.app.cinep.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    @Query("FROM Genre g WHERE g.description=?1")
    Genre findByDescription(String description);

}

