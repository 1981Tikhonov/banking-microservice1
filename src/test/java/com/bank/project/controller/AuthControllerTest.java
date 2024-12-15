package com.bank.project.controller;

import com.bank.project.security.JwtTokenProvider;
import com.bank.project.service.ManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testLogin_Success() throws Exception {
        String username = "manager";
        String password = "password";
        String token = "mock-jwt-token";

        // Настроим мок-объекты
        when(managerService.isManagerActive(username)).thenReturn(true);
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.createToken(authentication)).thenReturn(token);

        // Выполняем запрос и проверяем результат
        mockMvc.perform(post("/api/auth/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(token));

        // Проверяем, что методы были вызваны
        verify(managerService, times(1)).isManagerActive(username);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).createToken(authentication);
    }

    @Test
    public void testLogin_ManagerNotActive() throws Exception {
        String username = "inactiveManager";
        String password = "password";

        // Настроим мок-объект, чтобы менеджер был неактивным
        when(managerService.isManagerActive(username)).thenReturn(false);

        // Выполняем запрос и проверяем результат
        mockMvc.perform(post("/api/auth/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Manager is not active"));

        // Проверяем, что метод isManagerActive был вызван
        verify(managerService, times(1)).isManagerActive(username);
        verify(authenticationManager, times(0)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testLogin_Failure_InvalidCredentials() throws Exception {
        String username = "manager";
        String password = "wrongPassword";

        // Настроим мок-объект, чтобы аутентификация не прошла
        when(managerService.isManagerActive(username)).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Выполняем запрос и проверяем результат
        mockMvc.perform(post("/api/auth/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));

        // Проверяем, что метод isManagerActive был вызван
        verify(managerService, times(1)).isManagerActive(username);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
