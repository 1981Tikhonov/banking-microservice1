package com.bank.project.service;

import com.bank.project.entity.Manager;
import com.bank.project.entity.enums.ManagerStatus;
import com.bank.project.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerServiceImplTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;

    private Manager testManager;

    @BeforeEach
    void setUp() {
        testManager = new Manager();
        testManager.setId(1L);
        testManager.setFirstName("John");
        testManager.setLastName("Doe");
        testManager.setStatus(ManagerStatus.valueOf("ACTIVE"));
        testManager.setRole("ADMIN");
    }

    @Test
    void createManager_ShouldReturnCreatedManager() {
        // Arrange
        when(managerRepository.save(any(Manager.class))).thenReturn(testManager);

        // Act
        Manager createdManager = managerService.createManager(testManager);

        // Assert
        assertNotNull(createdManager);
        assertEquals("John", createdManager.getFirstName());
        verify(managerRepository, times(1)).save(any(Manager.class));
    }

    @Test
    void getManagerById_WhenManagerExists_ShouldReturnManager() {
        // Arrange
        when(managerRepository.findById(1L)).thenReturn(Optional.of(testManager));

        // Act
        Manager foundManager = managerService.getManagerById(1L);

        // Assert
        assertNotNull(foundManager);
        assertEquals("John", foundManager.getFirstName());
    }

    @Test
    void getManagerById_WhenManagerNotExists_ShouldReturnNull() {
        // Arrange
        when(managerRepository.findById(999L)).thenReturn(Optional.empty());


        // Act
        Manager foundManager = managerService.getManagerById(999L);

        // Assert
        assertNull(foundManager);
    }


    @Test
    void getAllManagers_ShouldReturnPageOfManagers() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Manager> page = new PageImpl<>(List.of(testManager));
        when(managerRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Manager> result = managerService.getAllManagers(0, 10, new String[]{"id,asc"});

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getFirstName());
    }

    @Test
    void updateManager_WhenManagerExists_ShouldUpdateManager() {
        // Arrange
        Manager updatedManager = new Manager();
        updatedManager.setFirstName("Jane");
        updatedManager.setLastName("Smith");

        when(managerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        when(managerRepository.save(any(Manager.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Manager result = managerService.updateManager(1L, updatedManager);

        // Assert
        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("ACTIVE", result.getStatus());
    }

    @Test
    void deleteManager_WhenManagerExists_ShouldReturnTrue() {
        // Arrange
        when(managerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(managerRepository).deleteById(1L);

        // Act
        boolean result = managerService.deleteManager(1L);

        // Assert
        assertTrue(result);
        verify(managerRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteManager_WhenManagerNotExists_ShouldReturnFalse() {
        // Arrange
        when(managerRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = managerService.deleteManager(999L);
        // Assert
        assertFalse(result);
        verify(managerRepository, never()).deleteById(anyLong());
    }
}
