package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.CommercialOperationMapper;
import org.techniu.isbackend.dto.mapper.ContactMapper;
import org.techniu.isbackend.dto.model.CommercialOperationDto;
import org.techniu.isbackend.dto.model.ContactDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class CommercialOperationServiceImpl implements CommercialOperationService{
    private CommercialOperationRepository commercialOperationRepository;
    private ClientRepository clientRepository;
    private ContactRepository contactRepository;
    private ServiceTypeService serviceTypeService;
    private LogService logService;
    private ServiceTypeRepository serviceTypeRepository;
    private CommercialOperationStatusRepository commercialOperationStatusRepository;
    private final ContactMapper contactMapper = Mappers.getMapper(ContactMapper.class);
    private final CommercialOperationMapper commercialOperationMapper = Mappers.getMapper(CommercialOperationMapper.class);
    CommercialOperationServiceImpl(CommercialOperationRepository commercialOperationRepository,
                                   CommercialOperationStatusRepository commercialOperationStatusRepository,
                                   ClientRepository clientRepository, ContactRepository contactRepository, ServiceTypeService serviceTypeService, LogService logService, ServiceTypeRepository serviceTypeRepository) {
        this.commercialOperationRepository = commercialOperationRepository;
        this.clientRepository = clientRepository;
        this.commercialOperationStatusRepository = commercialOperationStatusRepository;
        this.contactRepository = contactRepository;
        this.serviceTypeService = serviceTypeService;
        this.logService = logService;
        this.serviceTypeRepository = serviceTypeRepository;
    }
    @Override
    public void save(CommercialOperationDto commercialOperationDto) {
        // save country if note existe
       // commercialOperationDto.setName(commercialOperationDto.getName().toLowerCase());
        Client client = clientRepository.getBy_id(commercialOperationDto.getClientId());
        CommercialOperationStatus commercialOperationStatus = commercialOperationStatusRepository.findBy_id(commercialOperationDto.getStateId());
        //System.out.println(commercialOperationDto.getStateId());
        int len = this.getAll().size() + 1;
        String code;
        //City city=cityRepository.findCityBy_id(cityId);
       // String country = city.getStateCountry().getCountry().getCountryName().length() > 3 ? city.getStateCountry().getCountry().getCountryName().substring(0,3).toUpperCase() : city.getStateCountry().getCountry().getCountryName().toUpperCase();
        if (len < 9) {
            len+=1;
            code = "-00" + len;
            commercialOperationDto.setCode(code);
        }
        if (len < 99) {
            len+=1;
            code = "-0" + len;
            commercialOperationDto.setCode(code);
        } else {
            len+=1;
            code =  "-" + len;
            commercialOperationDto.setCode(code);
        }
        if (commercialOperationDto.getName().contains(" ")) {
             throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        Optional<CommercialOperation>  commercialOperation= Optional.ofNullable(commercialOperationRepository.findByNameIgnoreCase(commercialOperationDto.getName()));
        if (commercialOperation.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        Optional<CommercialOperation>  commercialOperation1= Optional.ofNullable(commercialOperationRepository.findByCode(commercialOperationDto.getCode()));
        if (commercialOperation1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        ArrayList<ServiceType> serviceTypes = new ArrayList<>();
        List<String> serviceTypesIds = commercialOperationDto.getServiceTypeId();
        for (String serviceId : serviceTypesIds) {
            serviceTypes.add(serviceTypeRepository.findBy_id(serviceId));
        }
        CommercialOperation commercialOperation3 = commercialOperationMapper.dtoToModel(commercialOperationDto);
        commercialOperation3.setClient(client);
        commercialOperation3.setState(commercialOperationStatus);
        commercialOperation3.setServiceType(serviceTypes);
        ArrayList<Contact> contacts = new ArrayList<>();
        for (String id : commercialOperationDto.getContactsIds()){
            contacts.add(contactRepository.findBy_id(id));
        }
        commercialOperation3.setContacts(contacts);
        commercialOperationRepository.save(commercialOperation3);
        logService.addLog(LogType.CREATE, ClassType.OPERATION,"create operation "+commercialOperationDto.getName());
    }

    @Override
    public void update(CommercialOperationDto commercialOperationDto) {
        // save country if note existe
        commercialOperationDto.setName(commercialOperationDto.getName().toLowerCase());
        if (commercialOperationDto.getName().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
       Optional<CommercialOperation> commercialOperation1 = Optional.ofNullable(commercialOperationRepository.findBy_id(commercialOperationDto.getCommercialOperationId()));
        if (!commercialOperation1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        Optional<CommercialOperation> commercialOperation2 = Optional.ofNullable(commercialOperationRepository.findByName(commercialOperationDto.getName()));

        if (commercialOperation2.isPresent() && !(commercialOperation1.get().getName().equals(commercialOperationDto.getName())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
        Optional<CommercialOperation> commercialOperation3 = Optional.ofNullable(commercialOperationRepository.findByCode(commercialOperationDto.getCode()));
        if (commercialOperation3.isPresent() && !(commercialOperation1.get().getCode().equals(commercialOperationDto.getCode())) ) {
            throw exception(DUPLICATE_ENTITY);
        }

        Client client = clientRepository.findBy_id(commercialOperationDto.getClientId());
        commercialOperationDto.setClient(client);
        CommercialOperationStatus commercialOperationStatus = commercialOperationStatusRepository.findBy_id(commercialOperationDto.getStateId());
        commercialOperationDto.setState(commercialOperationStatus);
         commercialOperationRepository.save(commercialOperationMapper.dtoToModel(commercialOperationDto));
        logService.addLog(LogType.UPDATE, ClassType.OPERATION,"update operation "+commercialOperationDto.getName());
    }

    @Override
    public List<CommercialOperationDto> getAll() {
        // Get all commercialOperations
        List<CommercialOperation> commercialOperations = commercialOperationRepository.findAll();
        // Create a list of all commercialOperation dto
        ArrayList<CommercialOperationDto> commercialOperationDtos = new ArrayList<>();

        for (CommercialOperation commercialOperation : commercialOperations) {
            CommercialOperationDto commercialOperationDto = commercialOperationMapper.modelToDto(commercialOperation);
            commercialOperationDto.setClientName(commercialOperation.getClient().getName());
            commercialOperationDto.setClientId(commercialOperation.getClient().get_id());
            commercialOperationDto.setStateId(commercialOperation.getState().get_id());
            commercialOperationDto.setStateName(commercialOperation.getState().getName());
            commercialOperationDto.setSector1(commercialOperation.getClient().getSector1());
            commercialOperationDto.setSector2(commercialOperation.getClient().getSector2());
            commercialOperationDto.setSector3(commercialOperation.getClient().getSector3());
            //commercialOperationDto.set(commercialOperation.getState().getPercentage());
            commercialOperationDto.setCountryName(commercialOperation.getClient().getAddress().getCity().getStateCountry().getCountry().getCountryName());
            ArrayList<String> serviceTypes = new ArrayList<>();
            if (commercialOperation.getServiceType() != null){
               int a= commercialOperation.getServiceType().size();
                for (ServiceType servicetype : commercialOperation.getServiceType()) {
                    serviceTypes.add(servicetype.getName());
                    a=a-1;
                    if(a>0) {
                        serviceTypes.add(" , ");
                    }

                }
            commercialOperationDto.setServiceTypeName(serviceTypes);
                commercialOperationDto.setStateName(commercialOperation.getState().getName());
        }
            ArrayList<ContactDto> contactDtos = new ArrayList<>();
            if (commercialOperation.getContacts() != null){
                for (Contact contact : commercialOperation.getContacts()) {
                    contactDtos.add(contactMapper.modelToDto(contact));
                }
                commercialOperationDto.setContactDtos(contactDtos);
            }
            commercialOperationDtos.add(commercialOperationDto);
        }
        return commercialOperationDtos;
    }

    @Override
    public List<CommercialOperationDto> getAll2() {
        // Get all actions
        List<CommercialOperation> commercialOperation = commercialOperationRepository.findAll();
        // Create a list of all actions dto
        ArrayList<CommercialOperationDto> commercialOperationDtos = new ArrayList<>();

        for (CommercialOperation commercialOperation1 : commercialOperation) {
            CommercialOperationDto commercialOperationDto = commercialOperationMapper.modelToDto(commercialOperation1);
            commercialOperationDtos.add(commercialOperationDto);
        }
        return commercialOperationDtos;
    }

    /**
     * delete Action
     *
     * @param id - id
     */
    @Override
    public void remove(String id) {
        Optional<CommercialOperation> action = Optional.ofNullable(commercialOperationRepository.findBy_id(id));
        // If CommercialOperation doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        commercialOperationRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.OPERATION,"delete operation "+action.get().getName());
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.CommercialOperation, exceptionType, args);
    }
}
