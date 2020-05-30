package com.example.cloudsqldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class CloudSqlDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSqlDemoApplication.class, args);
    }

    @RestController
    static class CustomerController {

        private final JdbcTemplate jdbcTemplate;

        public CustomerController(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @GetMapping("/customers")
        public List<String> getCustomers() {
            return this.jdbcTemplate.queryForList("SELECT * FROM customers").stream()
                    .map((m) -> m.values().toString())
                    .collect(Collectors.toList());
        }
    }

}