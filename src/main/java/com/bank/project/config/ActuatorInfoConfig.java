package com.bank.project.config;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorInfoConfig {

    @Bean
    public InfoContributor sslInfo() {
        // Возвращаем пустой InfoContributor, чтобы устранить проблему
        return builder -> {};
    }
}
