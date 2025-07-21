package com.bank.project.controller.integration;

import com.bank.project.dto.CreateClientRequest;
import com.bank.project.entity.Client;
import com.bank.project.repository.ClientRepository;
import com.bank.project.service.mapper.ClientMapper;
import com.bank.project.entity.enums.ClientStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.bank.project.dto.CreateClientRequest.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ClientControllerIntegrationTest {

    private static final String API_CLIENTS = "/api/clients";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String PHONE = "+71234567890";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1990, 1, 1);
    private static final String PASSPORT_SERIES = "1234";
    private static final String PASSPORT_NUMBER = "123456";
    private static final String DEPARTMENT_CODE = "123-456";
    private static final String ISSUED_BY = "Police Department";
    private static final LocalDate ISSUE_DATE = LocalDate.of(2015, 1, 1);
    private static final String REGISTRATION_ADDRESS = "123 Main St, City, Country";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    private Client testClient;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
        testClient = createTestClient();
    }

    @AfterEach
    void tearDown() {
        // Not needed as @Transactional will rollback changes
    }

    @Test
    void createClient_WithValidData_ShouldReturnCreatedClient() throws Exception {
        // Given
        CreateClientRequest request = createValidClientRequest();

        // When & Then
        mockMvc.perform(post(API_CLIENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value(request.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(request.getLastName()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.status").value(ClientStatus.ACTIVE.name()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());

        // Verify the client was saved in the database
        List<Client> clients = clientRepository.findAll();
        assertEquals(2, clients.size()); // 1 from setup + 1 new
    }

    private Client createTestClient() {
        Client client = new Client();
        client.setFirstName(FIRST_NAME);
        client.setLastName(LAST_NAME);
        client.setEmail(VALID_EMAIL);
        client.setPhone(PHONE);
        client.setBirthDate(BIRTH_DATE);
        client.setPassportSeries(PASSPORT_SERIES);
        client.setPassportNumber(PASSPORT_NUMBER);
        client.setDepartmentCode(DEPARTMENT_CODE);
        client.setIssuedBy(ISSUED_BY);
        client.setIssueDate(ISSUE_DATE);
        client.setRegistrationAddress(REGISTRATION_ADDRESS);
        client.setStatus(ClientStatus.ACTIVE.name());
        return clientRepository.save(client);
    }

    private CreateClientRequest createValidClientRequest() {
        // Create a new instance using reflection to avoid builder
        CreateClientRequest request = new CreateClientRequest();
        try {
            // Use reflection to set fields since setters aren't being generated
            java.lang.reflect.Field[] fields = CreateClientRequest.class.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                switch (field.getName()) {
                    case "firstName": field.set(request, "Jane"); break;
                    case "lastName": field.set(request, "Smith"); break;
                    case "middleName": field.set(request, "Marie"); break;
                    case "email": field.set(request, "jane.smith@example.com"); break;
                    case "phone": field.set(request, "+7987654321"); break;
                    case "birthDate": field.set(request, LocalDate.of(1990, 1, 1)); break;
                    case "passportSeries": field.set(request, "1234"); break;
                    case "passportNumber": field.set(request, "123456"); break;
                    case "departmentCode": field.set(request, "123-456"); break;
                    case "issuedBy": field.set(request, "Police Department"); break;
                    case "issueDate": field.set(request, LocalDate.of(2015, 1, 1)); break;
                    case "registrationAddress": field.set(request, "123 Main St, City, Country"); break;
                    case "taxCode": field.set(request, "123456789012"); break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test client request", e);
        }
        return request;
    }

    @Test
    void getClientById_WithExistingId_ShouldReturnClient() throws Exception {
        // When & Then
        mockMvc.perform(get(API_CLIENTS + "/{id}", testClient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testClient.getId()))
                .andExpect(jsonPath("$.firstName").value(testClient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testClient.getLastName()))
                .andExpect(jsonPath("$.email").value(testClient.getEmail()))
                .andExpect(jsonPath("$.status").value(testClient.getStatus()));
    }

    @Test
    void updateClient_WithValidData_ShouldUpdateClient() throws Exception {
        // Given
        String updatedFirstName = "John Updated";
        String updatedLastName = "Doe Updated";
        String updatedEmail = "john.updated@example.com";
        
        // Create update request using reflection
        CreateClientRequest updateRequestDto = createValidClientRequest();
        try {
            java.lang.reflect.Field firstNameField = CreateClientRequest.class.getDeclaredField("firstName");
            java.lang.reflect.Field lastNameField = CreateClientRequest.class.getDeclaredField("lastName");
            java.lang.reflect.Field emailField = CreateClientRequest.class.getDeclaredField("email");
            
            firstNameField.setAccessible(true);
            lastNameField.setAccessible(true);
            emailField.setAccessible(true);
            
            firstNameField.set(updateRequestDto, updatedFirstName);
            lastNameField.set(updateRequestDto, updatedLastName);
            emailField.set(updateRequestDto, updatedEmail);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update client request fields", e);
        }
        Client updateRequest = clientMapper.toClient(updateRequestDto);

        // When & Then
        mockMvc.perform(put(API_CLIENTS + "/{id}", testClient.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testClient.getId()))
                .andExpect(jsonPath("$.firstName").value(updatedFirstName))
                .andExpect(jsonPath("$.lastName").value(updatedLastName))
                .andExpect(jsonPath("$.email").value(updatedEmail));

        // Verify the update in the database
        Client updatedClient = clientRepository.findById(testClient.getId()).orElseThrow();
        assertEquals(updatedFirstName, updatedClient.getFirstName());
        assertEquals(updatedLastName, updatedClient.getLastName());
        assertEquals(updatedEmail, updatedClient.getEmail());
    }

    @Test
    void deleteClient_WithExistingId_ShouldReturnNoContent() throws Exception {
        // Given
        Long clientId = testClient.getId();
        assertTrue(clientRepository.existsById(clientId));

        // When & Then
        mockMvc.perform(delete(API_CLIENTS + "/{id}", clientId))
                .andExpect(status().isNoContent());

        // Verify client is deleted
        assertFalse(clientRepository.existsById(clientId));
        mockMvc.perform(get(API_CLIENTS + "/{id}", clientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getClients_WithPagination_ShouldReturnPageOfClients() throws Exception {
        // Given
        int numberOfClients = 5;
        createTestClients(numberOfClients);

        // When & Then
        mockMvc.perform(get(API_CLIENTS)
                .param("page", "0")
                .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3))) // 3 items per page
                .andExpect(jsonPath("$.totalElements").value(numberOfClients + 1)) // 1 from setup + 5 new
                .andExpect(jsonPath("$.totalPages").value(2)) // 6 items / 3 per page = 2 pages
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(false));
    }

    @Test
    void getClientsByStatus_WithValidStatus_ShouldReturnFilteredClients() throws Exception {
        // Given
        String status = ClientStatus.INACTIVE.name();
        int inactiveClientsCount = 3;
        
        // Create inactive clients
        for (int i = 0; i < inactiveClientsCount; i++) {
            Client client = createTestClient();
            client.setStatus(status);
            clientRepository.save(client);
        }

        // When & Then
        mockMvc.perform(get(API_CLIENTS + "/status/{status}", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(inactiveClientsCount)))
                .andExpect(jsonPath("$[0].status").value(status));
    }

    @Test
    void createClient_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Given
        CreateClientRequest invalidRequest = new CreateClientRequest();
        invalidRequest.setEmail(VALID_EMAIL); // Only setting email, other required fields are missing

        // When & Then
        mockMvc.perform(post(API_CLIENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[?(@.field == 'firstName')]").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'lastName')]").exists());
    }

    @Test
    void getClientById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        // Given
        long nonExistentId = 9999L;
        assertFalse(clientRepository.existsById(nonExistentId));

        // When & Then
        mockMvc.perform(get(API_CLIENTS + "/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void updateClient_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        // Given
        long nonExistentId = 9999L;
        assertFalse(clientRepository.existsById(nonExistentId));
        CreateClientRequest updateRequest = createValidClientRequest();

        // When & Then
        mockMvc.perform(put(API_CLIENTS + "/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deleteClient_WithNonExistentId_ShouldReturnNoContent() throws Exception {
        // Given
        long nonExistentId = 9999L;
        assertFalse(clientRepository.existsById(nonExistentId));

        // When & Then
        mockMvc.perform(delete(API_CLIENTS + "/{id}", nonExistentId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getClients_WithSearchFilter_ShouldReturnFilteredResults() throws Exception {
        // Given
        String searchTerm = "special";
        createTestClients(3); // Regular clients
        
        // Create a client with search term in first name
        Client specialClient = createTestClient();
        specialClient.setFirstName("SpecialClient");
        clientRepository.save(specialClient);

        // When & Then
        mockMvc.perform(get(API_CLIENTS)
                .param("search", searchTerm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[?(@.firstName == 'SpecialClient')]").exists());
    }

    @Test
    void createClient_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        // Given
        CreateClientRequest invalidRequest = createValidClientRequest();
        invalidRequest.setEmail(INVALID_EMAIL);

        // When & Then
        mockMvc.perform(post(API_CLIENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[?(@.field == 'email')]").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'email')].message").value("must be a well-formed email address"));
    }
    
    // Helper methods
    private void createTestClients(int count) {
        for (int i = 0; i < count; i++) {
            Client client = createTestClient();
            client.setFirstName("Test" + i);
            client.setEmail("test" + i + "@example.com");
            client.setStatus(ClientStatus.ACTIVE.name());
            clientRepository.save(client);
        }
    }
}
