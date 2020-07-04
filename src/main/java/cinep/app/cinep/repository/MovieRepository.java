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

    List<Movie> findMoviesByRussianTitleOrderByStartTimeAscStartDateAsc(String title);

    List<Movie> findMoviesByEnglishTitleOrderByStartTimeAscStartDateAsc(String title);

    List<Movie> findMoviesByEstonianTitleOrderByStartTimeAscStartDateAsc(String title);

    List<Movie> findMoviesByOriginalTitleOrderByStartTimeAscStartDateAsc(String title);

    List<Movie> findMoviesByTheatreOrderByStartTimeAscStartDateAsc(String theatre);

    @Query("FROM Movie m INNER JOIN Genre g ON m.id = g.movie.id WHERE g.description = ?1 ")
    List<Movie> findByGenreOrderByStartTimeAscStartDateAsc(String description);

    List<Movie> findMoviesByStartTimeBetweenAndStartDateBetweenOrderByStartTimeAscStartDateAsc(@Param("sTime") LocalTime sTime, @Param("eTime")LocalTime eTime,
           @Param("sDate")LocalDate sDate, @Param("eDate")LocalDate eDate);

}
