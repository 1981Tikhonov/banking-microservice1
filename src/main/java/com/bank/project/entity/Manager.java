package com.bank.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String status;

    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, unique = true)
    private String username;  // Имя пользователя для аутентификации

    @Column(nullable = false)
    private String password;  // Хешированный пароль

    @Column(nullable = false)
    private String role;  // Роль (например, ADMIN или USER)

    public Manager(long l, String john, String doe, String active, String admin, Object o, Object o1, Object o2) {

    }

    public Manager() {

    }
}
