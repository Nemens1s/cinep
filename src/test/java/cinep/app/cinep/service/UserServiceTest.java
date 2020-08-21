package cinep.app.cinep.service;

import cinep.app.cinep.dto.LoginDetails;
import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.exceptions.MovieAlreadyInBookmarksException;
import cinep.app.cinep.exceptions.MyBadRequestException;
import cinep.app.cinep.exceptions.UserAlreadyInDatabaseException;
import cinep.app.cinep.exceptions.UserNotFoundException;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.User;
import cinep.app.cinep.repository.UserRepository;
import cinep.app.cinep.security.SecurityConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    JdbcTemplate jdbc;

    private static final String CREATE_DB = "create-data.sql";
    private static final String DROP_DB = "drop-data.sql";


    @Before
    public void before() throws SQLException {
        try (Connection connection = Objects.requireNonNull(jdbc.getDataSource()).getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_DB));
        }
    }

    @After
    public void after() throws SQLException {
        try (Connection connection = Objects.requireNonNull(jdbc.getDataSource()).getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_DB));
        }
    }

    @Test
    @Transactional
    public void getBookMarksSuccess()  {
        Set<MovieDto> movies = userService.getBookmarks("First User");
        assertEquals(2, movies.size());
        assertTrue(movies.stream().anyMatch(movie -> movie.getOriginalTitle().equals("Star Wars")));
    }

    @Test
    @Transactional
    public void addToBookmarksSuccess() {
        MovieDto dto = new MovieDto();
        dto.setId(12L);
        Set<MovieDto> movies = userService.addToBookmarks(dto, "First User");
        assertTrue(movies.stream().anyMatch(movie -> movie.getId() == 12L));
        assertEquals(3, movies.size());
    }

    @Test (expected = MovieAlreadyInBookmarksException.class)
    @Transactional
    public void addToBookmarksAlreadyThere () {
        MovieDto dto = new MovieDto();
        dto.setId(1L);
        userService.addToBookmarks(dto, "First User");
    }

    @Test
    @Transactional
    public void removeFromBookmarksSuccess () {
        MovieDto dto = new MovieDto();
        dto.setId(1L);
        Set<MovieDto> movieDtos = userService.removeBookmark(dto, "First User");
        assertEquals(1, movieDtos.size());
        assertTrue(movieDtos.stream().noneMatch(movieDto -> movieDto.getOriginalTitle().equals("Star Wars")));

        Set<MovieDto> movies = userService.getBookmarks("First User");
        assertTrue(movies.stream().noneMatch(movieDto -> movieDto.getOriginalTitle().equals("Star Wars")));
    }

    @Test (expected = MyBadRequestException.class)
    @Transactional
    public void removeFromBookmarksNoMovieInBookmarks () {
        MovieDto dto = new MovieDto();
        dto.setId(100L);
        userService.removeBookmark(dto, "First User");
    }

    @Test (expected = UserAlreadyInDatabaseException.class)
    public void testRegisterUserAlreadyExists () throws UserAlreadyInDatabaseException {
        UserDto dto = new UserDto();
        dto.setUsername("First User");
        dto.setPassword("qwerty");
        userService.register(dto);
    }


    @Test
    public void testRegisterAndThenLoginSuccess () throws UserAlreadyInDatabaseException {
        UserDto dto = new UserDto();
        dto.setUsername("test");
        dto.setPassword("test");
        userService.register(dto);
        LoginDetails details = userService.login(dto);
        assertTrue(details.getRoles().stream().anyMatch(role -> role.equals("ROLE_USER")));
    }
//
//    @Test(expected = UserNotFoundException.class)
//    @Transactional
//    public void insertIntoBookmarksUserNotFoundException() throws UserNotFoundException, MovieAlreadyInBookmarksException {
//        userService.addToBookmarks(100L, "Not found");
//    }
//
//    @Test(expected = MovieAlreadyInBookmarksException.class)
//    @Transactional
//    public void insertIntoBookmarksAlreadyInBookmarksException() throws MovieAlreadyInBookmarksException, UserNotFoundException {
//        userService.addToBookmarks(100L, "First User");
//        userService.addToBookmarks(100L, "First User");
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    @Transactional
//    public void getBookmarksFromNotExistingUser() throws UserNotFoundException {
//        userService.getBookmarks("Not existing");
//    }
//
//    @Test
//    @Transactional
//    public void getBookmarksFromUserWithoutBookmarks() throws UserNotFoundException {
//        Set<Movie> movieDtos = userService.getBookmarks("Seventh User");
//        assertEquals(0, movieDtos.size());
//    }
//
//    @Test
//    @Transactional
//    public void getBookmarksUserHasMultipleBookmarks() throws UserNotFoundException {
//        Set<Movie> movieDtos = userService.getBookmarks("First User");
//        assertEquals(2, movieDtos.size());
//    }
//
//
//    @Test
//    @Transactional
//    public void insertIntoBookMarksSuccess() throws UserNotFoundException, MovieAlreadyInBookmarksException {
//        assertEquals(1, userService.addToBookmarks(1L, "Seventh User").size());
//    }
//
//    @Test
//    public void registerSuccess() throws UserAlreadyInDatabaseException {
//        UserDto userDto = new UserDto();
//        userDto.setUsername("test");
//        userDto.setPassword("test1234");
//        HttpStatus httpStatus = userService.register(userDto);
//        assertEquals(HttpStatus.CREATED, httpStatus);
//    }
//    @Test(expected = MyBadRequestException.class)
//    public void registerSuccessNameIsNullThrowsException() throws UserAlreadyInDatabaseException {
//        UserDto userDto = new UserDto();
//        userDto.setPassword("test");
//        userService.register(userDto);
//    }
//
//    @Test(expected = MyBadRequestException.class)
//    public void registerPasswordIsNullThrowsException() throws UserAlreadyInDatabaseException {
//        UserDto userDto = new UserDto();
//        userDto.setUsername("Test");
//        userService.register(userDto);
//    }
//    @Test(expected = MyBadRequestException.class)
//    public void registerUsernameEmptyStringThrowsException() throws UserAlreadyInDatabaseException {
//        UserDto userDto = new UserDto();
//        userDto.setUsername("");
//        userDto.setPassword("test");
//        userService.register(userDto);
//    }
//
//    @Test(expected = MyBadRequestException.class)
//    public void registerPasswordEmptyStringThrowsException() throws UserAlreadyInDatabaseException {
//        UserDto userDto = new UserDto();
//        userDto.setUsername("Test");
//        userDto.setPassword("");
//        userService.register(userDto);
//    }
//    @Test(expected = UserAlreadyInDatabaseException.class)
//    public void registerUserAlreadyExists() throws UserAlreadyInDatabaseException {
//        UserDto userDto = new UserDto();
//        userDto.setUsername("Test");
//        userDto.setPassword("Test");
//        HttpStatus status = userService.register(userDto);
//        assertEquals(HttpStatus.CREATED, status);
//        userService.register(userDto);
//    }
}
