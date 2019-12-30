package cinep.app.cinep.dto;

import cinep.app.cinep.security.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LoginDetails {

    private String userName;
    private String token;
    private Role role;
    private List<String> roles;

}
