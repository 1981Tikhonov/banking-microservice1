package com.bank.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
    private RateLimit rateLimit = new RateLimit();
    private Headers headers = new Headers();
    private Cors cors = new Cors();
    private Audit audit = new Audit();
    private Suspicious suspicious = new Suspicious();

    @Data
    public static class RateLimit {
        private boolean enabled = true;
        private int requestsPerMinute = 100;
        private int requestsPerHour = 1000;
        private int requestsPerDay = 5000;
    }

    @Data
    public static class Headers {
        private boolean enabled = true;
        private String contentSecurityPolicy = "default-src 'self'";
        private long hsts = 31536000; // 1 year in seconds
    }

    @Data
    public static class Cors {
        private String allowedOrigins = "*";
        private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS";
        private String allowedHeaders = "*";
        private boolean allowCredentials = true;
    }

    @Data
    public static class Audit {
        private boolean enabled = true;
        private List<String> sensitiveParams = List.of("password", "secret", "token", "ssn", "creditCard");
    }

    @Data
    public static class Suspicious {
        private Login login = new Login();
        private Transaction transaction = new Transaction();
        private List<String> ipCountries = List.of("CN", "RU", "KP", "IR", "SY");
    }

    @Data
    public static class Login {
        private int attempts = 5;
        private int minutes = 60;
    }

    @Data
    public static class Transaction {
        private double amount = 10000.0;
        private int count = 5;
        private int minutes = 5;
    }
}
