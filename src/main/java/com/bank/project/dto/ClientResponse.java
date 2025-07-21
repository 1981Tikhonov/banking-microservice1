package com.bank.project.dto;

import com.bank.project.entity.Client;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String passportSeries;
    private String passportNumber;
    private String departmentCode;
    private String issuedBy;
    private LocalDate issueDate;
    private String registrationAddress;
    private String status;

    public static ClientResponse fromEntity(Client client) {
        if (client == null) {
            return null;
        }
        
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setFirstName(client.getFirstName());
        response.setLastName(client.getLastName());
        response.setMiddleName(client.getMiddleName());
        response.setBirthDate(client.getBirthDate());
        response.setEmail(client.getEmail());
        response.setPhone(client.getPhone());
        response.setPassportSeries(client.getPassportSeries());
        response.setPassportNumber(client.getPassportNumber());
        response.setDepartmentCode(client.getDepartmentCode());
        response.setIssuedBy(client.getIssuedBy());
        response.setIssueDate(client.getIssueDate());
        response.setRegistrationAddress(client.ge0tRegistrationAddress());
        response.setStatus(client.getStatus());
        
        return response;
    }
}
