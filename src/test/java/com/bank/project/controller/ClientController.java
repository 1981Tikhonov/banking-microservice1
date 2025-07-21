package com.bank.project.controller;

import com.bank.project.dto.ClientResponse;
import com.bank.project.dto.CreateClientRequest;
import com.bank.project.entity.Client;
import com.bank.project.exception.ResourceNotFoundException;
import com.bank.project.service.ClientService;
import com.bank.project.service.mapper.ClientMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.bank.project.service.ClientServiceImpl.MAX_PAGE_SIZE;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client", description = "Endpoints for managing clients")
@RequiredArgsConstructor
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;


    @Operation(summary = "Create a new client", description = "Creates a new client and returns the created client object.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Client created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request) {
        logger.info("Received request to create client: {}", request);
        Client createdClient = clientService.createClient(request);
        logger.info("Client created successfully with ID: {}", createdClient.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ClientResponse.fromEntity(createdClient));
    }

    @Operation(summary = "Get client by ID", description = "Fetches a client by their unique ID.")
    @GetMapping("/{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client found"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ClientResponse> getClientById(
            @Parameter(description = "Client ID to fetch", required = true)
            @PathVariable Long id) {
        logger.info("Fetching client with ID: {}", id);
        Client client = clientService.getClientById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        logger.info("Client found with ID: {}", id);
        return ResponseEntity.ok(ClientResponse.fromEntity(client));
    }

    @Operation(summary = "Get all clients", description = "Fetches all clients with pagination and sorting.")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of clients")
    public ResponseEntity<Page<ClientResponse>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        
        logger.info("Fetching all clients with pagination - page: {}, size: {}, sort: {}", page, size, sort);
        
        Pageable pageable = createPageRequest(page, size, sort);
        List<Client> clients = clientService.getAllClients(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            sort
        );
        
        // Convert to Page<ClientResponse>
        List<ClientResponse> content = clients.stream()
                .map(ClientResponse::fromEntity)
                .collect(Collectors.toList());
                
        Page<ClientResponse> response = new PageImpl<>(
            content,
            pageable,
            content.size() // This is a simplification; in a real app, you'd want the total count
        );
        
        logger.info("Successfully fetched {} clients", content.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get clients by status", description = "Fetches clients based on their status (e.g., ACTIVE, INACTIVE).")
    @GetMapping("/status/{status}")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved clients by status")
    public ResponseEntity<Page<ClientResponse>> getClientsByStatus(
            @Parameter(description = "Client status to filter by (ACTIVE, INACTIVE)", required = true)
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        
        logger.info("Fetching clients with status: {}, page: {}, size: {}, sort: {}", status, page, size, sort);
        
        Pageable pageable = createPageRequest(page, size, sort);
        List<Client> clients = clientService.getClientsByStatus(
            status.toUpperCase()
        );
        
        // Convert to Page<ClientResponse>
        List<ClientResponse> content = clients.stream()
                .map(ClientResponse::fromEntity)
                .collect(Collectors.toList());
                
        Page<ClientResponse> response = new PageImpl<>(
            content,
            pageable,
            content.size() // This is a simplification; in a real app, you'd want the total count
        );
        
        logger.info("Found {} clients with status: {}", content.size(), status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update client", description = "Updates an existing client's information.")
    @PutMapping("/{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Client updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ClientResponse> updateClient(
            @Parameter(description = "Client ID to update") @PathVariable Long id,
            @com.bank.project.controller.Valid @RequestBody CreateClientRequest request) {
        logger.info("Updating client with ID: {}", id);
        
        try {
            Client updatedClient = clientService.updateClient(id, request);
            logger.info("Client with ID: {} updated successfully", id);
            return ResponseEntity.ok(ClientResponse.fromEntity(updatedClient));
        } catch (ResourceNotFoundException e) {
            logger.warn("Client with ID: {} not found for update", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error updating client with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(summary = "Delete client", description = "Marks a client as INACTIVE by their unique ID.")
    @DeleteMapping("/{id}")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Client marked as INACTIVE"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "Client ID to mark as INACTIVE") @PathVariable Long id) {
        logger.info("Deleting (marking as INACTIVE) client with ID: {}", id);
        
        try {
            clientService.deleteClient(id);
            logger.info("Client with ID: {} marked as INACTIVE", id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            logger.warn("Client with ID: {} not found for deletion", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting client with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Helper method to validate and adjust pagination parameters
     */
    private Pageable createPageRequest(int page, int size, String[] sort) {
        int validatedPage = Math.max(0, page);
        int validatedSize = Math.min(Math.max(1, size), MAX_PAGE_SIZE);
        
        if (sort == null || sort.length == 0) {
            return PageRequest.of(validatedPage, validatedSize, Sort.by(Sort.Direction.ASC, "id"));
        }
        
        try {
            List<Sort.Order> orders = Arrays.stream(sort)
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .map(s -> s.split(","))
                    .filter(parts -> parts.length == 2)
                    .map(parts -> new Sort.Order(
                            parts[1].trim().equalsIgnoreCase("desc") ? 
                                Sort.Direction.DESC : Sort.Direction.ASC,
                            parts[0].trim()
                    ))
                    .collect(Collectors.toList());
                    
            if (orders.isEmpty()) {
                return PageRequest.of(validatedPage, validatedSize, Sort.by(Sort.Direction.ASC, "id"));
            }
            
            return PageRequest.of(validatedPage, validatedSize, Sort.by(orders));
        } catch (Exception e) {
            logger.warn("Invalid sort parameters: {}. Using default sorting by id", Arrays.toString(sort), e);
            return PageRequest.of(validatedPage, validatedSize, Sort.by(Sort.Direction.ASC, "id"));
        }
    }
}
