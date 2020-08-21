package cinep.app.cinep.controller;

import cinep.app.cinep.dto.LoginDetails;
import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.exceptions.MovieAlreadyInBookmarksException;
import cinep.app.cinep.exceptions.UserAlreadyInDatabaseException;
import cinep.app.cinep.exceptions.UserNotFoundException;
import cinep.app.cinep.security.*;
import cinep.app.cinep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.Set;


@RestController
@RequestMapping(value = "/profile")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/bookmarks/show")
    @Secured(Roles.USER)
    public @ResponseBody Set<MovieDto> viewBookmarks() throws UserNotFoundException {
        CinepUser cinepUser = current();
        return userService.getBookmarks(cinepUser.getUsername());
    }


    @PostMapping(value = "/bookmarks/add")
    @Secured(Roles.USER)
    public Set<MovieDto> addToBookmarks(@RequestBody MovieDto movieDto) throws MovieAlreadyInBookmarksException {
        CinepUser cinepUser = current();
        return userService.addToBookmarks(movieDto, cinepUser.getUsername());
    }

    @DeleteMapping(value = "/bookmarks/delete")
    @Secured(Roles.USER)
    public ResponseEntity<Set<MovieDto>> removeBookmark(@RequestBody MovieDto movieDto){
        CinepUser cinepUser = current();
        return new ResponseEntity<>(userService.removeBookmark(movieDto, cinepUser.getUsername()), HttpStatus.RESET_CONTENT);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) throws UserAlreadyInDatabaseException {
        HttpStatus status = userService.register(userDto);
        if(status.equals(HttpStatus.CREATED)){
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public LoginDetails login(@RequestBody UserDto userDto){
        return userService.login(userDto);
    }

    private CinepUser current(){
        return UserSessionHolder.getLoggedInUser();
    }



}
