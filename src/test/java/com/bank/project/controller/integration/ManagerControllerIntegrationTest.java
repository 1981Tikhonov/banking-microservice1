package com.bank.project.controller.integration;

import com.bank.project.entity.Manager;
import com.bank.project.entity.enums.ManagerStatus;
import com.bank.project.repository.ManagerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ManagerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ManagerRepository managerRepository;

    private Manager testManager;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        managerRepository.deleteAll();

        // Create test manager
        testManager = new Manager();
        testManager.setFirstName("John");
        testManager.setLastName("Doe");
        testManager.setEmail("john.doe@bank.com");
        testManager.setPhone("+1234567890");
        testManager.setStatus(ManagerStatus.ACTIVE);
        testManager.setPosition("Senior Manager");
        testManager = managerRepository.save(testManager);
    }

    @Test
    @com.bank.project.controller.integration.WithMockUser(roles = "ADMIN")
    void createManager_ShouldReturnCreatedManager() throws Exception {
        String managerJson = """
            {
                "firstName": "Jane",
                "lastName": "Smith",
                "email": "jane.smith@bank.com",
                "phone": "+1987654321",
                "status": "ACTIVE",
                "position": "Manager",
                "hireDate": "2023-01-15"
            }
            """;

        mockMvc.perform(post("/api/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(managerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@bank.com"));
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void getManagerById_ShouldReturnManager() throws Exception {
        mockMvc.perform(get("/api/managers/{id}", testManager.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testManager.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateManager_ShouldUpdateManager() throws Exception {
        String updateJson = """
            {
                "firstName": "John Updated",
                "lastName": "Doe Updated",
                "position": "Senior Director",
                "status": "ON_LEAVE"
            }
            """;

        mockMvc.perform(put("/api/managers/{id}", testManager.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John Updated"))
                .andExpect(jsonPath("$.lastName").value("Doe Updated"))
                .andExpect(jsonPath("$.position").value("Senior Director"))
                .andExpect(jsonPath("$.status").value("ON_LEAVE"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteManager_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/managers/{id}", testManager.getId()))
                .andExpect(status().isNoContent());

        // Verify manager is deleted
        mockMvc.perform(get("/api/managers/{id}", testManager.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void getAllManagers_ShouldReturnPageOfManagers() throws Exception {
        // Add more test managers
        for (int i = 0; i < 5; i++) {
            Manager manager = new Manager();
            manager.setFirstName("Manager" + i);
            manager.setLastName("Lastname" + i);
            manager.setEmail("manager" + i + "@bank.com");
            manager.setStatus(ManagerStatus.ACTIVE);
            manager.setPosition("Manager");
            managerRepository.save(manager);
        }

        mockMvc.perform(get("/api/managers")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(lessThanOrEqualTo(5))))
                .andExpect(jsonPath("$.totalElements").value(6)); // 1 from setup + 5 new
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void getManagersByStatus_ShouldReturnFilteredManagers() throws Exception {
        // Create an inactive manager
        Manager inactiveManager = new Manager();
        inactiveManager.setFirstName("Inactive");
        inactiveManager.setLastName("Manager");
        inactiveManager.setEmail("inactive@bank.com");
        inactiveManager.setStatus(ManagerStatus.INACTIVE);
        inactiveManager.setPosition("Former Manager");
        managerRepository.save(inactiveManager);

        mockMvc.perform(get("/api/managers/status/INACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("INACTIVE"));
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    void searchManagers_ShouldReturnMatchingManagers() throws Exception {
        // Search by first name
        mockMvc.perform(get("/api/managers/search")
                .param("query", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].firstName").value(containsString("John")));

        // Search by last name
        mockMvc.perform(get("/api/managers/search")
                .param("query", "Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].lastName").value(containsString("Doe")));

        // Search by email
        mockMvc.perform(get("/api/managers/search")
                .param("query", "bank.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].email").value(containsString("bank.com")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createManager_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        String invalidManagerJson = """
            {
                "firstName": "",
                "email": "invalid-email",
                "status": "INVALID_STATUS"
            }
            """;

        mockMvc.perform(post("/api/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidManagerJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))));
    }

    @Test
    @com.bank.project.controller.integration.WithMockUser(username = "user", roles = {"USER"})
    void accessDenied_ForNonAdminUsers() throws Exception {
        // Regular users shouldn't be able to create managers
        String managerJson = """
            {
                "firstName": "Test",
                "lastName": "User",
                "email": "test@bank.com"
            }
            """;

        mockMvc.perform(post("/api/managers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(managerJson))
                .andExpect(status().isForbidden());
    }
}
