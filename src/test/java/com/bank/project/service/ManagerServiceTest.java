package com.bank.project.service;

import com.bank.project.entity.Manager;
import com.bank.project.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ManagerService managerService;

    private Manager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создание mock менеджера для тестов
        manager = new Manager();
        manager.setId(1L);
        manager.setUsername("john_doe");
        manager.setPassword("encoded_password");
        manager.setFirstName("John");
        manager.setLastName("Doe");
        manager.setStatus("ACTIVE");
        manager.setRole("ADMIN");
        manager.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testLoadUserByUsername() {
        when(managerRepository.findByUsername("john_doe")).thenReturn(Optional.of(manager));

        UserDetails userDetails = managerService.loadUserByUsername("john_doe");

        assertNotNull(userDetails);
        assertEquals("john_doe", userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(managerRepository.findByUsername("non_existent_user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> managerService.loadUserByUsername("non_existent_user"));
    }

    @Test
    void testCreateManager() {
        when(managerRepository.existsByUsername("john_doe")).thenReturn(false);
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");

        manager.setPassword("password");
        Manager createdManager = managerService.createManager(manager);

        assertNotNull(createdManager);
        assertEquals("john_doe", createdManager.getUsername());
        verify(managerRepository, times(1)).save(manager);
    }

    @Test
    void testCreateManager_UsernameAlreadyExists() {
        when(managerRepository.existsByUsername("john_doe")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> managerService.createManager(manager));
    }

    @Test
    void testUpdateManager() {
        Manager updatedManagerData = new Manager();
        updatedManagerData.setFirstName("Updated");
        updatedManagerData.setLastName("Name");
        updatedManagerData.setStatus("INACTIVE");
        updatedManagerData.setRole("USER");

        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);

        Manager updatedManager = managerService.updateManager(1L, updatedManagerData);

        assertEquals("Updated", updatedManager.getFirstName());
        assertEquals("Name", updatedManager.getLastName());
        assertEquals("INACTIVE", updatedManager.getStatus());
        assertEquals("USER", updatedManager.getRole());
    }

    @Test
    void testDeleteManager() {
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        boolean result = managerService.deleteManager(1L);

        assertTrue(result);
        verify(managerRepository, times(1)).delete(manager);
    }

    @Test
    void testGetManagerById() {
        when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        Manager foundManager = managerService.getManagerById(1L);

        assertNotNull(foundManager);
        assertEquals("john_doe", foundManager.getUsername());
    }

    @Test
    void testGetManagerById_NotFound() {
        when(managerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> managerService.getManagerById(1L));
    }

    @Test
    void testFindManagersByStatus() {
        when(managerRepository.findByStatus("ACTIVE")).thenReturn(List.of(manager));

        List<Manager> managers = managerService.findManagersByStatus("ACTIVE");

        assertNotNull(managers);
        assertFalse(managers.isEmpty());
        assertEquals("ACTIVE", managers.get(0).getStatus());
    }

    @Test
    void testFindManagersByRole() {
        when(managerRepository.findByRole("ADMIN")).thenReturn(List.of(manager));

        List<Manager> managers = managerService.findManagersByRole("ADMIN");

        assertNotNull(managers);
        assertFalse(managers.isEmpty());
        assertEquals("ADMIN", managers.get(0).getRole());
    }

    @Test
    void testFindManagerByFirstName() {
        when(managerRepository.findByFirstName("John")).thenReturn(Optional.of(manager));

        Optional<Manager> foundManager = managerService.findManagerByFirstName("John");

        assertTrue(foundManager.isPresent());
        assertEquals("John", foundManager.get().getFirstName());
    }

    @Test
    void testFindManagerByLastName() {
        when(managerRepository.findByLastName("Doe")).thenReturn(Optional.of(manager));

        Optional<Manager> foundManager = managerService.findManagerByLastName("Doe");

        assertTrue(foundManager.isPresent());
        assertEquals("Doe", foundManager.get().getLastName());
    }

    @Test
    void testIsManagerActive() {
        // В данный момент метод всегда возвращает false, но можно добавить реальную логику для проверки
        assertFalse(managerService.isManagerActive("john_doe"));
    }
}
