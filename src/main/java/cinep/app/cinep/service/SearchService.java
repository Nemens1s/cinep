package cinep.app.cinep.service;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.exceptions.MovieTitleNotFoundException;
import cinep.app.cinep.exceptions.TheatreNotSupportedException;
import cinep.app.cinep.model.Cinema;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.Rating;
import cinep.app.cinep.repository.MovieRepository;
import cinep.app.cinep.repository.RatingsRepository;
import cinep.app.cinep.service.parsers.ScheduleParser;
import cinep.app.cinep.service.parsers.XmlReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SearchService {

    private final ScheduleParser scheduleParser;
    private final RatingService ratingService;
    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;
    private final RatingsRepository ratingsRepository;
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    public SearchService(ScheduleParser scheduleParser, RatingService ratingService, MovieRepository movieRepository,
                         ObjectMapper objectMapper, RatingsRepository ratingsRepository) {
        this.scheduleParser = scheduleParser;
        this.ratingService = ratingService;
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
        this.ratingsRepository = ratingsRepository;
    }


    public List<MovieDto> findAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return objectMapper.convertMovieListToDtoList(movies);
    }

    public List<MovieDto> findByTheatre(String theatreName) throws TheatreNotSupportedException {
        List<Movie> movies = movieRepository.findByTheatre(theatreName);
        if (movies.isEmpty()) {
            throw new TheatreNotSupportedException("This theatre is not supported");
        }
        return objectMapper.convertMovieListToDtoList(movies);
    }

    public List<MovieDto> findByTitle(String title) throws MovieTitleNotFoundException {
        List<Movie> movies = movieRepository.findByTitle(title);
        if (movies.isEmpty()) {
            throw new MovieTitleNotFoundException("There is no movie with that title");
        }
        return objectMapper.convertMovieListToDtoList(movies);
    }

    public List<MovieDto> findByTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime timeToParse = LocalTime.parse(time, formatter);
        List<Movie> movies = movieRepository.findAll();
        movies = movies.stream().filter(movie -> Duration.between(timeToParse, movie.getStartTime()).getSeconds() <= 14400 ).collect(Collectors.toList());
        return objectMapper.convertMovieListToDtoList(movies);
    }

    private List<Movie> getMoviesFromAPI() {
        List<Movie> listOfMovies = new ArrayList<>();
        List<Cinema> cinemas = new ArrayList<>(Arrays.asList(Cinema.values()));
        cinemas.forEach(cinema -> listOfMovies.addAll(scheduleParser.parseSchedule(cinema)));
        listOfMovies.sort(Comparator.comparing(Movie::getStartTime));
        return listOfMovies;
    }

    @Scheduled(cron = "0 0 4 * * ?", zone = "Europe/Tallinn")
    private void getMoviesToDataBase() {
        logger.info("Getting new movies to database: Time is " + LocalDateTime.now());
        movieRepository.deleteAll();
        List<Movie> movies = getMoviesFromAPI();
        logger.info("Total movies " + movies.size());
        movies.sort(Comparator.comparing(Movie::getStartTime));
        movieRepository.saveAll(movies);
    }

    @Scheduled(cron = "0 0 * * * *")
    private void refreshDataBase() {
        List<Movie> movies = movieRepository.findAll();
        logger.info("Started refreshing database, time is: "+LocalTime.now()+" Currently there are " + movies.size() + " movies");
        if (movies.size() > 0){
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
            logger.info("FINISHED REFRESING DATABASE");
        } else {
            logger.info("There are no movies, skipping refresh");
            getMoviesToDataBase();
        }

    }
}
