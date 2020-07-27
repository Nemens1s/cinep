package cinep.app.cinep.controller;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = {"/movies", "/films"})
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping()
    public @ResponseBody
    Page<MovieDto> search(@RequestParam Map<String, String> searchRequest, Pageable pageable) {
        return searchService.search(searchRequest, pageable);
    }

    /*
    This part of code is replaced with the method that is above because adding new parameters for searching became
    difficult as well as there is no option to search by different parameters.
    For example if a person wants to see "Star Wars" but he prefers two or three cinemas then he cant do it in a one
    search
    @GetMapping("/all")
    public @ResponseBody
    List<MovieDto> findAll() {
        return searchService.findAllMovies();
    }

    @GetMapping("/theatre")
    public @ResponseBody
    List<MovieDto> searchByTheatreName(@RequestParam List<String> theatres) throws TheatreNotSupportedException {
        return searchService.findByTheatre(theatres);
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
    */
}
