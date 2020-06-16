package cinep.app.cinep.controller;


import cinep.app.cinep.service.utilities.DatabaseRefresher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final DatabaseRefresher refresher;

    @Autowired
    public AdminController(DatabaseRefresher refresher) {
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
