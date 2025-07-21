package com.bank.project.dto;

import com.bank.project.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String status;
    private String taxCode;
    private String passportSeries;
    private String passportNumber;
    private String departmentCode;
    private String issuedBy;
    private LocalDate issueDate;
    private String registrationAddress;
    private String residentialAddress;
    private Long managerId;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Explicitly define all getters and setters to ensure they're available at runtime
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getTaxCode() { return taxCode; }
    public void setTaxCode(String taxCode) { this.taxCode = taxCode; }
    
    public String getPassportSeries() { return passportSeries; }
    public void setPassportSeries(String passportSeries) { this.passportSeries = passportSeries; }
    
    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    
    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
    
    public String getIssuedBy() { return issuedBy; }
    public void setIssuedBy(String issuedBy) { this.issuedBy = issuedBy; }
    
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    
    public String getRegistrationAddress() { return registrationAddress; }
    public void setRegistrationAddress(String registrationAddress) { this.registrationAddress = registrationAddress; }
    
    public String getResidentialAddress() { return residentialAddress; }
    public void setResidentialAddress(String residentialAddress) { this.residentialAddress = residentialAddress; }
    
    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static ClientResponse fromEntity(Client client) {
        if (client == null) {
            return null;
        }
        
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setFirstName(client.getFirstName());
        response.setMiddleName(client.getMiddleName());
        response.setLastName(client.getLastName());
        response.setBirthDate(client.getBirthDate());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        response.setStatus(client.getStatus());
        response.setTaxCode(client.getTaxCode());
        response.setPassportSeries(client.getPassportSeries());
        response.setPassportNumber(client.getPassportNumber());
        response.setDepartmentCode(client.getDepartmentCode());
        response.setIssuedBy(client.getIssuedBy());
        response.setIssueDate(client.getIssueDate());
        response.setRegistrationAddress(client.getRegistrationAddress());
        response.setResidentialAddress(client.getResidentialAddress());
        response.setManagerId(client.getManagerId());
        response.setAddress(client.getAddress());
        response.setCreatedAt(client.getCreatedAt());
        response.setUpdatedAt(client.getUpdatedAt());
        
        return response;
    }
}
