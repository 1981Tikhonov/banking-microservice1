package com.bank.project.controller;

import com.bank.project.entity.Client;
import com.bank.project.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client", description = "Endpoints for managing clients")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Create a new client", description = "Creates a new client and returns the created client object.")
    @PostMapping
    public ResponseEntity<Client> createClient(@Parameter(description = "Client details to be created") @RequestBody Client client) {
        logger.info("Creating client with details: {}", client);
        Client createdClient = clientService.createClient(client);
        logger.info("Client created successfully with ID: {}", createdClient.getId());
        return ResponseEntity.ok(createdClient);
    }

    @Operation(summary = "Get client by ID", description = "Fetches a client by their unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@Parameter(description = "Client ID to fetch") @PathVariable Long id) {
        logger.info("Fetching client with ID: {}", id);
        Client client = clientService.getClientById(id);
        if (client != null) {
            logger.info("Client found with ID: {}", id);
            return ResponseEntity.ok(client);
        } else {
            logger.warn("Client with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all clients", description = "Fetches all clients.")
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        logger.info("Fetching all clients");
        List<Client> clients = clientService.getAllClients();
        logger.info("Total number of clients fetched: {}", clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients by status", description = "Fetches clients based on their status (e.g., active, inactive).")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Client>> getClientsByStatus(@Parameter(description = "Client status to filter by") @PathVariable String status) {
        logger.info("Fetching clients with status: {}", status);
        List<Client> clients = clientService.getClientsByStatus(status);
        logger.info("Total clients with status {}: {}", status, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients by manager ID", description = "Fetches clients based on the manager ID associated with them.")
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Client>> getClientsByManagerId(@Parameter(description = "Manager ID to filter clients by") @PathVariable Long managerId) {
        logger.info("Fetching clients managed by manager ID: {}", managerId);
        List<Client> clients = clientService.getClientsByManagerId(managerId);
        logger.info("Total clients managed by manager ID {}: {}", managerId, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients by tax code", description = "Fetches clients based on their tax code.")
    @GetMapping("/tax-code/{taxCode}")
    public ResponseEntity<List<Client>> getClientsByTaxCode(@Parameter(description = "Tax code to filter clients by") @PathVariable String taxCode) {
        logger.info("Fetching clients with tax code: {}", taxCode);
        List<Client> clients = clientService.getClientsByTaxCode(taxCode);
        logger.info("Total clients with tax code {}: {}", taxCode, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients by first name", description = "Fetches clients based on their first name.")
    @GetMapping("/first-name/{firstName}")
    public ResponseEntity<List<Client>> getClientsByFirstName(@Parameter(description = "First name to filter clients by") @PathVariable String firstName) {
        logger.info("Fetching clients with first name: {}", firstName);
        List<Client> clients = clientService.getClientsByFirstName(firstName);
        logger.info("Total clients with first name {}: {}", firstName, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients by last name", description = "Fetches clients based on their last name.")
    @GetMapping("/last-name/{lastName}")
    public ResponseEntity<List<Client>> getClientsByLastName(@Parameter(description = "Last name to filter clients by") @PathVariable String lastName) {
        logger.info("Fetching clients with last name: {}", lastName);
        List<Client> clients = clientService.getClientsByLastName(lastName);
        logger.info("Total clients with last name {}: {}", lastName, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients by email", description = "Fetches clients based on their email address.")
    @GetMapping("/email/{email}")
    public ResponseEntity<List<Client>> getClientsByEmail(@Parameter(description = "Email address to filter clients by") @PathVariable String email) {
        logger.info("Fetching clients with email: {}", email);
        List<Client> clients = clientService.getClientsByEmail(email);
        logger.info("Total clients with email {}: {}", email, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients by phone", description = "Fetches clients based on their phone number.")
    @GetMapping("/phone/{phone}")
    public ResponseEntity<List<Client>> getClientsByPhone(@Parameter(description = "Phone number to filter clients by") @PathVariable String phone) {
        logger.info("Fetching clients with phone number: {}", phone);
        List<Client> clients = clientService.getClientsByPhone(phone);
        logger.info("Total clients with phone number {}: {}", phone, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients by address", description = "Fetches clients based on their address.")
    @GetMapping("/address/{address}")
    public ResponseEntity<List<Client>> getClientsByAddress(@Parameter(description = "Address to filter clients by") @PathVariable String address) {
        logger.info("Fetching clients with address: {}", address);
        List<Client> clients = clientService.getClientsByAddress(address);
        logger.info("Total clients with address {}: {}", address, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients created within a date range", description = "Fetches clients created within a specific date range.")
    @GetMapping("/created-between")
    public ResponseEntity<List<Client>> getClientsByCreatedAtBetween(
            @Parameter(description = "Start date of the range") @RequestParam LocalDateTime startDate,
            @Parameter(description = "End date of the range") @RequestParam LocalDateTime endDate) {
        logger.info("Fetching clients created between {} and {}", startDate, endDate);
        List<Client> clients = clientService.getClientsByCreatedAtBetween(startDate, endDate);
        logger.info("Total clients created between {} and {}: {}", startDate, endDate, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get clients updated after a specific date", description = "Fetches clients who were updated after a specific date.")
    @GetMapping("/updated-after")
    public ResponseEntity<List<Client>> getClientsByUpdatedAtAfter(@Parameter(description = "Date after which clients were updated") @RequestParam LocalDateTime updatedAt) {
        logger.info("Fetching clients updated after: {}", updatedAt);
        List<Client> clients = clientService.getClientsByUpdatedAtAfter(updatedAt);
        logger.info("Total clients updated after {}: {}", updatedAt, clients.size());
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Update client", description = "Updates an existing client's information.")
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(
            @Parameter(description = "Client ID to update") @PathVariable Long id,
            @Parameter(description = "Updated client details") @RequestBody Client clientDetails) {
        logger.info("Updating client with ID: {}", id);
        Client updatedClient = clientService.updateClient(id, clientDetails);
        if (updatedClient != null) {
            logger.info("Client with ID: {} updated successfully", id);
            return ResponseEntity.ok(updatedClient);
        } else {
            logger.warn("Client with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete client", description = "Deletes a client by their unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@Parameter(description = "Client ID to delete") @PathVariable Long id) {
        logger.info("Deleting client with ID: {}", id);
        boolean isDeleted = clientService.deleteClient(id);
        if (isDeleted) {
            logger.info("Client with ID: {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Client with ID: {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}
