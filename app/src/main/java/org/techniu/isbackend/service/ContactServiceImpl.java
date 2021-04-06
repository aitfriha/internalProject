package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.ContactMapper;
import org.techniu.isbackend.dto.model.ContactDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.AddressRepository;
import org.techniu.isbackend.repository.CityRepository;
import org.techniu.isbackend.repository.ClientRepository;
import org.techniu.isbackend.repository.ContactRepository;
import org.techniu.isbackend.repository.CivilityTitleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class ContactServiceImpl implements ContactService{
    private ContactRepository contactRepository;
    private ClientRepository clientRepository;
    private CityRepository cityRepository;
    private CivilityTitleRepository civilityTitleRepository;
    private AddressRepository addressRepository;
    private LogService logService;
    private final ContactMapper contactMapper = Mappers.getMapper(ContactMapper.class);
    ContactServiceImpl(ContactRepository contactRepository, ClientRepository clientRepository, CityRepository cityRepository, CivilityTitleRepository civilityTitleRepository, AddressRepository addressRepository, LogService logService) {
        this.contactRepository = contactRepository;
        this.clientRepository = clientRepository;
        this.cityRepository = cityRepository;
        this.civilityTitleRepository = civilityTitleRepository;
        this.addressRepository = addressRepository;
        this.logService = logService;
    }
    @Override
    public void save(ContactDto contactDto, String companyId, Address address, String cityId) {
        // save Client if note existe
       Client client = clientRepository.getBy_id(companyId);
        City city =cityRepository.findCityBy_id(cityId);
        CivilityTitle civilityTitle = civilityTitleRepository.findBy_id(contactDto.getCivilityId());
        Optional<Contact>  contact= Optional.ofNullable(contactRepository.findByPersonalEmail(contactDto.getPersonalEmail()));
        if (contact.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        Contact contact1=contactMapper.dtoToModel(contactDto);
        //System.out.println(contact1);
        contact1.setClient(client);
        contact1.setCivilityTitle(civilityTitle);
        contact1.setAddress(addressRepository.save(address.setCity(city)));
        contactRepository.save(contact1);
        logService.addLog(LogType.CREATE, ClassType.CONTACT,"create contact "+contact1.getMotherFamilyName());
    }

    @Override
    public void update(ContactDto contactDto, String companyId, Address address, String cityId) {
        Client client = clientRepository.getBy_id(companyId);
        City city =cityRepository.findCityBy_id(cityId);
        CivilityTitle civilityTitle = civilityTitleRepository.findBy_id(contactDto.getCivilityId());
        // save Contact if note existe
       Optional<Contact> contact1 = Optional.ofNullable(contactRepository.findBy_id(contactDto.getContactId()));
        Optional<Contact> contact2 = Optional.ofNullable(contactRepository.findByPersonalEmail(contactDto.getPersonalEmail()));
        if (contact2.isPresent() && !(contact1.get().getPersonalEmail().equals(contactDto.getPersonalEmail())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
        Contact contact3 = contactMapper.dtoToModel(contactDto);
        contact3.setCivilityTitle(civilityTitle);
        contact3.setClient(client);
        Address address1=contact1.get().getAddress();
        address1.setCity(city);
        address1.setFullAddress(contactDto.getFullAddress());
        address1.setPostCode(contactDto.getPostCode());
        contact3.setAddress(addressRepository.save(address1));
         contactRepository.save(contact3);
        logService.addLog(LogType.UPDATE, ClassType.CONTACT,"update contact "+contact3.getMotherFamilyName());
    }

    @Override
    public List<ContactDto> getAll() {
        // Get all Contacts
        List<Contact> contacts = contactRepository.findAll();
        // Create a list of all Contacts dto
        ArrayList<ContactDto> contactDtos = new ArrayList<>();

        for (Contact contact : contacts) {
            ContactDto contactDto=contactMapper.modelToDto(contact);
            contactDto.setContactId(contact.get_id());
            contactDto.setCountryName(contact.getAddress().getCity().getStateCountry().getCountry().getCountryName());
            contactDto.setCountryId(contact.getAddress().getCity().getStateCountry().getCountry().getCountryId());
            contactDto.setStateName(contact.getAddress().getCity().getStateCountry().getStateName());
            contactDto.setCityName(contact.getAddress().getCity().getCityName());
            contactDto.setFullAddress(contact.getAddress().getFullAddress());
            contactDto.setPostCode(contact.getAddress().getPostCode());
            contactDto.setCompanyId(contact.getClient().get_id());
            if(contact.getCivilityTitle()!=null){
            contactDto.setCivilityName(contact.getCivilityTitle().getName());}
            contactDto.setCountryStateId(contact.getAddress().getCity().getStateCountry().get_id());
            contactDtos.add(contactDto);
        }
        return contactDtos;
    }
    /**
     * delete Contact
     *
     * @param id - id
     */
    @Override
    public void remove(String id) {
        Optional<Contact> action = Optional.ofNullable(contactRepository.findBy_id(id));
        // If Contact doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        contactRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.CONTACT,"update contact "+action.get().getMotherFamilyName());
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Contact, exceptionType, args);
    }
}
