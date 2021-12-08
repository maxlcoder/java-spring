package pers.maxlcoder.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import pers.maxlcoder.demo.service.UserService;

import javax.sql.DataSource;
import java.time.ZoneId;

@Configuration
@ComponentScan
@PropertySource("application.properties")
@PropertySource("jdbc.properties")
@EnableAspectJAutoProxy
public class AppConfig {

    @Value("${jdbc.url}")
    String jdbcUrl;

    @Value("${jdbc.username}")
    String jdbcUsername;

    @Value("${jdbc.password}")
    String jdbcPassword;


    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    TransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }


//    @Value("${test:helo}")
//    String test;
//
//    @Bean("hello")
//    String createHello() {
//        return test;
//    }
//
//    @Bean("z")
//    ZoneId createZoneOfZ() {
//        return ZoneId.of("Z");
//    }
//
//    @Bean
//    @Qualifier("utc8")
//    ZoneId createZoneOfUTC8() {
//        return ZoneId.of("UTC+08:00");
//    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        // 插入Root:
        try {
            userService.register("root@example.com", "password3", "root");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        ((ConfigurableApplicationContext) context).close();
    }
}
