package cinep.app.cinep.service;

import cinep.app.cinep.dto.LoginDetails;
import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.exceptions.*;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.model.User;
import cinep.app.cinep.repository.MovieRepository;
import cinep.app.cinep.repository.UserRepository;
import cinep.app.cinep.security.CinepUser;
import cinep.app.cinep.security.CinepUserDetailsService;
import cinep.app.cinep.security.JwtTokenProvider;
import cinep.app.cinep.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MovieRepository movieRepository;
    private final AuthenticationManager authenticationManager;
    private final CinepUserDetailsService cinepUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       MovieRepository movieRepository, AuthenticationManager authenticationManager, CinepUserDetailsService cinepUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.movieRepository = movieRepository;
        this.authenticationManager = authenticationManager;
        this.cinepUserDetailsService = cinepUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public Set<MovieDto> getBookmarks(String username)  {
        User user = userRepository.findByUsername(username);
        return user.getBookmarks().stream().map(MovieDto::new).collect(Collectors.toSet());
    }

    public Set<MovieDto> addToBookmarks(MovieDto movieDto, String username) throws  MovieAlreadyInBookmarksException {
        User user = userRepository.findByUsername(username);
        Movie movie = movieRepository.findById(movieDto.getId()).orElseThrow(MyBadRequestException::new);
        if (user.getBookmarks().contains(movie)) {
            throw new MovieAlreadyInBookmarksException("This movie is already in bookmarks");
        }
        user.getBookmarks().add(movie);
        userRepository.save(user);
        return user.getBookmarks().stream().map(MovieDto::new).collect(Collectors.toSet());
    }

    public Set<MovieDto> removeBookmark(MovieDto movieDto, String username) {
        User user = userRepository.findByUsername(username);
        Movie movie = movieRepository.findById(movieDto.getId()).orElseThrow(MyBadRequestException::new);
        user.getBookmarks().remove(movie);
        userRepository.save(user);
        return user.getBookmarks().stream().map(MovieDto::new).collect(Collectors.toSet());
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

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return HttpStatus.CREATED;
    }

    public LoginDetails login (UserDto userDto) {
        if (userDto.getUsername()== null) {
            throw new MyBadRequestException();
        }
        if (userDto.getPassword() == null) {
            throw new MyBadRequestException();
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        final UserDetails userDetails = cinepUserDetailsService.loadUserByUsername(userDto.getUsername());
        final String token = jwtTokenProvider.generateToken(userDetails);
        CinepUser cinepUser = (CinepUser) userDetails;
        return new LoginDetails(cinepUser.getUsername(), token, cinepUser.getRole(),  toAuthorities(cinepUser));
    }

    private List<String> toAuthorities(CinepUser cinepUser) {
        return cinepUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }


}
