package com.bank.project.repository;

import com.bank.project.entity.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private Client client2;

    @BeforeEach
    void setUp() {
        // Создаем примеры данных для тестирования
        Client client1 = new Client();
        client1.setManagerId(1L);
        client1.setStatus("ACTIVE");
        client1.setTaxCode("1234567890");
        client1.setFirstName("John");
        client1.setLastName("Doe");
        client1.setEmail("john.doe@example.com");
        client1.setPhone("123456789");
        client1.setAddress("123 Main St");
        client1.setCreatedAt(LocalDateTime.now());
        client1.setUpdatedAt(LocalDateTime.now());

        client2 = new Client();
        client2.setManagerId(2L);
        client2.setStatus("INACTIVE");
        client2.setTaxCode("0987654321");
        client2.setFirstName("Jane");
        client2.setLastName("Smith");
        client2.setEmail("jane.smith@example.com");
        client2.setPhone("987654321");
        client2.setAddress("456 Another St");
        client2.setCreatedAt(LocalDateTime.now());
        client2.setUpdatedAt(LocalDateTime.now());

        // Сохраняем данные в базу для тестирования
        clientRepository.save(client1);
        clientRepository.save(client2);
    }

    @Test
    void testFindByStatus() {
        // Проверка метода поиска по статусу
        List<Client> clients = clientRepository.findByStatus("ACTIVE");
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void testFindByManagerId() {
        // Проверка метода поиска по managerId
        List<Client> clients = clientRepository.findByManagerId(1L);
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getManagerId()).isEqualTo(1L);
    }

    @Test
    void testFindByTaxCode() {
        // Проверка метода поиска по налоговому коду
        List<Client> clients = clientRepository.findByTaxCode("1234567890");
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getTaxCode()).isEqualTo("1234567890");
    }

    @Test
    void testFindByFirstName() {
        // Проверка метода поиска по имени
        List<Client> clients = clientRepository.findByFirstName("John");
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void testFindByLastName() {
        // Проверка метода поиска по фамилии
        List<Client> clients = clientRepository.findByLastName("Smith");
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getLastName()).isEqualTo("Smith");
    }

    @Test
    void testFindByEmail() {
        // Проверка метода поиска по email
        List<Client> clients = clientRepository.findByEmail("john.doe@example.com");
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testFindByPhone() {
        // Проверка метода поиска по телефону
        List<Client> clients = clientRepository.findByPhone("987654321");
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getPhone()).isEqualTo("987654321");
    }

    @Test
    void testFindByAddress() {
        // Проверка метода поиска по адресу
        List<Client> clients = clientRepository.findByAddress("123 Main St");
        assertThat(clients).hasSize(1);
        assertThat(clients.get(0).getAddress()).isEqualTo("123 Main St");
    }

    @Test
    void testFindByCreatedAtBetween() {
        // Проверка метода поиска по диапазону дат создания
        LocalDateTime now = LocalDateTime.now();
        List<Client> clients = clientRepository.findByCreatedAtBetween(now.minusDays(1), now.plusDays(1));
        assertThat(clients).hasSize(2);
    }

    @Test
    void testFindByUpdatedAtAfter() {
        // Проверка метода поиска по дате обновления
        LocalDateTime now = LocalDateTime.now();
        List<Client> clients = clientRepository.findByUpdatedAtAfter(now.minusDays(1));
        assertThat(clients).hasSize(2);
    }
}
