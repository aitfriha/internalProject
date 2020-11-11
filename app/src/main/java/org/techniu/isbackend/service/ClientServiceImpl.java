package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.ClientMapper;
import org.techniu.isbackend.dto.model.ClientDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService{
    private ClientRepository clientRepository;
    private AddressRepository addressRepository;
    private CityRepository cityRepository;
    private AddressService addressService;
    private StaffService  staffService;
    private StaffRepository staffRepository;
    private AssignmentRepository assignmentRepository;
    private CountryConfigRepository countryConfigRepository;
    private AssignmentService assignmentService;
    private CountryRepository countryRepository;
    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);
    ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository, AddressService addressService, StaffService staffService,
                      CountryConfigRepository countryConfigRepository,
                      AssignmentService assignmentService,
                      AssignmentRepository assignmentRepository,
                      StaffRepository staffRepository, CityRepository cityRepository, CountryRepository countryRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
        this.staffRepository = staffRepository;
        this.cityRepository = cityRepository;
        this.staffService = staffService;
        this.countryConfigRepository = countryConfigRepository;
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public void saveClient(Client client,Address address,String cityId,String AssistantCommercialId,String responsibleCommercialId) {
        int len = this.getAllClient().size();
        String code;
        City city=cityRepository.findCityBy_id(cityId);
        String country = city.getStateCountry().getCountry().getCountryName().length() > 3 ? city.getStateCountry().getCountry().getCountryName().substring(0,3).toUpperCase() : city.getStateCountry().getCountry().getCountryName().toUpperCase();
        if (len < 9) {
            len+=1;
            code = country + "-00" + len;
            client.setCode(code);
        }
        if (len < 99) {
            len+=1;
            code = country + "-0" + len;
            client.setCode(code);
        } else {
            len+=1;
            code = country + "-" + len;
            client.setCode(code);
        }
        ///Staff AssistantCommercial = staffRepository.findBy_id(AssistantCommercialId);
       // Staff responsibleCommercial = staffRepository.findBy_id(responsibleCommercialId);
       client.setAddress(addressService.saveAddress(address.setCity(city)));
       ///client.setAssistantCommercial(AssistantCommercial);
       ///client.setResponsibleCommercial(responsibleCommercial);

        clientRepository.save(client);
    }

    @Override
    public void updateClient(Client client, Address address, String cityId, String AssistantCommercialId, String responsibleCommercialId) {
        Client clientOld=clientRepository.getBy_id(client.get_id());
        City city=cityRepository.findCityBy_id(cityId);
        Address address1=clientOld.getAddress();
        System.out.println(address);
        client.setCode(clientOld.getCode());
        client.setResponsibleCommercial(clientOld.getResponsibleCommercial());
        client.setAssistantCommercial(clientOld.getAssistantCommercial());
        address1.setPostCode(address.getPostCode());
        address1.setFullAddress(address.getFullAddress());
        address1.setCity(city);
        address1.setPostCode(address.getPostCode());
        client.setSector1(clientOld.getSector1());
        client.setSector2(clientOld.getSector2());
        client.setSector3(clientOld.getSector3());
        client.setSectorLeader(clientOld.getSectorLeader());
        client.setAddress(addressService.saveAddress(addressService.saveAddress(address1)));

        clientRepository.save(client);
    }

    @Override
    public Client getClientByCode(String codeClient) {
        return clientRepository.getByCode(codeClient);
    }
/*
    @Override
    public Client updateClient(String clientId, Client client) {
        return clientRepository.findById(clientId).map(client1 -> {
            client.set_id(client1.get_id());
            return clientRepository.save(client);
        }).orElseThrow(() -> new ExceptionMessage("Cannot update client"));
    }*/

    @Override
    public ResponseEntity<?> deleteClient(String clientId) {
        return null;
    }

    @Override
    public List<ClientDto> getAllClient() {
        // Get all actions
        List<Client> clients = clientRepository.findAll();
        // Create a list of all actions dto
        ArrayList<ClientDto> clientsDtos = new ArrayList<>();

        for (Client client : clients) {
            ClientDto clientDto=clientMapper.modelToDto(client);
            clientsDtos.add(clientDto);
            clientDto.setCity(client.getAddress().getCity().getCityName());
            clientDto.setCountry(client.getAddress().getCity().getStateCountry().getCountry().getCountryName());
            clientDto.setAddressName(client.getAddress().getFullAddress());
            clientDto.setPostCode(client.getAddress().getPostCode());
            clientDto.setCountryId(client.getAddress().getCity().getStateCountry().getCountry().getCountryId());
            clientDto.setStateId(client.getAddress().getCity().getStateCountry().get_id());
            CountryConfig countryConfig=countryConfigRepository.getByCountry(client.getAddress().getCity().getStateCountry().getCountry());
            if(countryConfig !=null)
            {
               // clientDto.setCountryLeader(countryConfig.getLeader().getName());

            }
            else
            {
                clientDto.setCountryLeader("-");
            }
            //get assistant commercial if existe
            Assignment assignmentResponsible = assignmentRepository.findByClientAndTypeStaff(client,"Responsible Commercial");
            if(assignmentResponsible !=null) {
                clientDto.setResponsibleCommercial(assignmentResponsible.getStaff().getFirstName()+" "+assignmentResponsible.getStaff().getFatherFamilyName());
            }
            Assignment assignmentAssistant = assignmentRepository.findByClientAndTypeStaff(client,"Assistant Commercial");
            System.out.println("assignmentAssistant  ** " +assignmentAssistant);
            if(assignmentAssistant !=null) {
                clientDto.setAssistantCommercial(assignmentAssistant.getStaff().getFirstName()+" "+assignmentAssistant.getStaff().getFatherFamilyName());
            }
            //get responsable commercial if existe

        }
        return clientsDtos;
    }

    @Override
    public List<Client> getClientsByCountryName(String country) {
        List<Client> clients = clientRepository.findAll();
        System.out.println(clients);
        return clients.stream().filter(client -> client.getAddress().getCity().getStateCountry().getCountry().getCountryName().equals(country)).collect(Collectors.toList());
    }

    @Override
    public List<ClientDto> getListClientsByCountry(String country) {
        // Get all actions
        List<Client> clients = clientRepository.findAll();
        // Create a list of all actions dto
        ArrayList<ClientDto> clientsDtos = new ArrayList<>();
        Country country1=countryRepository.getByCountryName(country);
        for (Client client : clients) {
            if(client.getAddress().getCity().getStateCountry().getCountry().getCountryName().equals(country)) {
                ClientDto clientDto = clientMapper.modelToDto(client);
                clientsDtos.add(clientDto);
                clientDto.setCity(client.getAddress().getCity().getCityName());
                clientDto.setCountry(client.getAddress().getCity().getStateCountry().getCountry().getCountryName());
                clientDto.setAddressName(client.getAddress().getFullAddress());
                clientDto.setPostCode(client.getAddress().getPostCode());
                clientDto.setCountryId(client.getAddress().getCity().getStateCountry().getCountry().getCountryId());
                clientDto.setStateId(client.getAddress().getCity().getStateCountry().get_id());
                CountryConfig countryConfig = countryConfigRepository.getByCountry(client.getAddress().getCity().getStateCountry().getCountry());
                if (countryConfig != null) {
                    // clientDto.setCountryLeader(countryConfig.getLeader().getName());

                } else {
                    clientDto.setCountryLeader("-");
                }
                //get assistant commercial if existe
                Assignment assignmentResponsible = assignmentRepository.findByClientAndTypeStaff(client, "Responsible Commercial");
                if (assignmentResponsible != null) {
                    clientDto.setResponsibleCommercial(assignmentResponsible.getStaff().getFirstName() + " " + assignmentResponsible.getStaff().getFatherFamilyName());
                }
                Assignment assignmentAssistant = assignmentRepository.findByClientAndTypeStaff(client, "Assistant Commercial");
                System.out.println("assignmentAssistant  ** " + assignmentAssistant);
                if (assignmentAssistant != null) {
                    clientDto.setAssistantCommercial(assignmentAssistant.getStaff().getFirstName() + " " + assignmentAssistant.getStaff().getFatherFamilyName());
                }
                //get responsable commercial if existe
            }
        }
        return clientsDtos;
    }
}
