package com.bank.project.service;

import com.bank.project.entity.Manager;
import com.bank.project.repository.ManagerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(ManagerService.class);

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public ManagerService(ManagerRepository managerRepository, PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);
        Manager manager = managerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        logger.info("User found: {}", manager.getUsername());
        // Build and return the User object for Spring Security
        return User.builder()
                .username(manager.getUsername())
                .password(manager.getPassword())
                .roles("ROLE_" + manager.getRole().toUpperCase()) // "ROLE_" prefix is mandatory
                .build();
    }

    public Manager createManager(Manager manager) {
        logger.info("Creating new manager with username: {}", manager.getUsername());
        if (managerRepository.existsByUsername(manager.getUsername())) {
            logger.error("Username already exists: {}", manager.getUsername());
            throw new IllegalArgumentException("Username already exists!");
        }
        manager.setPassword(passwordEncoder.encode(manager.getPassword())); // Encrypt the password
        manager.setCreatedAt(LocalDateTime.now());
        Manager savedManager = managerRepository.save(manager);
        logger.info("Manager created successfully: {}", savedManager.getUsername());
        return savedManager;
    }

    public List<Manager> getAllManagers() {
        logger.debug("Fetching all managers from the database");
        return managerRepository.findAll();
    }

    public Manager updateManager(Long id, Manager managerDetails) {
        logger.info("Updating manager with ID: {}", id);
        Manager existingManager = getManagerById(id);
        existingManager.setFirstName(managerDetails.getFirstName());
        existingManager.setLastName(managerDetails.getLastName());
        existingManager.setStatus(managerDetails.getStatus());
        existingManager.setDescription(managerDetails.getDescription());
        existingManager.setRole(managerDetails.getRole().toUpperCase());

        if (managerDetails.getPassword() != null && !managerDetails.getPassword().isEmpty()) {
            existingManager.setPassword(passwordEncoder.encode(managerDetails.getPassword()));
            logger.info("Password updated for manager ID: {}", id);
        }

        Manager updatedManager = managerRepository.save(existingManager);
        logger.info("Manager with ID: {} updated successfully", id);
        return updatedManager;
    }

    public boolean deleteManager(Long id) {
        logger.info("Deleting manager with ID: {}", id);
        Manager manager = getManagerById(id);
        managerRepository.delete(manager);
        logger.info("Manager with ID: {} deleted successfully", id);
        return false;
    }

    public Manager getManagerById(Long id) {
        logger.info("Fetching manager with ID: {}", id);
        return managerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with ID: " + id));
    }

    public List<Manager> findManagersByStatus(String status) {
        logger.info("Finding managers with status: {}", status);
        return managerRepository.findByStatus(status);
    }

    public List<Manager> findManagersByRole(String role) {
        logger.info("Finding managers with role: {}", role);
        return managerRepository.findByRole(role);
    }

    public Optional<Manager> findManagerByFirstName(String firstName) {
        logger.info("Searching for manager with first name: {}", firstName);
        return managerRepository.findByFirstName(firstName);
    }

    public Optional<Manager> findManagerByLastName(String lastName) {
        logger.info("Searching for manager with last name: {}", lastName);
        return managerRepository.findByLastName(lastName);
    }

    public boolean isManagerActive(String username) {
        logger.debug("Checking if manager with username {} is active", username);
        return false;
    }
}
