package com.bank.project.service;

import com.bank.project.dto.CreateClientRequest;
import com.bank.project.entity.Client;
import com.bank.project.exception.ResourceNotFoundException;
import com.bank.project.repository.ClientRepository;
import com.bank.project.service.mapper.ClientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client testClient;
    private CreateClientRequest createRequest;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setFirstName("John");
        testClient.setLastName("Doe");
        testClient.setEmail("john.doe@example.com");
        testClient.setStatus("ACTIVE");
        testClient.setCreatedAt(LocalDateTime.now());
        testClient.setUpdatedAt(LocalDateTime.now());

        createRequest = new CreateClientRequest();
        try {
            // Use reflection to set fields since setters aren't being generated
            java.lang.reflect.Field[] fields = CreateClientRequest.class.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                switch (field.getName()) {
                    case "firstName": field.set(createRequest, "John"); break;
                    case "lastName": field.set(createRequest, "Doe"); break;
                    case "email": field.set(createRequest, "john.doe@example.com"); break;
                    case "phone": field.set(createRequest, "+71234567890"); break;
                    case "birthDate": field.set(createRequest, java.time.LocalDate.of(1990, 1, 1)); break;
                    case "passportSeries": field.set(createRequest, "1234"); break;
                    case "passportNumber": field.set(createRequest, "123456"); break;
                    case "departmentCode": field.set(createRequest, "123-456"); break;
                    case "issuedBy": field.set(createRequest, "Police Department"); break;
                    case "issueDate": field.set(createRequest, java.time.LocalDate.of(2015, 1, 1)); break;
                    case "registrationAddress": field.set(createRequest, "123 Main St, City, Country"); break;
                    case "taxCode": field.set(createRequest, "123456789012"); break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up test request", e);
        }
    }

    @Test
    void createClient_ShouldReturnCreatedClient() {
        when(clientMapper.toEntity(any(CreateClientRequest.class))).thenReturn(testClient);
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        Client created = clientService.createClient(createRequest);

        assertNotNull(created);
        assertEquals("John", created.getFirstName());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void getClientById_WhenClientExists_ShouldReturnClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));

        Optional<Client> found = clientService.getClientById(1L);

        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
    }

    @Test
    void getClientById_WhenClientNotExists_ShouldReturnEmpty() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Client> found = clientService.getClientById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void updateClient_WhenClientExists_ShouldReturnUpdatedClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        // Обновляем имя клиента
        createRequest.setFirstName("John Updated");
        
        Client updated = clientService.updateClient(1L, createRequest);

        assertNotNull(updated);
        assertEquals("John Updated", updated.getFirstName());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void updateClient_WhenClientNotExists_ShouldThrowException() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());
        CreateClientRequest updateRequest = new CreateClientRequest();
        
        assertThrows(ResourceNotFoundException.class, () -> 
            clientService.updateClient(999L, updateRequest)
        );
    }

    @Test
    void deleteClient_WhenClientExists_ShouldMarkAsInactive() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        clientService.deleteClient(1L);

        assertEquals("INACTIVE", testClient.getStatus());
        verify(clientRepository, times(1)).save(testClient);
    }

    @Test
    void getAllClients_ShouldReturnPageOfClients() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(clientRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(testClient), pageable, 1));

        // when
        List<Client> clients = clientService.getAllClients(0, 10, new String[]{"id,asc"});

        // then
        assertFalse(clients.isEmpty());
        assertEquals(1, clients.size());
        assertEquals("John", clients.get(0).getFirstName());
    }

    @Test
    void getClientsByStatus_ShouldReturnFilteredClients() {
        // given
        when(clientRepository.findByStatus(eq("ACTIVE")))
            .thenReturn(new PageImpl<>(List.of(testClient)));

        // when
        List<Client> clients = clientService.getClientsByStatus("ACTIVE");

        // then
        assertFalse(clients.isEmpty());
        assertEquals("ACTIVE", clients.get(0).getStatus());
    }
}
