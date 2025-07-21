package com.bank.project.controller;

import com.bank.project.dto.CreateClientRequest;
import com.bank.project.entity.Client;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public class ClientService {
    public Client createClient(@Valid CreateClientRequest request) {
            return null;
    }

    public Optional<Object> getClientById(Long id) {
            return null;
    }

    public List<Client> getAllClients(int pageNumber, int pageSize, String[] sort) {
            return null;
    }

    public List<Client> getClientsByStatus(String upperCase, int pageNumber, int pageSize) {
            return null;
    }

    public Client updateClient(Long id, @Valid CreateClientRequest request) {
            return null;
    }

    public void deleteClient(Long id) {

    }
}
