package cinep.app.cinep.service;


import cinep.app.cinep.model.Genre;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.pojo.MovieData;
import cinep.app.cinep.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public void updateGenres(List<Movie> movies, Map<String, MovieData> movieDataMap) {
        List<Genre> genres = new ArrayList<>();
        genreRepository.deleteAll();
        for (Movie movie : movies) {
            if (movieDataMap.containsKey(movie.getOriginalTitle())) {
                MovieData data = movieDataMap.get(movie.getOriginalTitle());
                addGenreToList(genres,data.getEstGenres().split(",") ,movie);
                addGenreToList(genres,data.getEngGenres().split(",") ,movie);
                addGenreToList(genres,data.getRusGenres().split(",") ,movie);
            }
        }
        genreRepository.saveAll(genres);
    }

    private void addGenreToList (List<Genre> genres, String[] genresAsStrings, Movie movie) {
        for (String description : genresAsStrings) {
            description = description.trim();
            description = description.substring(0, 1).toUpperCase() + description.substring(1);
            if (description.equalsIgnoreCase("фильм ужасов")) {
                description = "Ужасы";
            }
            genres.add(new Genre(description, movie));
        }
    }

}
