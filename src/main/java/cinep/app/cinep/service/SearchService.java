package cinep.app.cinep.service;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.repository.MovieRepository;
import cinep.app.cinep.service.utilities.ObjectMapper;
import cinep.app.cinep.specifications.MovieSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
public class SearchService {

    private final MovieRepository movieRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public SearchService(MovieRepository movieRepository, ObjectMapper objectMapper) {
        this.movieRepository = movieRepository;
        this.objectMapper = objectMapper;
    }


    public List<MovieDto> search(Map<String, String> request) {
        Specification<Movie> specification = MovieSpecification.createSpecification(request);
        List<Movie> movies = movieRepository.findAll(specification);
        return objectMapper.convertMovieListToDtoList(movies);
    }



    /*
    public List<MovieDto> findAllMovies() {
        List<Movie> movies = movieRepository.findAllMovies();
        return objectMapper.convertMovieListToDtoList(movies);
    }

    public List<MovieDto> findByTheatre(List<String> theatres) throws TheatreNotSupportedException {
        List<Movie> movies = new ArrayList<>();
        for (String theatre : theatres) {
            movies.addAll(movieRepository.findMoviesByTheatre(theatre));
        }
        if (movies.isEmpty()) {
            throw new TheatreNotSupportedException("This theatre is not supported");
        }
        return objectMapper.convertMovieListToDtoList(movies);
    }

    public List<MovieDto> findByTitle(String title, String lang) throws MovieTitleNotFoundException {
        List<Movie> movies;
        title = title.toLowerCase();
        if (lang.equalsIgnoreCase("rus")) {
            movies = movieRepository.findMoviesByRussianTitle(title);
        } else if (lang.equalsIgnoreCase("est")) {
            movies = movieRepository.findMoviesByEstonianTitle(title);
        } else {
            movies = movieRepository.findMoviesByEnglishTitle(title);
        }
        if (movies.isEmpty()) {
            movies = movieRepository.findMoviesByOriginalTitle(title);
        }
        if (movies.isEmpty()) {
            throw new MovieTitleNotFoundException("There is no movie with that title");
        }
        return objectMapper.convertMovieListToDtoList(movies);
    }

    public List<MovieDto> findByTimeAndDate(LocalTime sTime, LocalTime eTime,
                                            LocalDate sDate, LocalDate eDate) {
        if (sTime == null) {
            LocalTime currentTime = LocalTime.now();
            sTime = LocalTime.of(currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond());
        }
        if (eTime == null) {
            eTime = LocalTime.parse("23:59:59");
        }
        if (sDate == null) {
            sDate = LocalDate.now();
        }
        if (eDate == null) {
            eDate = sDate;
        }
        List<Movie> movies = movieRepository
                .findMoviesByStartTimeBetweenAndStartDateBetweenOrderByStartTimeAscStartDateAsc(sTime, eTime, sDate, eDate);
        return objectMapper.convertMovieListToDtoList(movies);
    }

    public List<MovieDto> findByGenres(List<String> genreDescription, String lang) {
        List<Movie> movies = new ArrayList<>();
        for (String desc : genreDescription) {
            movies.addAll(movieRepository.findByGenre(desc, lang));
        }
        return objectMapper.convertMovieListToDtoList(movies);
    }

    */


}
