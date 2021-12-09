package pers.maxlcoder.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS `users` ("
                + "`id` BIGINT(20) NOT NULL AUTO_INCREMENT, "
                + "`email` VARCHAR(100) NOT NULL, "
                + "`password` VARCHAR(100) NOT NULL, "
                + "`name` VARCHAR(100) NOT NULL, "
                + "PRIMARY KEY (`id`)"
                + ") ENGINE=InnoDB");
    }
}
