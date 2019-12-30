package cinep.app.cinep.controller;

import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.model.User;
import cinep.app.cinep.repository.UserRepository;
import cinep.app.cinep.security.JwtTokenProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Resource
    private TestRestTemplate template;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Resource
    private UserRepository userRepository;

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

    private static final ParameterizedTypeReference<Set<String>> BOOKMARKS = new ParameterizedTypeReference<Set<String>>() {
    };

    @Test
    @SuppressWarnings("unchecked")
    public void getBookMarksSuccess(){
        String token = jwtTokenProvider.createTokenForTests("Third User");
        HttpEntity<String> entity = new HttpEntity<>(authHeader(token));

        ResponseEntity bookmarks = template.exchange("/profile/bookmarks/show", HttpMethod.GET, entity, BOOKMARKS);
        assertEquals(HttpStatus.OK, bookmarks.getStatusCode());
        Set<String> body = (Set<String>) bookmarks.getBody();
        assert body != null;
        assertEquals(2, body.size());
    }
    @Test
    public void addBookMarksSuccess(){
        String token = jwtTokenProvider.createTokenForTests("Third User");
        HttpEntity<String> entity = new HttpEntity<>("New Title", authHeader(token));
        ResponseEntity responseEntity = template.exchange("/profile/bookmarks/add", HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void registerSuccess(){
        UserDto userDto = new UserDto();
        userDto.setUsername("Test");
        userDto.setPassword("Test");
        HttpEntity<UserDto> entity = new HttpEntity<>(userDto);
        ResponseEntity responseEntity = template.exchange("/profile/register", HttpMethod.POST, entity, UserDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User user = userRepository.findByUsername("Test");
        assertNotNull(user);
    }

    @Test
    public void loginNoUserInDatabase(){
        UserDto userDto = new UserDto();
        userDto.setUsername("First User");
        userDto.setPassword("QWERTY");
        HttpEntity<UserDto> entity = new HttpEntity<>(userDto);
        ResponseEntity responseEntity = template.exchange("/profile/login", HttpMethod.POST, entity, UserDto.class);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void loginSuccess(){
        registerSuccess();
        UserDto userDto = new UserDto();
        userDto.setUsername("Test");
        userDto.setPassword("Test");
        HttpEntity<UserDto> entity = new HttpEntity<>(userDto);
        ResponseEntity responseEntity = template.exchange("/profile/login", HttpMethod.POST, entity, UserDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    private HttpHeaders authHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

}
