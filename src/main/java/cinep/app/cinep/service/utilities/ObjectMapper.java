package cinep.app.cinep.service.utilities;

import cinep.app.cinep.dto.UserDto;
import cinep.app.cinep.model.User;
import org.springframework.stereotype.Service;


@Service
public class ObjectMapper {


    public UserDto convertUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setBookmarks(user.getBookmarks());
        return userDto;
    }


    public User convertDtoToUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setBookmarks(userDto.getBookmarks());
        return user;
    }

}
