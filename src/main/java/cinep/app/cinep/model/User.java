package cinep.app.cinep.model;

import cinep.app.cinep.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name = "user_bookmarks", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "movie_title")
    @JsonIgnore
    private Set<String> bookmarks;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String password;

}
