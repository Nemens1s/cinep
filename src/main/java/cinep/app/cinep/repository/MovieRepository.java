package cinep.app.cinep.repository;

import cinep.app.cinep.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("FROM Movie WHERE originalTitle = ?1")
    List<Movie> findByOriginalTitle(String title);

    @Query("FROM Movie WHERE estonianTitle = ?1")
    List<Movie> findByTitle(String title);

    @Query("FROM Movie WHERE theatre = ?1")
    List<Movie> findByTheatre(String theatre);

    @Query("FROM Movie m INNER JOIN Genre g ON m.id = g.movie.id WHERE g.description = ?1 ")
    List<Movie> findByGenre(String description);

    List<Movie> findMoviesByStartTimeBetweenAndStartDateBetweenOrderByStartTimeAscStartDateAsc(@Param("sTime") LocalTime sTime, @Param("eTime")LocalTime eTime,
           @Param("sDate")LocalDate sDate, @Param("eDate")LocalDate eDate);

}
