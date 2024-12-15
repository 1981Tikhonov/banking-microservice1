package com.bank.project.controller;

import com.bank.project.entity.Manager;
import com.bank.project.service.ManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerService managerService;

    private Manager manager;

    @BeforeEach
    void setUp() {
        manager = new Manager(1L, "John", "Doe", "ACTIVE", "Admin", null, null, null);
    }

    @Test
    void testCreateManager() throws Exception {
        when(managerService.createManager(any(Manager.class))).thenReturn(manager);

        mockMvc.perform(post("/api/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"status\":\"ACTIVE\",\"role\":\"Admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.role").value("Admin"));

        verify(managerService, times(1)).createManager(any(Manager.class));
    }

    @Test
    void testGetManagerById() throws Exception {
        when(managerService.getManagerById(anyLong())).thenReturn(manager);

        mockMvc.perform(get("/api/managers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.role").value("Admin"));

        verify(managerService, times(1)).getManagerById(anyLong());
    }

    @Test
    void testGetManagerByIdNotFound() throws Exception {
        when(managerService.getManagerById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/managers/1"))
                .andExpect(status().isNotFound());

        verify(managerService, times(1)).getManagerById(anyLong());
    }

    @Test
    void testGetAllManagers() throws Exception {
        when(managerService.getAllManagers()).thenReturn(Collections.singletonList(manager));

        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));

        verify(managerService, times(1)).getAllManagers();
    }

    @Test
    void testUpdateManager() throws Exception {
        when(managerService.updateManager(anyLong(), any(Manager.class))).thenReturn(manager);

        mockMvc.perform(put("/api/managers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"status\":\"ACTIVE\",\"role\":\"Admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.role").value("Admin"));

        verify(managerService, times(1)).updateManager(anyLong(), any(Manager.class));
    }

    @Test
    void testUpdateManagerNotFound() throws Exception {
        when(managerService.updateManager(anyLong(), any(Manager.class))).thenReturn(null);

        mockMvc.perform(put("/api/managers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"status\":\"ACTIVE\",\"role\":\"Admin\"}"))
                .andExpect(status().isNotFound());

        verify(managerService, times(1)).updateManager(anyLong(), any(Manager.class));
    }

    @Test
    void testDeleteManager() throws Exception {
        when(managerService.deleteManager(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/api/managers/1"))
                .andExpect(status().isNoContent());

        verify(managerService, times(1)).deleteManager(anyLong());
    }

    @Test
    void testDeleteManagerNotFound() throws Exception {
        when(managerService.deleteManager(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/managers/1"))
                .andExpect(status().isNotFound());

        verify(managerService, times(1)).deleteManager(anyLong());
    }

    @Test
    void testFindManagerByFirstName() throws Exception {
        when(managerService.findManagerByFirstName(anyString())).thenReturn(Optional.of(manager));

        mockMvc.perform(get("/api/managers/first-name/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(managerService, times(1)).findManagerByFirstName(anyString());
    }

    @Test
    void testFindManagerByFirstNameNotFound() throws Exception {
        when(managerService.findManagerByFirstName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/managers/first-name/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(managerService, times(1)).findManagerByFirstName(anyString());
    }

    @Test
    void testFindManagersByStatus() throws Exception {
        when(managerService.findManagersByStatus(anyString())).thenReturn(Collections.singletonList(manager));

        mockMvc.perform(get("/api/managers/status/ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(managerService, times(1)).findManagersByStatus(anyString());
    }

    @Test
    void testFindManagersByRole() throws Exception {
        when(managerService.findManagersByRole(anyString())).thenReturn(Collections.singletonList(manager));

        mockMvc.perform(get("/api/managers/role/Admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].role").value("Admin"));

        verify(managerService, times(1)).findManagersByRole(anyString());
    }
}
