package com.bank.project.controller;

import com.bank.project.entity.Client;
import com.bank.project.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    public void testCreateClient() throws Exception {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setId(1L);

        // Мокируем сервис
        when(clientService.createClient(any(Client.class))).thenReturn(client);

        // Выполняем POST-запрос и проверяем ответ
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        // Проверяем, что метод createClient был вызван
        verify(clientService, times(1)).createClient(any(Client.class));
    }

    @Test
    public void testGetClientById() throws Exception {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setId(1L);

        // Мокируем сервис
        when(clientService.getClientById(1L)).thenReturn(client);

        // Выполняем GET-запрос и проверяем ответ
        mockMvc.perform(get("/api/clients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        // Проверяем, что метод getClientById был вызван
        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    public void testGetAllClients() throws Exception {
        Client client1 = new Client();
        client1.setFirstName("John");
        client1.setLastName("Doe");
        Client client2 = new Client();
        client2.setFirstName("Jane");
        client2.setLastName("Smith");

        List<Client> clients = Arrays.asList(client1, client2);

        // Мокируем сервис
        when(clientService.getAllClients()).thenReturn(clients);

        // Выполняем GET-запрос и проверяем ответ
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        // Проверяем, что метод getAllClients был вызван
        verify(clientService, times(1)).getAllClients();
    }

    @Test
    public void testGetClientsByStatus() throws Exception {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setStatus("active");

        List<Client> clients = List.of(client);

        // Мокируем сервис
        when(clientService.getClientsByStatus("active")).thenReturn(clients);

        // Выполняем GET-запрос и проверяем ответ
        mockMvc.perform(get("/api/clients/status/{status}", "active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("active"));

        // Проверяем, что метод getClientsByStatus был вызван
        verify(clientService, times(1)).getClientsByStatus("active");
    }

    @Test
    public void testUpdateClient() throws Exception {
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setId(1L);

        // Мокируем сервис
        when(clientService.updateClient(eq(1L), any(Client.class))).thenReturn(client);

        // Выполняем PUT-запрос и проверяем ответ
        mockMvc.perform(put("/api/clients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"lastName\": \"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        // Проверяем, что метод updateClient был вызван
        verify(clientService, times(1)).updateClient(eq(1L), any(Client.class));
    }

    @Test
    public void testDeleteClient() throws Exception {
        // Мокируем сервис
        when(clientService.deleteClient(1L)).thenReturn(true);

        // Выполняем DELETE-запрос и проверяем ответ
        mockMvc.perform(delete("/api/clients/{id}", 1L))
                .andExpect(status().isNoContent());

        // Проверяем, что метод deleteClient был вызван
        verify(clientService, times(1)).deleteClient(1L);
    }
}
