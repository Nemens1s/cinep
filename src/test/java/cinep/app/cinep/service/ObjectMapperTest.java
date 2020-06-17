package cinep.app.cinep.service;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.User;
import cinep.app.cinep.service.utilities.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ObjectMapperTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testConvertMovieSetToDtoSet() {
        Set<Movie> movies = new HashSet<>();
        movies.add(getMovie());
        Set<MovieDto> movieDtos = objectMapper.convertMovieSetToDtoSet(movies);
        assertEquals(movies.iterator().next().getOriginalTitle(), movieDtos.iterator().next().getOriginalTitle());
    }

    @Test
    public void testConvertDtoSetToMovieSet() {
        Set<MovieDto> movieDtos = new HashSet<>();
        movieDtos.add(getMovieDto());
        Set<Movie> movies = objectMapper.convertDtoSetToMovieSet(movieDtos);
        assertEquals(movieDtos.iterator().next().getOriginalTitle(), movies.iterator().next().getOriginalTitle());
    }

    @Test
    public void testConvertMovieToDto() {
        MovieDto result = objectMapper.convertMovieToDto(getMovie());
        assertEquals(getMovieDto().getOriginalTitle(), result.getOriginalTitle());
    }

    @Test
    public void testConvertDtoToMovie(){
        Movie result = objectMapper.convertDtoToMovie(getMovieDto());
        assertEquals(getMovie().getOriginalTitle(), result.getOriginalTitle());
    }

    @Test
    public void testConvertUserToDto() {
        UserDto result = objectMapper.convertUserToDto(getUser());
        assertEquals(getUserDto().getUsername(), result.getUsername());
    }

    @Test
    public void testConvertDtoToUser() {
        User result = objectMapper.convertDtoToUser(getUserDto());
        assertEquals(getUser().getUsername(), result.getUsername());
    }

    @Test(expected = NullPointerException.class)
    public void testConvertMovieDtoThrowsNullPointerException() {
        objectMapper.convertDtoToMovie(null);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertMovieThrowsNullPointerException(){
        objectMapper.convertMovieToDto(null);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertUserDtoThrowsNullPointerException(){
        objectMapper.convertDtoToUser(null);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertUserThrowsNullPointerException(){
        objectMapper.convertUserToDto(null);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertDtoSetThrowsNullPointerException(){
        objectMapper.convertDtoSetToMovieSet(null);
    }

    @Test(expected = NullPointerException.class)
    public void testConvertSetThrowsNullPointerException(){
        objectMapper.convertMovieSetToDtoSet(null);
    }

    private static MovieDto getMovieDto() {
        MovieDto movieDto = new MovieDto();
        movieDto.setOriginalTitle("Test");
        movieDto.setUserRating("Test");
        movieDto.setTheatreAuditorium("Test");
        movieDto.setTheatre("Test");
        movieDto.setShowUrl("Test");
        movieDto.setId(1L);
        movieDto.setStartDate(LocalDate.MAX);
        movieDto.setStartTime(LocalTime.MIDNIGHT);
        movieDto.setDurationInMinutes(90);
        return movieDto;
    }

    private static Movie getMovie() {
        Movie movieDto = new Movie();
        movieDto.setOriginalTitle("Test");
        movieDto.setUserRating("Test");
        movieDto.setTheatreAuditorium("Test");
        movieDto.setTheatre("Test");
        movieDto.setShowUrl("Test");
        movieDto.setId(1L);
        movieDto.setStartDate(LocalDate.MAX);
        movieDto.setStartTime(LocalTime.MIDNIGHT);
        movieDto.setDurationInMinutes(90);
        return movieDto;
    }

    private static User getUser() {
        User user = new User();
        user.setUsername("Test");
        user.setId(1L);
        Set<Movie> movies = new HashSet<>();
        movies.add(getMovie());
        Set<String> titles = new HashSet<>();
        movies.forEach(movie -> titles.add(movie.getOriginalTitle()));
        user.setBookmarks(titles);
        return user;
    }

    public static UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername("Test");
        Set<MovieDto> movieDtos = new HashSet<>();
        movieDtos.add(getMovieDto());
        Set<String> titles = new HashSet<>();
        movieDtos.forEach(movie -> titles.add(movie.getOriginalTitle()));
        userDto.setBookmarks(titles);
        return userDto;
    }
}
