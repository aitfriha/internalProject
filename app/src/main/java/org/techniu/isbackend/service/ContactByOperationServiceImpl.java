package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.ContactByOperationMapper;
import org.techniu.isbackend.dto.model.ContactByOperationDto;
import org.techniu.isbackend.entity.CommercialOperation;
import org.techniu.isbackend.entity.CommercialOperationStatus;
import org.techniu.isbackend.entity.ContactByOperation;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.CommercialOperationStatusRepository;
import org.techniu.isbackend.repository.ContactByOperationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class ContactByOperationServiceImpl implements ContactByOperationService{
    private ContactByOperationRepository contactByOperationRepository;
    private CommercialOperationStatusRepository commercialOperationStatusRepository;
    private final ContactByOperationMapper contactByOperationMapper = Mappers.getMapper(ContactByOperationMapper.class);
    ContactByOperationServiceImpl(ContactByOperationRepository contactByOperationRepository, CommercialOperationStatusRepository commercialOperationStatusRepository) {
        this.contactByOperationRepository = contactByOperationRepository;
        this.commercialOperationStatusRepository = commercialOperationStatusRepository;
    }
    @Override
    public void save(ContactByOperationDto contactByOperationDto) {
        Optional<CommercialOperationStatus>  commercialOperationStatus= Optional.ofNullable(commercialOperationStatusRepository.findBy_id(contactByOperationDto.getStatusId()));
       /* if (!commercialOperationStatus.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }*/
        ContactByOperation contactByOperation1 =contactByOperationMapper.dtoToModel(contactByOperationDto);
        contactByOperation1.setStatus(commercialOperationStatus.get());
        contactByOperationRepository.save(contactByOperation1);
    }
    /*
    @Override
    public void save(ContactByOperationDto contactByOperationDto) {
        Optional<CommercialOperationStatus>  commercialOperationStatus= Optional.ofNullable(commercialOperationStatusRepository.findBy_id(contactByOperationDto.getStatusId()));
        if (!commercialOperationStatus.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        ContactByOperation contactByOperation1 =contactByOperationMapper.dtoToModel(contactByOperationDto);
        contactByOperation1.setStatus(commercialOperationStatus.get());
        contactByOperationRepository.save(contactByOperation1);
    }*/

    @Override
    public void update(ContactByOperationDto contactByOperationDto) {
       Optional<ContactByOperation> contactByOperation1 = Optional.ofNullable(contactByOperationRepository.findBy_id(contactByOperationDto.getContactByOperationId()));
        if (!contactByOperation1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

         contactByOperationRepository.save(contactByOperation1.get().setMandatoryAttributes(contactByOperationDto.getMandatoryAttributes()));
    }

    @Override
    public List<ContactByOperationDto> getAll() {
        // Get all actions
        List<ContactByOperation> contactByOperations = contactByOperationRepository.findAll();
        // Create a list of all actions dto
        ArrayList<ContactByOperationDto> contactByOperationDtos = new ArrayList<>();
        for (ContactByOperation contactByOperation : contactByOperations) {
            ContactByOperationDto contactByOperationDto=contactByOperationMapper.modelToDto(contactByOperation);
            contactByOperationDto.setStatusName(contactByOperation.getStatus().getName());
            contactByOperationDtos.add(contactByOperationDto);
        }
        return contactByOperationDtos;
    }
    /**
     * delete ContactByOperation
     *
     * @param statusId - statusId
     * @param contactTypeName - contactTypeName
     */
    @Override
    public void remove(String statusId, String contactTypeName) {
        Optional<ContactByOperation> contactByOperation = Optional.ofNullable(contactByOperationRepository.findBy_idAndContactsType(statusId,contactTypeName));
        // If ContactByOperation doesn't exists
        if (!contactByOperation.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        contactByOperationRepository.delete(contactByOperation.get());
    }

    @Override
    public List<ContactByOperationDto> getContactByOperationById(String id) {
        // Get all action
        CommercialOperationStatus commercialOperationStatus= commercialOperationStatusRepository.findBy_id(id);
        List<ContactByOperation> contactByOperations = contactByOperationRepository.findByStatus(commercialOperationStatus);
        ArrayList<ContactByOperationDto> contactByOperationDtos = new ArrayList<>();
        ContactByOperationDto contactByOperationDto;
        for (ContactByOperation contactByOperation : contactByOperations) {
             contactByOperationDto=contactByOperationMapper.modelToDto(contactByOperation).setStatusName(contactByOperation.getStatus().getName());
            contactByOperationDtos.add(contactByOperationDto);
        }
        return contactByOperationDtos;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.ContactByOperation, exceptionType, args);
    }
}
