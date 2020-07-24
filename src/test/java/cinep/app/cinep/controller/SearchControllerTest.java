package cinep.app.cinep.controller;

import cinep.app.cinep.dto.MovieDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchControllerTest {

    @Autowired
    JdbcTemplate jdbc;

    private static final String CREATE_DB = "create-data.sql";
    private static final String DROP_DB = "drop-data.sql";

    @Before
    public void before() throws SQLException {
        try (Connection connection = jdbc.getDataSource().getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_DB));
        }
    }

    @After
    public void after() throws SQLException {
        try (Connection connection = jdbc.getDataSource().getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_DB));
        }
    }

    public static final ParameterizedTypeReference<List<MovieDto>> MOVIES_LIST = new ParameterizedTypeReference<List<MovieDto>>() {
    };

    @Resource
    private TestRestTemplate template;

//    @Test
//    public void searchAllMoviesReturnsListOfMovies(){
//        ResponseEntity<List<MovieDto>> entity = template.exchange("/movies/all", HttpMethod.GET, null, MOVIES_LIST);
//        assertEquals(HttpStatus.OK, entity.getStatusCode());
//        List<MovieDto> movieDtos = entity.getBody();
//        assertTrue(isNotEmpty(movieDtos));
//    }
//
//    @Test
//    public void searchByTitleReturnsListOfMovies(){
//        ResponseEntity<List<MovieDto>> entity = template.exchange("/movies/title?title=Star Wars", HttpMethod.GET, null, MOVIES_LIST);
//        assertEquals(HttpStatus.OK, entity.getStatusCode());
//        List<MovieDto> movieDtos = entity.getBody();
//        assertTrue(isNotEmpty(movieDtos));
//        assertTrue(movieDtos.stream().anyMatch(movieDto -> movieDto.getOriginalTitle().equalsIgnoreCase("Star Wars")));
//    }
//
//    @Test
//    public void searchByTheatreReturnsListOfMovies(){
//        ResponseEntity<List<MovieDto>> entity = template.exchange("/movies/theatre?theatre=Kosmos", HttpMethod.GET, null, MOVIES_LIST);
//        assertEquals(HttpStatus.OK, entity.getStatusCode());
//        List<MovieDto> movieDtos = entity.getBody();
//        assertTrue(isNotEmpty(movieDtos));
//    }

}
