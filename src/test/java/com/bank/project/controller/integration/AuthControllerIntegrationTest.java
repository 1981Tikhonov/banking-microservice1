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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String TEST_USERNAME = "test.manager@bank.com";
    private static final String TEST_PASSWORD = "securePassword123";

    @BeforeEach
    void setUp() {
        managerRepository.deleteAll();

        Manager manager = new Manager();
        manager.setFirstName("Test");
        manager.setLastName("Manager");
        manager.setEmail(TEST_USERNAME);
        manager.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        manager.setStatus(ManagerStatus.ACTIVE);
        manager.setPosition("Test Manager");
        manager.setPhone("+1234567890");

        managerRepository.save(manager);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnJwtToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("username", TEST_USERNAME)
                        .param("password", TEST_PASSWORD)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyString())));
    }

    @Test
    void login_WithInvalidPassword_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("username", TEST_USERNAME)
                        .param("password", "wrongPassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void login_WithNonExistentUser_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("username", "nonexistent@bank.com")
                        .param("password", "anyPassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void login_WithInactiveAccount_ShouldReturnUnauthorized() throws Exception {
        Manager inactiveManager = new Manager();
        inactiveManager.setFirstName("Inactive");
        inactiveManager.setLastName("Manager");
        inactiveManager.setEmail("inactive@bank.com");
        inactiveManager.setPassword(passwordEncoder.encode("password123"));
        inactiveManager.setStatus(ManagerStatus.INACTIVE);
        inactiveManager.setPosition("Test Inactive");
        inactiveManager.setPhone("+1234567891");

        managerRepository.save(inactiveManager);

        mockMvc.perform(post("/api/auth/login")
                        .param("username", "inactive@bank.com")
                        .param("password", "password123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Account is not active"));
    }

    @Test
    void refreshToken_WithValidToken_ShouldReturnNewToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .param("username", TEST_USERNAME)
                        .param("password", TEST_PASSWORD)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();

        String token = result.getResponse().getContentAsString();

        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string(not(emptyString())));
    }

    @Test
    void refreshToken_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_ShouldInvalidateToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .param("username", TEST_USERNAME)
                        .param("password", TEST_PASSWORD)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();

        String token = result.getResponse().getContentAsString();

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful"));
    }
}
