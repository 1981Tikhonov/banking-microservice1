package com.bank.project.service;

import com.bank.project.dto.CreateClientRequest;
import com.bank.project.entity.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing clients.
 * Provides methods for CRUD operations and querying clients with pagination and sorting.
 */
public interface ClientService {
    
    /**
     * Create a new client
     * @param request DTO with client data
     * @return created client entity
     */
    Client createClient(Client request);

    @Transactional
    Client createClient(CreateClientRequest request);

    /**
     * Get client by ID
     * @param id client ID
     * @return client entity wrapped in Optional
     */
    Optional<Client> getClientById(Long id);
    
    /**
     * Update an existing client
     * @param id client ID
     * @param request DTO with updated client data
     * @return updated client entity
     */
    Client updateClient(Long id, CreateClientRequest request);
    
    /**
     * Delete a client (mark as INACTIVE)
     *
     * @param id client ID
     * @return
     */
    boolean deleteClient(Long id);
    
    /**
     * Get all clients with pagination and sorting
     * @param page page number (0-based)
     * @param size page size
     * @param sort sort parameters (e.g., "id,asc")
     * @return list of clients
     */
    List<Client> getAllClients(int page, int size, String[] sort);
    
    /**
     * Get clients by status with pagination
     *
     * @param status client status (ACTIVE, INACTIVE, etc.)
     * @return list of clients with specified status
     */
    List<Client> getClientsByStatus(String status);
}
