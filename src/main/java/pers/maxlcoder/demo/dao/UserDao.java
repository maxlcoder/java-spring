package pers.maxlcoder.demo.dao;


import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pers.maxlcoder.demo.service.UserClass;

@Component
@Transactional
public class UserDao extends AbstractDao<UserClass> {

    public UserClass fetchUserByEmail(String email) {
        List<UserClass> users = getJdbcTemplate().query("SELECT * FROM users WHERE email = ?", new Object[] { email },
                (ResultSet rs, int rowNum) -> {
                    return new UserClass( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                });
        return users.isEmpty() ? null : users.get(0);
    }

    public UserClass getUserByEmail(String email) {
        return getJdbcTemplate().queryForObject("SELECT * FROM users WHERE email = ?", new Object[] { email },
                (ResultSet rs, int rowNum) -> {
                    return new UserClass( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                });
    }

    public UserClass login(String email, String password) {
        UserClass user = getUserByEmail(email);
        if (user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("login failed.");
    }

    public UserClass createUser(String email, String password, String name) {
        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != getJdbcTemplate().update((conn) -> {
            var ps = conn.prepareStatement("INSERT INTO users(email, password, name) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, email);
            ps.setObject(2, password);
            ps.setObject(3, name);
            return ps;
        }, holder)) {
            throw new RuntimeException("Insert failed.");
        }
        if ("root".equalsIgnoreCase(name)) {
            throw new RuntimeException("Invalid name, will rollback...");
        }
        return new UserClass(holder.getKey().longValue(), email, password, name);
    }

    public void updateUser(UserClass user) {
        if (1 != getJdbcTemplate().update("UPDATE user SET name = ? WHERE id=?", user.getName(), user.getId())) {
            throw new RuntimeException("User not found by id");
        }
    }

}
