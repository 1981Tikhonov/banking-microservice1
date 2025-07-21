package com.bank.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "client")
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

        private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "department_code", nullable = false)
    @Getter
    private String departmentCode;

    @Getter
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "tax_code", unique = true)
    private String taxCode;

    @Getter
    @Column(name = "passport_series")
    private String passportSeries;

    @Getter
    @Column(name = "passport_number", unique = true)
    private String passportNumber;

    @Getter
    @Column(name = "issued_by")
    private String issuedBy;

    @Getter
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Getter
    @Column(name = "registration_address")
    private String registrationAddress;

    @Getter
    @Column(name = "residential_address")
    private String residentialAddress;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Client() {
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    public Client build() {
            return null;
    }

    public static class ClientBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String taxCode;
        private String address;
        private String status;
        private String departmentCode;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ClientBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ClientBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ClientBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ClientBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ClientBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public ClientBuilder taxCode(String taxCode) {
            this.taxCode = taxCode;
            return this;
        }

        public ClientBuilder address(String address) {
            this.address = address;
            return this;
        }

        public ClientBuilder departmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
            return this;
        }

        public ClientBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ClientBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ClientBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Client build() {
            Client client = new Client();
            client.setId(id);
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setEmail(email);
            client.setPhone(phone);
            client.setTaxCode(taxCode);
            client.setAddress(address);
            client.setStatus(status);
            client.setCreatedAt(createdAt);
            client.setUpdatedAt(updatedAt);
            return client;
        }

        public Object finalize(String jane) {
                return null;
        }

        public Object birthDate(LocalDate of) {
                return null;
        }


        public ClientBuilder middleName(@Size(max = 50, message = "Отчество должно содержать до 50 символов") String middleName) {
                    return null;
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String ge0tRegistrationAddress() {
            return null;
    }
}
