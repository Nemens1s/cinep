package cinep.app.cinep.service;

import cinep.app.cinep.dto.MovieDto;
import cinep.app.cinep.exceptions.MovieTitleNotFoundException;
import cinep.app.cinep.exceptions.TheatreNotSupportedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;
@RunWith(SpringRunner.class)
@SpringBootTest

public class SearchServiceTest {

    @Autowired
    SearchService searchService;

    @Autowired
    JdbcTemplate jdbc;

    private static final String CREATE_DB = "create-data.sql";
    private static final String DROP_DB = "drop-data.sql";

    @Before
    public void before() throws SQLException {
        try (Connection connection = Objects.requireNonNull(jdbc.getDataSource()).getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_DB));
        }
    }

    @After
    public void after() throws SQLException {
        try (Connection connection = Objects.requireNonNull(jdbc.getDataSource()).getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_DB));
        }
    }

    @Test
    public void searchByTheatreGetForumCinemasScheduleMovieTitleNotNull() throws  TheatreNotSupportedException {
        String theatre = "Coca-cola-plaza";

        List<MovieDto> movieDtos = searchService.findByTheatre(theatre);

        assertNotNull(movieDtos.get(0).getOriginalTitle());
    }

    @Test
    public void searchByTheatreT1Success() throws TheatreNotSupportedException {
        assertNotNull(searchService.findByTheatre("T1").get(0));
    }

    @Test
    public void searchByTheatreKosmosSuccess() throws TheatreNotSupportedException {
        assertNotNull(searchService.findByTheatre("Kosmos").get(0));
    }

    @Test
    public void searchByTheatreApolloSuccess() throws TheatreNotSupportedException {
        assertNotNull(searchService.findByTheatre("Apollo").get(0));
    }

    @Test
    public void searchByTheatreViimsiSuccess() throws TheatreNotSupportedException {
        assertNotNull(searchService.findByTheatre("Viimsi").get(0));
    }

    @Test
    public void searchByTheatreArtisSuccess() throws TheatreNotSupportedException {
        assertNotNull(searchService.findByTheatre("Artis").get(0));
    }

    @Test(expected = TheatreNotSupportedException.class)
    public void searchByTheatreThrowsException() throws TheatreNotSupportedException {
        String theatre = "wrong name";
        searchService.findByTheatre(theatre);
    }

    @Test
    @Ignore
    public void searchByTitleRatingNotNull() throws MovieTitleNotFoundException {
        assertNotNull(searchService.findByTitle("Predator").get(0).getUserRating());
    }

    @Test(expected = MovieTitleNotFoundException.class)
    public void searchByMovieGetTitleNotNull() throws MovieTitleNotFoundException {
        String movie = "Titanic";
        searchService.findByTitle(movie);
    }

}
