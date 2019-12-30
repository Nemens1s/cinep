package cinep.app.cinep.service;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ObjectMapper{

    public MovieDto convertMovieToDto(Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setOriginalTitle(movie.getOriginalTitle());
        movieDto.setUserRating(movie.getUserRating());
        movieDto.setTheatreAuditorium(movie.getTheatreAuditorium());
        movieDto.setTheatre(movie.getTheatre());
        movieDto.setShowUrl(movie.getShowUrl());
        movieDto.setId(movie.getId());
        movieDto.setStartDate(movie.getStartDate());
        movieDto.setStartTime(movie.getStartTime());
        movieDto.setDurationInMinutes(movie.getDurationInMinutes());
        return movieDto;
    }

    public UserDto convertUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setBookmarks(user.getBookmarks());
        return userDto;
    }


    public User convertDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setBookmarks(userDto.getBookmarks());
        return user;
    }


    public Movie convertDtoToMovie(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setOriginalTitle(movieDto.getOriginalTitle());
        movie.setUserRating(movieDto.getUserRating());
        movie.setTheatreAuditorium(movieDto.getTheatreAuditorium());
        movie.setTheatre(movieDto.getTheatre());
        movie.setShowUrl(movieDto.getShowUrl());
        movie.setId(movieDto.getId());
        movie.setStartDate(movieDto.getStartDate());
        movie.setStartTime(movieDto.getStartTime());
        movie.setDurationInMinutes(movie.getDurationInMinutes());
        return movie;
    }


    public Set<Movie> convertDtoSetToMovieSet(Set<MovieDto> movieDtoSet) {
        Set<Movie> movieSet = new HashSet<>();
        for (MovieDto movieDto : movieDtoSet){
            Movie movie = convertDtoToMovie(movieDto);
            movieSet.add(movie);
        }
        return movieSet;
    }


    public Set<MovieDto> convertMovieSetToDtoSet(Set<Movie> movieSet) {
        Set<MovieDto> movieDtoSet = new HashSet<>();
        for (Movie movie : movieSet){
            MovieDto movieDto = convertMovieToDto(movie);
            movieDtoSet.add(movieDto);
        }
        return movieDtoSet;
    }

    public List<MovieDto> convertMovieListToDtoList(List<Movie> movies){
        List<MovieDto> movieDtos = new ArrayList<>();
        movies.forEach(movie -> movieDtos.add(convertMovieToDto(movie)));
        return movieDtos;
    }
}
