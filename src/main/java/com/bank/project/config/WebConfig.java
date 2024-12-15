package com.bank.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Разрешить CORS для всех запросов
                .allowedOrigins("http://localhost:8080")  // Разрешить только запросы с этого домена
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Разрешенные HTTP методы
                .allowedHeaders("*")  // Разрешить все заголовки
                .allowCredentials(true);  // Разрешить отправку cookies
    }
}
