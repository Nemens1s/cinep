package cinep.app.cinep.service;

import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.Rating;
import cinep.app.cinep.pojo.MovieData;
import cinep.app.cinep.repository.MovieRepository;
import cinep.app.cinep.repository.RatingsRepository;
import cinep.app.cinep.service.parsers.AdditionalDataParser;
import cinep.app.cinep.service.parsers.ScheduleParser;
import cinep.app.cinep.service.utilities.SimilarityComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class MovieService {

    private final ScheduleParser scheduleParser;
    private final AdditionalDataParser dataParser;
    private final RatingService ratingService;
    private final MovieRepository movieRepository;
    private final RatingsRepository ratingsRepository;
    private final GenreService genreService;
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    public MovieService(ScheduleParser scheduleParser, AdditionalDataParser dataParser, RatingService ratingService,
                        MovieRepository movieRepository, RatingsRepository ratingsRepository,
                        GenreService genreService) {
        this.scheduleParser = scheduleParser;
        this.dataParser = dataParser;
        this.ratingService = ratingService;
        this.movieRepository = movieRepository;
        this.ratingsRepository = ratingsRepository;
        this.genreService = genreService;
    }

    public void fetchMovies() {
        LocalTime start = LocalTime.now();
        logger.info("Getting new movies to database: Time is " + start);
        movieRepository.deleteAll();
        List<Movie> movies = scheduleParser.getMoviesFromAPI();
        Map<String, MovieData> movieData = dataParser.getData();
        addTitlesToMovies(movies, movieData);
        logger.info("Total movies " + movies.size());
        movieRepository.saveAll(movies);
        genreService.updateGenres(movies, movieData);
        LocalTime end = LocalTime.now();
        logger.info(String.format("Total time: %s seconds", Duration.between(start, end).getSeconds()));
    }

    public void refresh() {
        List<Movie> movies = movieRepository.findAll();
        logger.info(String.format("Started refreshing database, time is: %tR Currently there are %d movies",
                LocalTime.now(), movies.size()));
        if (movies.size() > 0) {
            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Tallinn"));

            List<Movie> updatedMovies = new ArrayList<>();
            for (Movie movie : movies) {
                if (movie.getStartDate().isAfter(localDateTime.toLocalDate())
                        || movie.getStartTime().isAfter(localDateTime.toLocalTime())) {
                    updatedMovies.add(movie);
                } else {
                    movieRepository.deleteById(movie.getId());
                }
            }
            ratingService.updateRatings(updatedMovies);
            setRatingsToMovies(updatedMovies);
            logger.info(String.format("After refresh there are %d  movies left: Current time is %tR",
                    updatedMovies.size(), LocalTime.now()));
            logger.info(String.format("First movie id is %d", updatedMovies.get(0).getId()));
            movieRepository.saveAll(updatedMovies);
            logger.info("FINISHED REFRESHING DATABASE");
        } else {
            logger.info("There are no movies, skipping refresh");
            getMoviesToDataBase();
        }
    }

    private void setRatingsToMovies(List<Movie> result) {
        List<Rating> ratings = ratingsRepository.findAll();
        Map<String, String> ratingsMap = new HashMap<>();
        ratings.forEach(rating -> ratingsMap.put(rating.getTitle(), rating.getRating()));
        logger.info("There are " + ratingsMap.size() + " elements in ratings map");
        result.forEach(movie -> movie.setUserRating(ratingsMap.getOrDefault(movie.getOriginalTitle(), "Currently not available")));
    }

    private void addTitlesToMovies(List<Movie> movies, Map<String, MovieData> movieData) {
        for (Movie movie : movies) {
            Optional<String> title = movieData.keySet().stream()
                    .filter(s -> SimilarityComparator.similarity(s, movie.getOriginalTitle()))
                    .findFirst();
            if (title.isPresent()) {
                MovieData data = movieData.get(title.get());
                movie.setRussianTitle(data.getRusTitle());
                movie.setEnglishTitle(data.getEngTitle());
            }
        }
    }

    @Scheduled(cron = "0 0 4 */3 * ?", zone = "Europe/Tallinn")
    private void getMoviesToDataBase() {
        fetchMovies();
    }

    @Scheduled(cron = "0 0 * * * *")
    private void refreshDataBase() {
        refresh();
    }
}
