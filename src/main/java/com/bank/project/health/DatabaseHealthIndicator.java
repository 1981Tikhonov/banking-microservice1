package com.bank.project.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DatabaseHealthIndicator extends AbstractHealthIndicator {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            Map<String, Object> details = new HashMap<>();
            
            // Basic connection check
            boolean isValid = connection.isValid(5);
            details.put("connection.valid", isValid);
            
            // Get some database stats
            if (isValid) {
                Integer sessionCount = jdbcTemplate.queryForObject(
                    "SELECT count(*) FROM pg_stat_activity", Integer.class);
                details.put("active_sessions", sessionCount);
                
                Integer maxConnections = jdbcTemplate.queryForObject(
                    "SHOW max_connections", Integer.class);
                details.put("max_connections", maxConnections);
                
                // Add more database-specific metrics as needed
            }
            
            builder.up().withDetails(details);
            
        } catch (SQLException e) {
            builder.down(e);
        }
    }
}
