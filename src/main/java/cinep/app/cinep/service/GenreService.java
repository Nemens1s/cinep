package cinep.app.cinep.service;


import cinep.app.cinep.model.Genre;
import cinep.app.cinep.model.Movie;
import cinep.app.cinep.pojo.MovieData;
import cinep.app.cinep.repository.GenreRepository;
import cinep.app.cinep.service.utilities.SimilarityComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> findUniqueGenres() {
        return genreRepository.findGenres();
    }

    public void updateGenres(List<Movie> movies, Map<String, MovieData> movieDataMap) {
        List<Genre> genres = new ArrayList<>();
        genreRepository.deleteAll();
        for (Movie movie : movies) {
            Optional<String> title = movieDataMap.keySet().stream()
                    .filter(s -> SimilarityComparator.similarity(s, movie.getOriginalTitle()))
                    .findFirst();
            if (title.isPresent()) {
                MovieData data = movieDataMap.get(title.get());
                addGenreToList(genres,data.getEstGenres().split(",") ,movie, "est");
                addGenreToList(genres,data.getEngGenres().split(",") ,movie, "eng");
                addGenreToList(genres,data.getRusGenres().split(",") ,movie, "rus");
            }
        }
        genreRepository.saveAll(genres);
    }

    private void addGenreToList (List<Genre> genres, String[] genresAsStrings, Movie movie, String lang) {
        for (String description : genresAsStrings) {
            description = description.trim();
            description = description.substring(0, 1).toUpperCase() + description.substring(1);
            if (description.equalsIgnoreCase("фильм ужасов")) {
                description = "Ужасы";
            }
            genres.add(new Genre(description, movie, lang));
        }
    }

}
