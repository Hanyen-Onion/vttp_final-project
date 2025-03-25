package vttp.batch_b.min_project.server.repository;

import java.sql.ResultSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch_b.min_project.server.models.User;
import static vttp.batch_b.min_project.server.repository.Sql.SQL_CREATE_USER;
import static vttp.batch_b.min_project.server.repository.Sql.SQL_SELECT_USER_BY_EMAIL;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate template;

    public Optional<User> findUserByEmail(String email) {
        Optional<User> opt = template.query(SQL_SELECT_USER_BY_EMAIL, 
            (ResultSet rs) -> {
                if(rs.next())
                    return Optional.of(User.populate(rs));
                return Optional.empty();
            }, email);
        return opt;
    }

    public Boolean createNewUser(User user) {
        int added = template.update(SQL_CREATE_USER, 
            user.getEmail(),
            user.getUsername(),
            user.getPassword(),
            user.getLocation(),
            user.getTimezone(),
            user.getCurrency()
        );
        return added > 0;
    }
}
