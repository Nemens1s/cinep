package cinep.app.cinep.repository;

import cinep.app.cinep.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);


    @Query(value = "SELECT * FROM USER_BOOKMARKS ub WHERE ub.user_id = ?1" , nativeQuery = true)
    Set<String> getBookmarks(Long id);

}
