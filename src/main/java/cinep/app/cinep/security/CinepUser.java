package cinep.app.cinep.security;

import cinep.app.cinep.model.Movie;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

@Getter
public class CinepUser extends User {

    private Long id;
    private Role role;
    private Set<Long> bookmarks;

    public CinepUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Role role, Long id, Set<Long> bookmarks) {
        super(username, password, authorities);
        this.role = role;
        this.id = id;
        this.bookmarks = bookmarks;
    }

    public CinepUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
