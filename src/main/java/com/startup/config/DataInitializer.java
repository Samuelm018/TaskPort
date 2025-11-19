package com.startup.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public void run(String... args) throws Exception {
        // Load and execute schema.sql to ensure all tables exist, then seed admin
        try {
            ClassPathResource resource = new ClassPathResource("db/schema.sql");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String sql = reader.lines().collect(Collectors.joining("\n"));
                    // Split statements on semicolon and execute non-empty statements
                    for (String stmt : sql.split(";")) {
                        String s = stmt.trim();
                        if (!s.isEmpty()) {
                            try {
                                jdbc.execute(s + ";");
                            } catch (Exception e) {
                                // Log and continue - some statements may not be applicable
                                System.err.println("DataInitializer: failed to execute statement: " + e.getMessage());
                            }
                        }
                    }
                    System.out.println("Executed db/schema.sql");
                }
            } else {
                System.out.println("No db/schema.sql found on classpath");
            }

            // Now seed admin if missing
            Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM admin WHERE gmail = ?", Integer.class, "samuelm99729.work@gmail.com");
            if (count == null || count == 0) {
                String hashed = BCrypt.hashpw("Sam996525", BCrypt.gensalt());
                jdbc.update("INSERT INTO admin (name, gmail, password_hash) VALUES (?, ?, ?)", "Administrator", "samuelm99729.work@gmail.com", hashed);
                System.out.println("Seeded admin user: samuelm99729.work@gmail.com");
            }

        } catch (Exception ex) {
            System.err.println("DataInitializer: failed to initialize schema/admin: " + ex.getMessage());
        }
    }
}
