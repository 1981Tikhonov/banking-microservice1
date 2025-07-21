package com.bank.project.security;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@Slf4j
public class IP2Location {
    private String dbPath;
    private boolean initialized = true;
    private DatabaseReader databaseReader;

    public IP2Location(String dbPath) {
        this.dbPath = dbPath;
        DatabaseReader reader       = null;
        try {
            reader = new DatabaseReader.Builder(new File(dbPath)).build();
        } catch (IOException e) {
            log.error("Failed to initialize DatabaseReader: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize DatabaseReader", e);
        }
        this.databaseReader = reader;
        log.info("IP2Location database initialized with path: {}", dbPath);
    }

    public boolean isInitialized() {
        return initialized;
    }

    public String getCountry(String ipAddress) {
        if (!initialized) {
            log.error("IP2Location database is not initialized");
            return "Unknown";
        }

        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            DatabaseReader databaseReader     = new DatabaseReader.Builder(new File(dbPath)).build();
            CountryResponse response = databaseReader.country(inetAddress);
            return response.getCountry().getName();
        } catch (IOException | GeoIp2Exception e) {
            log.error("Failed to get country for IP address: {}", ipAddress, e);
            return "Unknown";
        }
    }

    public void Open(String ip2LocationDbPath, boolean b) {
        this.dbPath = ip2LocationDbPath;
    }
}
