package cinep.app.cinep.controller;


import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.exceptions.UserNotFoundException;
import cinep.app.cinep.service.UserService;
import cinep.app.cinep.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final MovieService refresher;

    @Autowired
    public AdminController(MovieService refresher) {
        this.refresher = refresher;
    }

    @GetMapping("/fetch")
    public HttpStatus fetchMovies() {
        refresher.fetchMovies();
        return HttpStatus.OK;
    }

    @GetMapping("/refresh")
    public HttpStatus refreshMovies() {
        refresher.refresh();
        return HttpStatus.OK;
    }

}
