package cinep.app.cinep.security;

import cinep.app.cinep.model.User;
import cinep.app.cinep.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CinepUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User with " + username + " was not found");
        }

        return new CinepUser(user.getUsername(), user.getPassword(), getAuthorities(user), user.getRole(), user.getId(), user.getBookmarks());
    }


    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        return getRoles(user)
                .map(Role::toSpringRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    private Stream<Role> getRoles(User user) {
        if (user.getRole().isAdmin()) {
            return Arrays.stream(Role.values());
        }
        return Stream.of(user.getRole());
    }


}
