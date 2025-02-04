package com.bank.project.service;

import com.bank.project.entity.Client;
import com.bank.project.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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

        Client foundClient = clientService.getClientById(1L);

        assertNotNull(foundClient);
        assertEquals(client.getId(), foundClient.getId());
        assertEquals(client.getFirstName(), foundClient.getFirstName());
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
        Client updatedClientData = new Client();
        updatedClientData.setFirstName("Jane");
        updatedClientData.setLastName("Doe");
        updatedClientData.setEmail("jane.doe@example.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClientData);

        Client updatedClient = clientService.updateClient(1L, updatedClientData);

        assertEquals("Jane", updatedClient.getFirstName());
        assertEquals("Doe", updatedClient.getLastName());
        verify(clientRepository, times(1)).save(client);
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
