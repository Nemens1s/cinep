package cinep.app.cinep.service;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.exceptions.MovieTitleNotFoundException;
import cinep.app.cinep.exceptions.TheatreNotSupportedException;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.Rating;
import cinep.app.cinep.repository.MovieRepository;
import cinep.app.cinep.repository.RatingsRepository;
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

    private final List<String> theatres = new ArrayList<>(Arrays.asList(
            "https://www.forumcinemas.ee/xml/Schedule/",
            "https://api.cinamonkino.com/xml/Schedule/?id=9989&lang=en-US",
            "https://api.cinamonkino.com/xml/Schedule/?id=9999&lang=en-US",
            "http://www.viimsikino.ee/xml/Schedule/",
            "https://www.apollokino.ee/xml/Schedule/",
            "https://www.kino.ee/xml/Schedule/"
    ));

    private final XmlReaderService xmlReaderService;
    private final RatingService ratingService;
    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;
    private final RatingsRepository ratingsRepository;
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    public SearchService(XmlReaderService xmlReaderService, RatingService ratingService, MovieRepository movieRepository,
                         ObjectMapper objectMapper, RatingsRepository ratingsRepository) {
        this.xmlReaderService = xmlReaderService;
        this.ratingService = ratingService;
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
        this.ratingsRepository = ratingsRepository;
    }


    public List<MovieDto> findAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return objectMapper.convertMovieListToDtoList(movies);
    }

    private List<Movie> getMoviesFromAPI() {
        List<Movie> listOfMovies = new ArrayList<>();
        theatres.forEach(theatre -> listOfMovies.addAll(xmlReaderService.parseAllXml(theatre)));
        listOfMovies.sort(Comparator.comparing(Movie::getStartTime));
        return listOfMovies;
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
        logger.info("Started refreshing database, time is: "+LocalTime.now()+" Currently there are " + movies.size());
        LocalTime localTime = LocalTime.now(ZoneId.of("Europe/Tallinn"));
        List<Movie> result = movies.stream().filter(movie -> movie.getStartTime().isAfter(localTime)).collect(Collectors.toList());
        movieRepository.deleteAll();
        ratingService.updateRatings(result);
        List<Rating> ratings = ratingsRepository.findAll();
        Map<String, String> ratingsMap = new HashMap<>();
        logger.info("There are " + ratingsMap.size() + " elements in ratings map");
        ratings.forEach(rating -> ratingsMap.put(rating.getTitle(), rating.getRating()));
        result.forEach(movie -> movie.setUserRating(ratingsMap.getOrDefault(movie.getOriginalTitle(), "Currently not available")));
        logger.info("After refresh there are " + result.size() + " movies left: Current time is " + LocalTime.now());
        movieRepository.saveAll(result);
    }
}
