package com.bank.project.service;

import com.bank.project.entity.Client;
import com.bank.project.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Retrieves a client by ID
    public Client getClientById(Long id) {
        logger.info("Retrieving client by ID: {}", id);
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow(() -> {
            logger.error("Client not found with ID: {}", id);
            return new RuntimeException("Client not found with ID: " + id);
        });
    }

    // Retrieves all clients
    public List<Client> getAllClients() {
        logger.info("Retrieving all clients");
        List<Client> clients = clientRepository.findAll();
        logger.info("Found {} clients", clients.size());
        return clients;
    }

    // Creates a new client
    public Client createClient(Client client) {
        logger.info("Creating a new client: {}", client);
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        Client savedClient = clientRepository.save(client);
        logger.info("Client successfully created: {}", savedClient);
        return savedClient;
    }

    // Updates an existing client
    public Client updateClient(Long id, Client clientDetails) {
        logger.info("Updating client with ID: {} using details: {}", id, clientDetails);
        Client existingClient = getClientById(id);
        existingClient.setFirstName(clientDetails.getFirstName());
        existingClient.setLastName(clientDetails.getLastName());
        existingClient.setEmail(clientDetails.getEmail());
        existingClient.setPhone(clientDetails.getPhone());
        existingClient.setStatus(clientDetails.getStatus());
        existingClient.setTaxCode(clientDetails.getTaxCode());
        existingClient.setManagerId(clientDetails.getManagerId());
        existingClient.setAddress(clientDetails.getAddress());  // Добавляем обработку адреса
        existingClient.setUpdatedAt(LocalDateTime.now());
        Client updatedClient = clientRepository.save(existingClient);
        logger.info("Client successfully updated: {}", updatedClient);
        return updatedClient;
    }

    // Deletes a client by ID
    public boolean deleteClient(Long id) {
        logger.info("Deleting client with ID: {}", id);
        Client client = getClientById(id);
        clientRepository.delete(client);
        logger.info("Client with ID: {} successfully deleted", id);
        return false;
    }

    // Retrieves clients by status
    public List<Client> getClientsByStatus(String status) {
        logger.info("Retrieving clients by status: {}", status);
        List<Client> clients = clientRepository.findByStatus(status);
        logger.info("Found {} clients with status: {}", clients.size(), status);
        return clients;
    }

    // Retrieves clients by manager ID
    public List<Client> getClientsByManagerId(Long managerId) {
        logger.info("Retrieving clients by manager ID: {}", managerId);
        return clientRepository.findByManagerId(managerId);
    }

    // Retrieves clients by tax code
    public List<Client> getClientsByTaxCode(String taxCode) {
        logger.info("Retrieving clients by tax code: {}", taxCode);
        return clientRepository.findByTaxCode(taxCode);
    }

    // Retrieves clients by first name
    public List<Client> getClientsByFirstName(String firstName) {
        logger.info("Retrieving clients by first name: {}", firstName);
        return clientRepository.findByFirstName(firstName);
    }

    // Retrieves clients by last name
    public List<Client> getClientsByLastName(String lastName) {
        logger.info("Retrieving clients by last name: {}", lastName);
        return clientRepository.findByLastName(lastName);
    }

    // Retrieves clients by email
    public List<Client> getClientsByEmail(String email) {
        logger.info("Retrieving clients by email: {}", email);
        return clientRepository.findByEmail(email);
    }

    // Retrieves clients by phone
    public List<Client> getClientsByPhone(String phone) {
        logger.info("Retrieving clients by phone: {}", phone);
        return clientRepository.findByPhone(phone);
    }

    // Retrieves clients by address
    public List<Client> getClientsByAddress(String address) {
        logger.info("Retrieving clients by address: {}", address);
        return clientRepository.findByAddress(address);
    }

    // Retrieves clients created within a date range
    public List<Client> getClientsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Retrieving clients created between {} and {}", startDate, endDate);
        return clientRepository.findByCreatedAtBetween(startDate, endDate);
    }

    // Retrieves clients updated after a specific date
    public List<Client> getClientsByUpdatedAtAfter(LocalDateTime updatedAt) {
        logger.info("Retrieving clients updated after: {}", updatedAt);
        return clientRepository.findByUpdatedAtAfter(updatedAt);
    }
}
