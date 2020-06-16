package cinep.app.cinep.service;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.exceptions.MovieTitleNotFoundException;
import cinep.app.cinep.exceptions.TheatreNotSupportedException;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.repository.MovieRepository;
import cinep.app.cinep.service.utilities.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SearchService {

    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public SearchService(MovieRepository movieRepository, ObjectMapper objectMapper) {
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
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




}
