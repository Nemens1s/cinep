package cinep.app.cinep.controller;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.exceptions.MovieTitleNotFoundException;
import cinep.app.cinep.exceptions.TheatreNotSupportedException;
import cinep.app.cinep.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/movies", "/films"})
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping(value = "/all")
    public @ResponseBody List<MovieDto> findAll(){
        return searchService.findAllMovies();
    }

    @GetMapping("/theatre")
    public @ResponseBody List<MovieDto> searchByTheatreName(@RequestParam String theatre) throws TheatreNotSupportedException {
        return searchService.findByTheatre(theatre);
    }

    @GetMapping("/title")
    public @ResponseBody List<MovieDto> searchByTitle(@RequestParam String title) throws MovieTitleNotFoundException {
        return searchService.findByTitle(title);
    }

    @GetMapping("/time")
    public @ResponseBody List<MovieDto> searchByTime(@RequestParam String time){
        return searchService.findByTime(time);
    }
}
