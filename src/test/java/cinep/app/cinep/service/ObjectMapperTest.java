package cinep.app.cinep.service;

import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.model.User;
import cinep.app.cinep.service.utilities.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ObjectMapperTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testConvertUserToDtoSuccess() {
        User user = new User();
        user.setUsername("Test");
        UserDto userDto = objectMapper.convertUserToDto(user);
        assertEquals("Test", userDto.getUsername());
    }

    @Test
    public void testConvertDtoToUserSuccess() {
        UserDto userDto = new UserDto();
        userDto.setUsername("Test");
        User user = objectMapper.convertDtoToUser(userDto);
        assertEquals("Test", user.getUsername());
    }

}
