package com.bank.project.service;

import com.bank.project.entity.Client;
import com.bank.project.repository.ClientRepository;
import com.bank.project.dto.CreateClientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creating a mock client for testing
        client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");
        client.setPhone("1234567890");
        client.setStatus("active");
        client.setTaxCode("123456789");
        client.setManagerId(10L);
        client.setAddress("123 Main St");
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGetClientById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.getClientById(1L);

        assertTrue(foundClient.isPresent(), "Client should be present");
        Client actualClient = foundClient.get();
        assertEquals(client.getId(), actualClient.getId());
        assertEquals(client.getFirstName(), actualClient.getFirstName());
    }

    @Test
    void testGetClientById_ClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.getClientById(1L));

        assertEquals("Client not found with ID: 1", exception.getMessage());
    }

    @Test
    void testCreateClient() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client createdClient = clientService.createClient(client);

        assertNotNull(createdClient);
        assertEquals(client.getFirstName(), createdClient.getFirstName());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testUpdateClient() {
        // Create DTO with updated data
        CreateClientRequest updateRequest = new CreateClientRequest();
        try {
            java.lang.reflect.Field firstNameField = CreateClientRequest.class.getDeclaredField("firstName");
            java.lang.reflect.Field lastNameField = CreateClientRequest.class.getDeclaredField("lastName");
            java.lang.reflect.Field emailField = CreateClientRequest.class.getDeclaredField("email");
            
            firstNameField.setAccessible(true);
            lastNameField.setAccessible(true);
            emailField.setAccessible(true);
            
            firstNameField.set(updateRequest, "Jane");
            lastNameField.set(updateRequest, "Doe");
            emailField.set(updateRequest, "jane.doe@example.com");
            
            // Create expected updated client
            Client updatedClientEntity = new Client();
            updatedClientEntity.setId(1L);
            updatedClientEntity.setFirstName("Jane");
            updatedClientEntity.setLastName("Doe");
            updatedClientEntity.setEmail("jane.doe@example.com");

            when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
            when(clientRepository.save(any(Client.class))).thenReturn(updatedClientEntity);

            Client updatedClient = clientService.updateClient(1L, updateRequest);

            assertEquals("Jane", updatedClient.getFirstName());
            assertEquals("Doe", updatedClient.getLastName());
            verify(clientRepository, times(1)).save(any(Client.class));
        } catch (Exception e) {
            fail("Failed to update client: " + e.getMessage());
        }
    }

    @Test
    void testDeleteClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        boolean result = clientService.deleteClient(1L);

        assertTrue(result);
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void testGetClientsByStatus() {
        // Assuming the clientRepository is already mocking the method
        when(clientRepository.findByStatus("active")).thenReturn(List.of(client));

        var clients = clientService.getClientsByStatus("active");

        assertFalse(clients.isEmpty());
        assertEquals("active", clients.get(0).getStatus());
    }
}
