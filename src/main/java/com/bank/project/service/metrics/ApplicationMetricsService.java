package com.bank.project.service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ApplicationMetricsService {
    
    private final Counter accountCreatedCounter;
    private final Counter transactionProcessedCounter;
    private final Timer transactionProcessingTimer;
    
    public ApplicationMetricsService(MeterRegistry registry) {
        this.accountCreatedCounter = Counter.builder("bank.account.created")
                .description("Total number of accounts created")
                .register(registry);
                
        this.transactionProcessedCounter = Counter.builder("bank.transaction.processed")
                .description("Total number of transactions processed")
                .tag("type", "all")
                .register(registry);
                
        this.transactionProcessingTimer = Timer.builder("bank.transaction.processing.time")
                .description("Time taken to process transactions")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(registry);
    }
    
    public void incrementAccountCreated() {
        accountCreatedCounter.increment();
    }
    
    public void incrementTransactionsProcessed(String transactionType) {
        transactionProcessedCounter.increment();
    }
    
    public void recordTransactionProcessingTime(Runnable operation) {
        transactionProcessingTimer.record(operation);
    }
    
    public void recordTransactionProcessingTime(long duration, TimeUnit unit) {
        transactionProcessingTimer.record(duration, unit);
    }
}
