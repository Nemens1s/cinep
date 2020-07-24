package cinep.app.cinep.repository;

import cinep.app.cinep.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    /*
    @Query("FROM Movie m ORDER BY m.startDate ASC, m.startTime ASC")
    List<Movie> findAllMovies();

    @Query("FROM Movie  m WHERE lower(m.russianTitle) LIKE %:t% ORDER BY m.startDate ASC, m.startTime ASC")
    List<Movie> findMoviesByRussianTitle(@Param("t") String title);

    @Query("FROM Movie  m WHERE lower(m.englishTitle) LIKE %:t% ORDER BY m.startDate ASC, m.startTime ASC")
    List<Movie> findMoviesByEnglishTitle(@Param("t") String title);

    @Query("FROM Movie  m WHERE lower(m.estonianTitle) LIKE %:t% ORDER BY m.startDate ASC, m.startTime ASC")
    List<Movie> findMoviesByEstonianTitle(@Param("t") String title);

    @Query("FROM Movie  m WHERE lower(m.originalTitle) LIKE %:t% ORDER BY m.startDate ASC, m.startTime ASC")
    List<Movie> findMoviesByOriginalTitle(@Param("t") String title);

    @Query("FROM Movie  m WHERE  m.theatre = :t ORDER BY m.startDate ASC, m.startTime ASC")
    List<Movie> findMoviesByTheatre(@Param("t") String theatre);

    @Query("FROM Movie m INNER JOIN Genre g ON m.id = g.movie.id WHERE g.description = :desc AND g.lang = :lang ORDER BY m.startDate ASC, m.startTime ASC")
    List<Movie> findByGenre(@Param("desc") String description, @Param("lang") String lang);

    List<Movie> findMoviesByStartTimeBetweenAndStartDateBetweenOrderByStartTimeAscStartDateAsc(@Param("sTime") LocalTime sTime, @Param("eTime") LocalTime eTime,
                                                                                               @Param("sDate") LocalDate sDate, @Param("eDate") LocalDate eDate);
     */
}
