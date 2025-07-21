package com.bank.project.service;

import com.bank.project.dto.CreateClientRequest;
import com.bank.project.entity.Client;
import com.bank.project.entity.enums.ClientStatus;
import com.bank.project.exception.ResourceNotFoundException;
import com.bank.project.repository.ClientRepository;
import com.bank.project.service.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Math.max;


/**
 * Implementation of the {@link ClientService} interface.
 * Provides methods for managing clients in the system.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {
    
    private static final String DEFAULT_SORT_FIELD = "id";
    private static final String SORT_DELIMITER = ",";
    public static final int MAX_PAGE_SIZE = 100;
    
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public Client createClient(Client request) {
        return null;
    }

    @Transactional
    @Override
    public Client createClient(CreateClientRequest request) {
        if (log.isInfoEnabled()) {
            log.info("Creating new client with email: {}", request.getEmail());
        }

        // Check if client with email already exists
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Client with email " + request.getEmail() + " already exists");
        }
        
        try {
            // Map DTO to entity and save
            Client client = clientMapper.toEntity(request);
            client.setCreatedAt(LocalDateTime.now());
            client.setUpdatedAt(LocalDateTime.now());
            client.setStatus("ACTIVE");
            
            Client savedClient = clientRepository.save(client);
            log.info("Created new client with ID: {}", savedClient.getId());
            return savedClient;
        } catch (Exception e) {
            log.error("Error creating client: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create client: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> getClientById(Long id) {
        log.debug("Fetching client with ID: {}", id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid client ID: " + id);
        }
        return clientRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Client> getAllClients(int page, int size, String[] sort) {
        log.debug("Fetching all clients with pagination - page: {}, size: {}, sort: {}", page, size, sort);
        
        // Validate and adjust pagination parameters
        // @formatter:off
        // This nested Math.min/max is intentional to ensure:
        // 1. Minimum page size is 1 (Math.max(1, size))
        // 2. Maximum page size is MAX_PAGE_SIZE (Math.min(...))
        // @formatter:on
        int validatedPage = max(0, page);
        int validatedSize = Math.min(size, MAX_PAGE_SIZE);
        
        // Parse sort parameters
        Sort sorting = parseSortParameters(sort);
        
        Pageable pageable = PageRequest.of(validatedPage, validatedSize, sorting);
        Page<Client> clientPage = clientRepository.findAll(pageable);
        
        return clientPage.getContent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Client> getClientsByStatus(String status) {
        log.debug("Fetching clients with status: {}", status);
        
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        
        try {
            ClientStatus clientStatus = ClientStatus.valueOf(status.toUpperCase());
            
            // Validate and adjust pagination parameters
            int size    = 10;
            int validatedPage = 0;

            Pageable pageable = PageRequest.of(validatedPage, size);
            Page<Client> clientPage = clientRepository.findByStatus(clientStatus, pageable);
            
            return clientPage.getContent();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid client status: " + status + ". Valid statuses are: " + 
                Arrays.stream(ClientStatus.values())
                    .map(ClientStatus::name)
                    .collect(Collectors.joining(", ")));
        }
    }
    
    @Override
    @Transactional
    public Client updateClient(Long id, CreateClientRequest request) {
        log.info("Updating client with ID: {}", id);
        
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid client ID: " + id);
        }
        
        Client existingClient = findClientOrThrow(id);
        
        // Check if email is being changed and if the new email already exists
        if (!existingClient.getEmail().equalsIgnoreCase(request.getEmail()) && 
            clientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email " + request.getEmail() + " is already in use");
        }
        
        try {
            // Update existing client with new data from request
            clientMapper.updateFromDto(request, existingClient);
            existingClient.setUpdatedAt(LocalDateTime.now());
            
            Client updatedClient = clientRepository.save(existingClient);
            log.info("Updated client with ID: {}", id);
            return updatedClient;
        } catch (Exception e) {
            log.error("Error updating client with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update client: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional
    public boolean deleteClient(Long id) {
        log.info("Deleting client with ID: {}", id);
        
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid client ID: " + id);
        }
        
        Client client = findClientOrThrow(id);
        
        try {
            // Instead of hard delete, we'll mark as INACTIVE
            client.setStatus("INACTIVE");
            client.setUpdatedAt(LocalDateTime.now());
            clientRepository.save(client);
            
            log.info("Marked client with ID: {} as INACTIVE", id);
        } catch (Exception e) {
            log.error("Error deleting client with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete client: " + e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * Helper method to parse sort parameters into a Sort object
     */
    private Sort parseSortParameters(String[] sort) {
        if (sort == null || sort.length == 0) {
            return Sort.by(Sort.Order.asc(DEFAULT_SORT_FIELD));
        }
        
        try {
            List<Sort.Order> orders = Arrays.stream(sort)
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .map(s -> s.split(SORT_DELIMITER))
                    .filter(parts -> parts.length == 2)
                    .map(parts -> new Sort.Order(
                            parts[1].trim().equalsIgnoreCase("desc") ? 
                                Sort.Direction.DESC : Sort.Direction.ASC,
                            parts[0].trim()
                    ))
                    .collect(Collectors.toList());
            
            if (orders.isEmpty()) {
                return Sort.by(Sort.Order.asc(DEFAULT_SORT_FIELD));
            }
            
            return Sort.by(orders);
        } catch (Exception e) {
            log.warn("Invalid sort parameters: {}. Using default sorting by {}", 
                    Arrays.toString(sort), DEFAULT_SORT_FIELD, e);
            return Sort.by(Sort.Order.asc(DEFAULT_SORT_FIELD));
        }
    }
    
    /**
     * Helper method to find a client by ID or throw an exception if not found
     */
    private Client findClientOrThrow(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
    }
}
