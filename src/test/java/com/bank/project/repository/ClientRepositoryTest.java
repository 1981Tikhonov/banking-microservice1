package com.bank.project.repository;

import com.bank.project.config.TestConfig;
import com.bank.project.entity.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Testcontainers
@Import(TestConfig.class)
class ClientRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private ClientRepository clientRepository;

    private Client activeClient;
    private Client inactiveClient;

    @BeforeEach
    void setUp() {
        // Create test data
        // Create test data
        activeClient = Client.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .taxCode("1234567890")
                .address("123 Main St, City")
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        inactiveClient = Client.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("+1987654321")
                .taxCode("0987654321")
                .address("456 Oak Ave, Town")
                .status("INACTIVE")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        clientRepository.deleteAll();
        clientRepository.save(activeClient);
        Client savedInactiveClient = clientRepository.save(inactiveClient);
    }

    @Test
    void findById_WhenClientExists_ShouldReturnClient() {
        // When
        Optional<Client> found = clientRepository.findById(activeClient.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(activeClient.getEmail(), found.get().getEmail());
    }

    @Test
    void findById_WhenClientNotExists_ShouldReturnEmpty() {
        // When
        Optional<Client> found = clientRepository.findById(999L);


        // Then
        assertFalse(found.isPresent());
    }


    @Test
    void findByStatus_ShouldReturnMatchingClients() {
        // When
        List<Client> activeClients = (List<Client>) clientRepository.findByStatus("ACTIVE");
        List<Client> inactiveClients = (List<Client>) clientRepository.findByStatus("INACTIVE");

        // Then
        assertEquals(1, activeClients.size());
        assertEquals("ACTIVE", activeClients.get(0).getStatus());
        assertEquals(1, inactiveClients.size());
        assertEquals("INACTIVE", inactiveClients.get(0).getStatus());
    }

    @Test
    void findByEmail_ShouldReturnMatchingClient() {
        // When
        Optional<Client> found = clientRepository.findByEmail("john.doe@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("john.doe@example.com", found.get().getEmail());
    }

    @Test
    void existsByEmail_ShouldReturnTrueForExistingEmail() {
        // When
        boolean exists = clientRepository.existsByEmail("john.doe@example.com");
        boolean notExists = clientRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void findByTaxCode_ShouldReturnMatchingClient() {
        // When
        List<Client> found = clientRepository.findByTaxCode("1234567890");

        // Then
        assertFalse(found.isEmpty());
        assertEquals("1234567890", found.get(0).getTaxCode());
    }

    @Test
    void existsByTaxCode_ShouldReturnTrueForExistingTaxCode() {
        // When
        boolean exists = clientRepository.existsByTaxCode("1234567890");
        boolean notExists = clientRepository.existsByTaxCode("9999999999");

        // Then
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void findByAddressContainingIgnoreCase_ShouldReturnMatchingClients() {
        // When
        List<Client> clients = (List<Client>) clientRepository.findByAddressContainingIgnoreCase("main");

        // Then
        assertEquals(1, clients.size());
        assertTrue(clients.get(0).getAddress().toLowerCase().contains("main"));
    }

    @Test
    void findAllWithPagination_ShouldReturnPageOfClients() {
        // Given
        Pageable pageable = PageRequest.of(0, 1, Sort.by("lastName").ascending());

        // When
        Page<Client> page = clientRepository.findAll(pageable);

        // Then
        assertEquals(2, page.getTotalElements());
        assertEquals(1, page.getNumberOfElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void updateClientStatus_ShouldUpdateStatus() {
        // When
        int updated = clientRepository.updateClientStatus(activeClient.getId(), "SUSPENDED");
        Optional<Client> updatedClient = clientRepository.findById(activeClient.getId());

        // Then
        assertEquals(1, updated);
        assertTrue(updatedClient.isPresent());
        assertEquals("SUSPENDED", updatedClient.get().getStatus());
    }

    @Test
    void findByCreatedAtBetween_ShouldReturnClientsInDateRange() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        // When
        List<Client> clients = clientRepository.findByCreatedAtBetween(start, end);


        // Then
        assertEquals(2, clients.size());
    }
}
