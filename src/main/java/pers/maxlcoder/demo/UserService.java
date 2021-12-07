package pers.maxlcoder.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import pers.maxlcoder.demo.metric.MetricTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MailService mailService;

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    private List<User> users = new ArrayList<>(List.of( // users:
            new User(1, "bob@example.com", "password", "Bob"), // bob
            new User(2, "alice@example.com", "password", "Alice"), // alice
            new User(3, "tom@example.com", "password", "Tom"))); // tom

    @MetricTime("login")
    public User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                mailService.sendLoginMail(user);
                return user;
            }
        }
        throw new RuntimeException("login failed.");
    }

    public User getUser(long id) {
        return getUserById(id);
//        return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow();
    }

    public User register(String email, String password, String name) {
        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != jdbcTemplate.update((conn) -> {
            var ps = conn.prepareStatement("INSERT INTO users(email,password,name) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, email);
            ps.setObject(2, password);
            ps.setObject(3, name);
            return ps;
        }, holder)) {
            throw new RuntimeException("Insert failed.");
        }
        return new User(holder.getKey().longValue(), email, password, name);
    }

    public User getUserById(long id) {
        return jdbcTemplate.execute((Connection conn) -> {
            try (var ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
                ps.setObject(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new User(
                                rs.getLong("id"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("name"));
                    }
                }
            }
            throw new RuntimeException("user not found by id.");
        });
    }


    public User getUserByName(String name) {
        return jdbcTemplate.execute("SELECT * FROM users WHERE name = ?", (PreparedStatement ps) -> {
            ps.setObject(1, name);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"));
                }
                throw new RuntimeException("user not found by id.");
            }
        });
    }

    public User getUserByEmail(String email) {
        // 传入SQL，参数和RowMapper实例:
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?", new Object[] { email },
                (ResultSet rs, int rowNum) -> {
                    // 将ResultSet的当前行映射为一个JavaBean:
                    return new User( // new User object:
                            rs.getLong("id"), // id
                            rs.getString("email"), // email
                            rs.getString("password"), // password
                            rs.getString("name")); // name
                });
    }


    public void updateUser(User user) {
        if (1 != jdbcTemplate.update("UPDATE users SET name = ? WHERE id = ?", user.getName(), user.getId())) {
            throw new RuntimeException("Update Fail User not found By id");
        }
    }


}
