package cinep.app.cinep.controller;

import cinep.app.cinep.dto.LoginDetails;
import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.exceptions.MovieAlreadyInBookmarksException;
import cinep.app.cinep.exceptions.MyBadRequestException;
import cinep.app.cinep.exceptions.UserAlreadyInDatabaseException;
import cinep.app.cinep.exceptions.UserNotFoundException;
import cinep.app.cinep.security.*;
import cinep.app.cinep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/profile")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CinepUserDetailsService cinepUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, CinepUserDetailsService cinepUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.cinepUserDetailsService = cinepUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @GetMapping(value = "/bookmarks/show")
    @Secured(Roles.USER)
    public @ResponseBody Set<String> viewBookmarks() throws UserNotFoundException {
        CinepUser cinepUser = current();
        return userService.getBookmarks(cinepUser.getUsername());
    }


    @PostMapping(value = "/bookmarks/add")
    @Secured(Roles.USER)
    public Set<String> addToBookmarks(@RequestBody String title) throws UserNotFoundException, MovieAlreadyInBookmarksException {
        CinepUser cinepUser = current();
        return userService.addToBookmarks(title, cinepUser.getUsername());
    }

    @DeleteMapping(value = "/bookmarks/delete/{title}")
    @Secured(Roles.USER)
    public HttpStatus removeBookmark(@PathVariable String title){
        CinepUser cinepUser = current();
        userService.removeBookmark(title, cinepUser.getUsername());
        return HttpStatus.OK;
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody UserDto userDto) throws UserAlreadyInDatabaseException {
        HttpStatus status = userService.register(userDto);
        if(status.equals(HttpStatus.CREATED)){
            return userDto;
        }
        return null;
    }

    @PostMapping("/login")
    public LoginDetails login(@RequestBody UserDto userDto){
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
        return new LoginDetails(cinepUser.getUsername(), token,cinepUser.getRole(),  toAuthorities(cinepUser));

    }
    @DeleteMapping("/delete/{username}")
    @Secured(Roles.ADMIN)
    public UserDto delete(@PathVariable String username) throws UserNotFoundException {
        return userService.deleteUser(username);
    }

    private CinepUser current(){
        return UserSessionHolder.getLoggedInUser();
    }

    private List<String> toAuthorities(CinepUser cinepUser) {
        return cinepUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }


}
