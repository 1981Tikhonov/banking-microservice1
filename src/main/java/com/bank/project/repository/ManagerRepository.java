package com.bank.project.repository;

import com.bank.project.entity.Manager;
import com.bank.project.entity.enums.ManagerStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    @NotNull Optional<Manager> findById(@NotNull Long id); // Поиск по ID

    Optional<Manager> findByUsername(String username); // Поиск по имени пользователя

    Optional<Manager> findByFirstName(String firstName); // Поиск по имени

    Optional<Manager> findByLastName(String lastName); // Поиск по фамилии

    List<Manager> findByStatus(ManagerStatus status); // Поиск по статусу

    List<Manager> findByRole(String role); // Поиск по роли

    boolean existsByUsername(String username); // Проверка существования имени пользователя
}
