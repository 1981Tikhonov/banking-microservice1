package com.bank.project.repository;

import com.bank.project.entity.Manager;
import com.bank.project.entity.enums.ManagerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ManagerRepositoryTest {

    @Autowired
    private ManagerRepository managerRepository;

    private Manager manager1;

    @BeforeEach
    void setUp() {
        // Создаем примеры менеджеров для тестирования
        manager1 = new Manager();
        manager1.setUsername("manager1");
        manager1.setFirstName("John");
        manager1.setLastName("Doe");
        manager1.setStatus(ManagerStatus.valueOf("active"));
        manager1.setRole("admin");

        Manager manager2 = new Manager();
        manager2.setUsername("manager2");
        manager2.setFirstName("Jane");
        manager2.setLastName("Smith");
        manager2.setStatus(ManagerStatus.valueOf("inactive"));
        manager2.setRole("user");

        // Сохраняем данные в базу для тестирования
        managerRepository.save(manager1);
        managerRepository.save(manager2);
    }

    @Test
    void testFindById() {
        // Проверка метода поиска менеджера по ID
        Optional<Manager> managerOptional = managerRepository.findById(manager1.getId());
        assertThat(managerOptional).isPresent();
        assertThat(managerOptional.get().getId()).isEqualTo(manager1.getId());
    }

    @Test
    void testFindByUsername() {
        // Проверка метода поиска менеджера по имени пользователя
        Optional<Manager> managerOptional = managerRepository.findByUsername("manager1");
        assertThat(managerOptional).isPresent();
        assertThat(managerOptional.get().getUsername()).isEqualTo("manager1");
    }

    @Test
    void testFindByFirstName() {
        // Проверка метода поиска менеджера по имени
        Optional<Manager> managerOptional = managerRepository.findByFirstName("John");
        assertThat(managerOptional).isPresent();
        assertThat(managerOptional.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void testFindByLastName() {
        // Проверка метода поиска менеджера по фамилии
        Optional<Manager> managerOptional = managerRepository.findByLastName("Smith");
        assertThat(managerOptional).isPresent();
        assertThat(managerOptional.get().getLastName()).isEqualTo("Smith");
    }

    @Test
    void testFindByStatus() {
        // Проверка метода поиска менеджеров по статусу
        List<Manager> activeManagers = managerRepository.findByStatus(ManagerStatus.ACTIVE);
        assertThat(activeManagers).hasSize(1);
        assertThat(activeManagers.get(0).getStatus()).isEqualTo(ManagerStatus.ACTIVE);
    }

    @Test
    void testFindByRole() {
        // Проверка метода поиска менеджеров по роли
        List<Manager> admins = managerRepository.findByRole("admin");
        assertThat(admins).hasSize(1);
        assertThat(admins.get(0).getRole()).isEqualTo("admin");
    }

    @Test
    void testExistsByUsername() {
        // Проверка существования имени пользователя
        boolean exists = managerRepository.existsByUsername("manager1");
        assertThat(exists).isTrue();

        exists = managerRepository.existsByUsername("nonexistent");
        assertThat(exists).isFalse();
    }
}
