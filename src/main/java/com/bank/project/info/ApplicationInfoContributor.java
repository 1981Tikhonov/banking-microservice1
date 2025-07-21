package com.bank.project.info;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApplicationInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> details = new HashMap<>();
        
        // Add application info
        details.put("name", "Banking Service");
        details.put("version", "1.0.0");
        details.put("environment", "development");
        
        // Add system info
        try {
            details.put("hostname", InetAddress.getLocalHost().getHostName());
            details.put("ip", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            details.put("hostname", "unknown");
        }
        
        // Add JVM info
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> jvmDetails = new HashMap<>();
        jvmDetails.put("version", System.getProperty("java.version"));
        jvmDetails.put("vendor", System.getProperty("java.vendor"));
        jvmDetails.put("availableProcessors", runtime.availableProcessors());
        jvmDetails.put("totalMemory", runtime.totalMemory() / (1024 * 1024) + " MB");
        jvmDetails.put("freeMemory", runtime.freeMemory() / (1024 * 1024) + " MB");
        jvmDetails.put("maxMemory", runtime.maxMemory() / (1024 * 1024) + " MB");
        
        details.put("jvm", jvmDetails);
        
        builder.withDetails(details);
    }
}
