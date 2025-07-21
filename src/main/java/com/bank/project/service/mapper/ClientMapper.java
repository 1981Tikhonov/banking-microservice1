package com.bank.project.service.mapper;

import com.bank.project.dto.CreateClientRequest;
import com.bank.project.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    /**
     * Maps CreateClientRequest DTO to Client entity
     *
     * @param request the DTO to convert
     * @return the converted Client entity
     */
    default Client toEntity(CreateClientRequest request) {
        if (request == null) {
            return null;
        }

        Client client = new Client();
        client.setFirstName(request.getFirstName());
        client.setMiddleName(request.getMiddleName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setBirthDate(request.getBirthDate());
        client.setPassportSeries(request.getPassportSeries());
        client.setPassportNumber(request.getPassportNumber());
        client.setDepartmentCode(request.getDepartmentCode());
        client.setIssuedBy(request.getIssuedBy());
        client.setIssueDate(request.getIssueDate());
        client.setRegistrationAddress(request.getRegistrationAddress());
        client.setResidentialAddress(request.getRegistrationAddress()); // Map to residentialAddress
        client.setTaxCode(request.getTaxCode());
        client.setStatus("ACTIVE");
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        
        return client;
    }

    /**
     * Updates a Client entity with data from DTO
     *
     * @param dto    the DTO with updated data
     * @param entity the entity to update
     */
    default void updateFromDto(CreateClientRequest dto, Client entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getMiddleName() != null) {
            entity.setMiddleName(dto.getMiddleName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getBirthDate() != null) {
            entity.setBirthDate(dto.getBirthDate());
        }
        if (dto.getPassportSeries() != null) {
            entity.setPassportSeries(dto.getPassportSeries());
        }
        if (dto.getPassportNumber() != null) {
            entity.setPassportNumber(dto.getPassportNumber());
        }
        if (dto.getDepartmentCode() != null) {
            entity.setDepartmentCode(dto.getDepartmentCode());
        }
        if (dto.getIssuedBy() != null) {
            entity.setIssuedBy(dto.getIssuedBy());
        }
        if (dto.getIssueDate() != null) {
            entity.setIssueDate(dto.getIssueDate());
        }
        if (dto.getRegistrationAddress() != null) {
            entity.setRegistrationAddress(dto.getRegistrationAddress());
            entity.setResidentialAddress(dto.getRegistrationAddress());
        }
        if (dto.getTaxCode() != null) {
            entity.setTaxCode(dto.getTaxCode());
        }
        
        entity.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Maps CreateClientRequest to Client (alias for toEntity for backward compatibility)
     */
    default Client toClient(CreateClientRequest request) {
        return toEntity(request);
    }
}
