package cinep.app.cinep.service;

import cinep.app.cinep.dto.MovieDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceTest {

    @Autowired
    SearchService searchService;

    @Autowired
    JdbcTemplate jdbc;

    private static final String CREATE_DB = "create-data.sql";
    private static final String DROP_DB = "drop-data.sql";

    private Map<String, String> request;
    private Pageable pageable;

    @Before
    public void before() throws SQLException {
        request = new HashMap<>();
        pageable = PageRequest.of(0, 20);
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
    public void testSearchNoArgumentsReturnsTwelve (){
        Page<MovieDto> result = searchService.search(request, pageable);
        assertEquals(12, result.get().count());
    }

    @Test
    public void testSearchOriginalTitleAvengers () {
        request.put("title", "Avengers");
        Page<MovieDto> result = searchService.search(request, pageable);
        assertEquals(1, result.get().count());
    }
    @Test
    public void testSearchOriginalTitleAndTheatre () {
        request.put("title", "Star Wars");
        request.put("theatre", "Coca-cola-plaza");
        Page<MovieDto> result = searchService.search(request, pageable);
        MovieDto dto = result.get().collect(Collectors.toList()).get(0);
        assertEquals("Star Wars", dto.getOriginalTitle());
        assertEquals(1, result.get().count());

    }

    @Test
    public void testSearchEstonianTitle () {
        request.put("title", "Kääbik");
        Page<MovieDto> result = searchService.search(request, pageable);
        MovieDto dto = result.get().collect(Collectors.toList()).get(0);
        assertEquals(1, result.get().count());
        assertEquals("Kääbik", dto.getEstonianTitle());
        assertEquals("Hobbit", dto.getOriginalTitle());
    }

    @Test
    public void testSearchRussianTitle () {
        request.put("title", "Пила");
        Page<MovieDto> result = searchService.search(request, pageable);
        MovieDto dto = result.get().collect(Collectors.toList()).get(0);
        assertEquals(1, result.get().count());
        assertEquals("Пила", dto.getRussianTitle());
    }

    @Test
    public void testSearchNotFullTitle () {
        request.put("title", "enger");
        Page<MovieDto> result = searchService.search(request, pageable);
        MovieDto dto = result.get().collect(Collectors.toList()).get(0);
        assertEquals(1, result.get().count());
        assertEquals("Avengers", dto.getOriginalTitle());
    }

    @Test
    public void testSearchOneGenre () {
        request.put("genre", "Horror");
        Page<MovieDto> result = searchService.search(request, pageable);
        assertEquals(2, result.get().count());
    }

    @Test
    public void testSearchTwoGenresThreeMovies () {
        request.put("genre", "Horror,Action");
        Page<MovieDto> result = searchService.search(request, pageable);
        assertEquals(3, result.get().count());
    }

    @Test
    public void testSearchTwoTheatresSevenMovies () {
        request.put("theatre", "Coca-cola-plaza,Kosmos");
        Page<MovieDto> result = searchService.search(request, pageable);
        assertEquals(7, result.get().count());
    }

    @Test
    public void testSearchStartTimeAndDate () {
        request.put("st", "15:00");
        request.put("sd", "20.07.2020");
        request.put("ed", "20.07.2020");
        Page<MovieDto> result = searchService.search(request, pageable);
        assertEquals(2, result.get().count());
    }

    @Test
    public void testSearchEndTimeAndDate () {
        request.put("et", "15:00");
        request.put("sd", "20.07.2020");
        request.put("ed", "20.07.2020");
        Page<MovieDto> result = searchService.search(request, pageable);
        assertEquals(2, result.get().count());
    }

    @Test
    public void testSearchStartDate () {
        request.put("sd", "22.07.2020");
        Page<MovieDto> result = searchService.search(request, pageable);
        assertEquals(4, result.get().count());
    }

    @Test
    public void testSearchEndDate () {
        request.put("ed", "25.07.2020");
        Page<MovieDto> result = searchService.search(request, pageable);
        assertEquals(12, result.get().count());
    }


}
