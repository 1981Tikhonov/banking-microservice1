package com.bank.project.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class MetricsConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public MeterBinder processMemoryMetrics() {
        return registry -> {
            // JVM memory metrics
            registry.gauge("jvm.memory.used", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            registry.gauge("jvm.memory.max", Runtime.getRuntime().maxMemory());
            registry.gauge("jvm.memory.committed", Runtime.getRuntime().totalMemory());
        };
    }

    @Bean
    public Timer httpRequestTimer(MeterRegistry registry) {
        return Timer.builder("http.server.requests")
                .description("Time taken to process HTTP requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(registry);
    }
}
