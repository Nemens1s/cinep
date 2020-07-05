package cinep.app.cinep.controller;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.exceptions.MovieTitleNotFoundException;
import cinep.app.cinep.exceptions.TheatreNotSupportedException;
import cinep.app.cinep.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = {"/movies", "/films"})
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/all")
    public @ResponseBody
    List<MovieDto> findAll() {
        return searchService.findAllMovies();
    }

    @GetMapping("/theatre")
    public @ResponseBody
    List<MovieDto> searchByTheatreName(@RequestParam String theatre) throws TheatreNotSupportedException {
        return searchService.findByTheatre(theatre);
    }

    @GetMapping("/title")
    public @ResponseBody
    List<MovieDto> searchByTitle(@RequestParam String title,
                                 @RequestParam (defaultValue = "eng", required = false) String lang) throws MovieTitleNotFoundException {
        return searchService.findByTitle(title, lang);
    }


    @GetMapping("/time")
    public @ResponseBody
    List<MovieDto> searchByTimeAndDate(@RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalTime sTime,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") LocalTime eTime,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate sDate,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate eDate) {
        return searchService.findByTimeAndDate(sTime, eTime, sDate, eDate);
    }

    @GetMapping("/genre")
    public @ResponseBody
    List<MovieDto> searchByGenres(@RequestParam(name = "g") List<String> genreDesc, @RequestParam String lang) {
        return searchService.findByGenres(genreDesc, lang);
    }
}
