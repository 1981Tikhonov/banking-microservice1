package com.bank.project.controller;

import com.bank.project.entity.Manager;
import com.bank.project.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Operation(summary = "Create a new manager", description = "Creates a new manager and returns the created manager.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Manager created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid manager data")
    })
    @PostMapping
    public ResponseEntity<Manager> createManager(@RequestBody Manager manager) {
        logger.info("Creating new manager: {}", manager);
        Manager createdManager = managerService.createManager(manager);
        logger.info("Manager created successfully with ID: {}", createdManager.getId());
        return ResponseEntity.ok(createdManager);
    }

    @Operation(summary = "Get a manager by ID", description = "Fetches the details of a manager by their unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Manager found"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManagerById(@Parameter(description = "ID of the manager to retrieve") @PathVariable Long id) {
        logger.info("Fetching manager with ID: {}", id);
        Manager manager = managerService.getManagerById(id);
        if (manager != null) {
            logger.info("Manager found: {}", manager);
            return ResponseEntity.ok(manager);
        } else {
            logger.warn("Manager with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all managers", description = "Fetches the details of all managers in the system.")
    @ApiResponse(responseCode = "200", description = "List of managers retrieved successfully")
    @GetMapping
    public ResponseEntity<List<Manager>> getAllManagers() {
        logger.info("Fetching all managers");
        List<Manager> managers = managerService.getAllManagers();
        logger.info("Total managers fetched: {}", managers.size());
        return ResponseEntity.ok(managers);
    }

    @Operation(summary = "Update a manager", description = "Updates the details of an existing manager.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Manager updated successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Manager> updateManager(@PathVariable Long id, @RequestBody Manager manager) {
        logger.info("Updating manager with ID: {}", id);
        Manager updatedManager = managerService.updateManager(id, manager);
        if (updatedManager != null) {
            logger.info("Manager updated successfully: {}", updatedManager);
            return ResponseEntity.ok(updatedManager);
        } else {
            logger.warn("Manager with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a manager", description = "Deletes an existing manager by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Manager deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        logger.info("Attempting to delete manager with ID: {}", id);
        boolean isDeleted = managerService.deleteManager(id);
        if (isDeleted) {
            logger.info("Manager with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Manager with ID: {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Find manager by first name", description = "Fetches a manager by their first name.")
    @ApiResponse(responseCode = "200", description = "Manager found")
    @GetMapping("/first-name/{firstName}")
    public ResponseEntity<Optional<Manager>> findManagerByFirstName(@PathVariable String firstName) {
        logger.info("Searching for manager by first name: {}", firstName);
        Optional<Manager> manager = managerService.findManagerByFirstName(firstName);
        if (manager.isPresent()) {
            logger.info("Manager found: {}", manager.get());
        } else {
            logger.warn("No manager found with first name: {}", firstName);
        }
        return ResponseEntity.ok(manager);
    }

    @Operation(summary = "Find manager by last name", description = "Fetches a manager by their last name.")
    @ApiResponse(responseCode = "200", description = "Manager found")
    @GetMapping("/last-name/{lastName}")
    public ResponseEntity<Optional<Manager>> findManagerByLastName(@PathVariable String lastName) {
        logger.info("Searching for manager by last name: {}", lastName);
        Optional<Manager> manager = managerService.findManagerByLastName(lastName);
        if (manager.isPresent()) {
            logger.info("Manager found: {}", manager.get());
        } else {
            logger.warn("No manager found with last name: {}", lastName);
        }
        return ResponseEntity.ok(manager);
    }

    @Operation(summary = "Find managers by status", description = "Fetches managers by their status (e.g. active, inactive).")
    @ApiResponse(responseCode = "200", description = "Managers found")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Manager>> findManagersByStatus(@PathVariable String status) {
        logger.info("Searching for managers by status: {}", status);
        List<Manager> managers = managerService.findManagersByStatus(status);
        logger.info("Total managers found with status {}: {}", status, managers.size());
        return ResponseEntity.ok(managers);
    }

    @Operation(summary = "Find managers by role", description = "Fetches managers by their role (e.g. admin, manager).")
    @ApiResponse(responseCode = "200", description = "Managers found")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Manager>> findManagersByRole(@PathVariable String role) {
        logger.info("Searching for managers by role: {}", role);
        List<Manager> managers = managerService.findManagersByRole(role);
        logger.info("Total managers found with role {}: {}", role, managers.size());
        return ResponseEntity.ok(managers);
    }
}
