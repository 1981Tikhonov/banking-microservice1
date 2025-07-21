package com.bank.project.controller;

import com.bank.project.dto.ClientResponse;
import com.bank.project.dto.CreateClientRequest;
import com.bank.project.entity.Client;
import com.bank.project.exception.GlobalExceptionHandler;
import com.bank.project.exception.ResourceNotFoundException;
import com.bank.project.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private Client testClient;
    private CreateClientRequest createRequest;
    private ClientResponse clientResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        testClient = new Client();
        testClient.setId(1L);
        testClient.setFirstName("John");
        testClient.setLastName("Doe");
        testClient.setEmail("john.doe@example.com");
        testClient.setStatus("ACTIVE");
        testClient.setCreatedAt(LocalDateTime.now());
        testClient.setUpdatedAt(LocalDateTime.now());

        createRequest = new CreateClientRequest();
        createRequest.setFirstName("John");
        createRequest.setLastName("Doe");
        createRequest.setEmail("john.doe@example.com");
        createRequest.setPhone("+1234567890");
        createRequest.setTaxCode("1234567890");
        createRequest.setAddress("123 Main St");

        clientResponse = ClientResponse.fromEntity(testClient);
    }

    @Test
    void createClient_ValidRequest_ShouldReturnCreatedClient() throws Exception {
        when(clientService.createClient(any(CreateClientRequest.class))).thenReturn(testClient);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));

        verify(clientService).createClient(any(CreateClientRequest.class));
    }

    @Test
    void getClientById_WhenClientExists_ShouldReturnClient() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(Optional.of(testClient));

        mockMvc.perform(get("/api/clients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(clientService).getClientById(1L);
    }

    @Test
    void getClientById_WhenClientNotExists_ShouldReturnNotFound() throws Exception {
        when(clientService.getClientById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Client")));

        verify(clientService).getClientById(999L);
    }

    @Test
    void getAllClients_ShouldReturnPaginatedResults() throws Exception {
        when(clientService.getAllClients(0, 10, new String[]{"id,asc"}))
                .thenReturn(List.of(testClient));

        mockMvc.perform(get("/api/clients")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].firstName", is("John")));

        verify(clientService).getAllClients(0, 10, new String[]{"id,asc"});
    }

    @Test
    void getClientsByStatus_ShouldReturnFilteredResults() throws Exception {
        when(clientService.getClientsByStatus("ACTIVE"))
                .thenReturn(List.of(testClient));

        mockMvc.perform(get("/api/clients/status/{status}", "ACTIVE")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].status", is("ACTIVE")));

        verify(clientService).getClientsByStatus("ACTIVE");
    }

    @Test
    void updateClient_WhenClientExists_ShouldReturnUpdatedClient() throws Exception {
        when(clientService.updateClient(eq(1L), any())).thenReturn(testClient);

        mockMvc.perform(put("/api/clients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")));

        verify(clientService).updateClient(eq(1L), any());
    }

    @Test
    void updateClient_WhenClientNotExists_ShouldReturnNotFound() throws Exception {
        when(clientService.updateClient(eq(999L), any()))
                .thenThrow(new ResourceNotFoundException("Client", "id", 999L));

        mockMvc.perform(put("/api/clients/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Client")));

        verify(clientService).updateClient(eq(999L), any());
    }

    @Test
    void deleteClient_WhenClientExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/clients/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(clientService).deleteClient(1L);
    }

    @Test
    void deleteClient_WhenClientNotExists_ShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Client", "id", 999L))
                .when(clientService).deleteClient(999L);

        mockMvc.perform(delete("/api/clients/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Client")));

        verify(clientService).deleteClient(999L);
    }
}
