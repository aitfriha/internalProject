package org.techniu.isbackend.service;

import org.springframework.http.ResponseEntity;
import org.techniu.isbackend.dto.model.ClientDto;
import org.techniu.isbackend.entity.Address;
import org.techniu.isbackend.entity.Client;
import org.techniu.isbackend.entity.SectorCompany;

import java.util.Date;
import java.util.List;

public interface ClientService {
    void saveClient(Client client, Address address,String cityId,String AssistantCommercialId,String responsibleCommercialId);

    void saveClientAssignement(Client client, Address address, String cityId,
                               Date startDateResponsibleCommercial, Date endDateResponsibleCommercial,
                               Date startDateAssistantCommercial, Date endDateAssistantCommercial,
                               String AssistantCommercialId, String responsibleCommercialId
    );

    void updateClient(Client client, Address address, String cityId, String AssistantCommercialId, String responsibleCommercialId);

    Client getClientByCode(String codeClient);
    //Client updateClient(String clientId, Client client);
    ResponseEntity<?> deleteClient(String clientId);
    List<ClientDto> getAllClient();
    List<Client> getClientsByCountryName(String country);
    List<ClientDto> getListClientsByCountry(String country);

    void deleteSectorFromclient(SectorCompany sectorCompany1,SectorCompany sectorCompany2,SectorCompany sectorCompany3);
}
