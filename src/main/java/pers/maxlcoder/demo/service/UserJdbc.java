package pers.maxlcoder.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class UserJdbc {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserClass getUserById(long id) {
        return jdbcTemplate.execute("SELECT * FROM users WHERE id = ?", (PreparedStatement ps) -> {
            ps.setObject(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserClass(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("name")
                    );
                }
                throw new RuntimeException("user not found by id.");
            }
        });
    }
}
