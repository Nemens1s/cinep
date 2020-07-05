package cinep.app.cinep.service.utilities;

import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.Rating;
import cinep.app.cinep.pojo.MovieData;
import cinep.app.cinep.repository.MovieRepository;
import cinep.app.cinep.repository.RatingsRepository;
import cinep.app.cinep.service.GenreService;
import cinep.app.cinep.service.RatingService;
import cinep.app.cinep.service.SearchService;
import cinep.app.cinep.service.parsers.AdditionalDataParser;
import cinep.app.cinep.service.parsers.ScheduleParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DatabaseRefresher {

    private final ScheduleParser scheduleParser;
    private final AdditionalDataParser dataParser;
    private final RatingService ratingService;
    private final MovieRepository movieRepository;
    private final RatingsRepository ratingsRepository;
    private final GenreService genreService;
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    public DatabaseRefresher(ScheduleParser scheduleParser, AdditionalDataParser dataParser, RatingService ratingService,
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
        logger.info("Getting new movies to database: Time is " + LocalDateTime.now());
        movieRepository.deleteAll();
        List<Movie> movies = scheduleParser.getMoviesFromAPI();
        Map<String, MovieData> movieData = dataParser.getData();
        addTitlesToMovies(movies, movieData);
        logger.info("Total movies " + movies.size());
        movieRepository.saveAll(movies);
        genreService.updateGenres(movies, movieData);
    }

    public void refresh() {
        List<Movie> movies = movieRepository.findAll();
        logger.info("Started refreshing database, time is: " + LocalTime.now() + " Currently there are " + movies.size() + " movies");
        if (movies.size() > 0) {
            LocalTime localTime = LocalTime.now(ZoneId.of("Europe/Tallinn"));
            List<Movie> result = movies.stream().filter(movie -> movie.getStartTime().isAfter(localTime)).collect(Collectors.toList());
            movieRepository.deleteAll();
            ratingService.updateRatings(result);
            List<Rating> ratings = ratingsRepository.findAll();
            Map<String, String> ratingsMap = new HashMap<>();
            ratings.forEach(rating -> ratingsMap.put(rating.getTitle(), rating.getRating()));
            logger.info("There are " + ratingsMap.size() + " elements in ratings map");
            result.forEach(movie -> movie.setUserRating(ratingsMap.getOrDefault(movie.getOriginalTitle(), "Currently not available")));
            logger.info("After refresh there are " + result.size() + " movies left: Current time is " + LocalTime.now());
            logger.info("FIRST MOVIE ID IS " + result.get(0).getId());
            movieRepository.saveAll(result);
            logger.info("FINISHED REFRESHING DATABASE");
        } else {
            logger.info("There are no movies, skipping refresh");
            getMoviesToDataBase();
        }
    }

    @Scheduled(cron = "0 0 4 * * ?", zone = "Europe/Tallinn")
    private void getMoviesToDataBase() {
        fetchMovies();
    }

    @Scheduled(cron = "0 0 * * * *")
    private void refreshDataBase() {
        refresh();
    }

    private void addTitlesToMovies (List<Movie> movies, Map<String, MovieData> movieData) {
        for (Movie movie : movies) {
            if (movieData.containsKey(movie.getOriginalTitle())) {
                MovieData data = movieData.get(movie.getOriginalTitle());
                movie.setRussianTitle(data.getRusTitle());
                movie.setEnglishTitle(data.getEngTitle());
                movie.setEstonianTitle(data.getEstTitle());
            }
        }
    }
}
