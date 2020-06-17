package cinep.app.cinep.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Setter
@Getter
@NoArgsConstructor

public class UserDto {
    private String username;
    private String password;
    private Set<Long> bookmarks;
}
