package org.techniu.isbackend.service;

import org.springframework.http.ResponseEntity;
import org.techniu.isbackend.entity.Client;

import java.util.List;

public interface ClientService {
    Client saveClient(Client client);
    Client getClientByCode(String codeClient);
    Client updateClient(String clientId, Client client);
    ResponseEntity<?> deleteClient(String clientId);
    List<Client> getAllClient();
    List<Client> getClientsByCountryName(String country);
}
