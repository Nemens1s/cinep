package cinep.app.cinep.service;

import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.exceptions.MovieAlreadyInBookmarksException;
import cinep.app.cinep.exceptions.MyBadRequestException;
import cinep.app.cinep.exceptions.UserAlreadyInDatabaseException;
import cinep.app.cinep.exceptions.UserNotFoundException;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.User;
import cinep.app.cinep.repository.MovieRepository;
import cinep.app.cinep.repository.UserRepository;
import cinep.app.cinep.security.Role;
import cinep.app.cinep.service.utilities.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final MovieRepository movieRepository;
    @Autowired
    public UserService(UserRepository userRepository, ObjectMapper objectMapper, PasswordEncoder passwordEncoder,
                       MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.movieRepository = movieRepository;
    }


    public Set<Movie> getBookmarks(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UserNotFoundException("There is no user with: " + username);
        }
        return fetchBookmarks(user.getBookmarks());
    }

    public Set<Movie> addToBookmarks(Long movieId, String username) throws UserNotFoundException, MovieAlreadyInBookmarksException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UserNotFoundException("There is no user with: " + username);
        }
        if (user.getBookmarks().contains(movieId)){
            throw new MovieAlreadyInBookmarksException("You already have this movie in your bookmarks");
        }
        user.getBookmarks().add(movieId);
        userRepository.save(user);
        return fetchBookmarks(user.getBookmarks());
    }

    public Long removeBookmark(Long id, String username) {
        User user = userRepository.findByUsername(username);
        user.getBookmarks().remove(id);
        userRepository.save(user);
        return id;
    }

    public HttpStatus register(UserDto userDto) throws UserAlreadyInDatabaseException {

        if (userDto.getUsername() == null || userDto.getUsername().equalsIgnoreCase("")
                || userDto.getPassword() == null || userDto.getPassword().equalsIgnoreCase("")){
            throw new MyBadRequestException();
        }
        User potentialUser = userRepository.findByUsername(userDto.getUsername());
        if(potentialUser != null){
            throw new UserAlreadyInDatabaseException("There is already a user with username: " + userDto.getUsername());
        }

        User user = objectMapper.convertDtoToUser(userDto);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return HttpStatus.CREATED;
    }

    public UserDto deleteUser(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UserNotFoundException("User was not found");
        }
        userRepository.delete(user);
        return objectMapper.convertUserToDto(user);
    }

    private Set<Movie> fetchBookmarks(Set<Long> movieIDs) {
        Set<Movie> movies = new LinkedHashSet<>();
        for (Long id : movieIDs) {
            Optional<Movie> movie = movieRepository.findById(id);
            movie.ifPresent(movies::add);
        }
        return movies;
    }
}
