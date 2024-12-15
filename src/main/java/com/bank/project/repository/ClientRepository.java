package com.bank.project.repository;

import com.bank.project.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Найти всех клиентов по статусу
    List<Client> findByStatus(String status);

    // Найти всех клиентов по менеджеру
    List<Client> findByManagerId(Long managerId);

    // Найти всех клиентов по налоговому коду
    List<Client> findByTaxCode(String taxCode);

    // Найти всех клиентов по имени
    List<Client> findByFirstName(String firstName);

    // Найти всех клиентов по фамилии
    List<Client> findByLastName(String lastName);

    // Найти всех клиентов по email
    List<Client> findByEmail(String email);

    // Найти всех клиентов по телефону
    List<Client> findByPhone(String phone);

    // Найти всех клиентов по адресу
    List<Client> findByAddress(String address);

    // Найти всех клиентов по диапазону дат создания
    List<Client> findByCreatedAtBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    // Найти всех клиентов по дате обновления
    List<Client> findByUpdatedAtAfter(java.time.LocalDateTime updatedAt);
}
