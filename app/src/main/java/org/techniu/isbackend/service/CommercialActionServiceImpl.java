package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.CommercialActionMapper;
import org.techniu.isbackend.dto.model.CommercialActionDto;
import org.techniu.isbackend.entity.CommercialAction;
import org.techniu.isbackend.entity.CommercialActionType;
import org.techniu.isbackend.entity.CommercialOperation;
import org.techniu.isbackend.entity.Contact;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.CommercialActionRepository;
import org.techniu.isbackend.repository.CommercialActionTypeRepository;
import org.techniu.isbackend.repository.CommercialOperationRepository;
import org.techniu.isbackend.repository.ContactRepository;

import java.util.*;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class CommercialActionServiceImpl implements CommercialActionService {
    private CommercialActionRepository commercialActionRepository;
    private CommercialOperationRepository commercialOperationRepository;
    private CommercialActionTypeRepository commercialActionTypeRepository;
    private ContactRepository contactRepository;
    private final CommercialActionMapper commercialActionMapper = Mappers.getMapper(CommercialActionMapper.class);

    public CommercialActionServiceImpl(CommercialActionRepository commercialActionRepository,
                                       CommercialOperationRepository commercialOperationRepository,
                                       CommercialActionTypeRepository commercialActionTypeRepository,
                                       ContactRepository contactRepository) {
        this.commercialActionRepository = commercialActionRepository;
        this.commercialOperationRepository = commercialOperationRepository;
        this.commercialActionTypeRepository = commercialActionTypeRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public void saveCommercialAction(CommercialActionDto commercialActionDto) {

        CommercialActionType commercialActionType = commercialActionTypeRepository.findAllBy_id(commercialActionDto.getCommercialActionType().get_id());
        CommercialOperation commercialOperation = commercialOperationRepository.findBy_id(commercialActionDto.getCommercialOperation().get_id());

        ArrayList<Contact> contacts = new ArrayList<>();
        for (LinkedHashMap line : commercialActionDto.getContactsIds()){
            if (line.get("checked").toString().equals("true")) {
                contacts.add(contactRepository.findBy_id(line.get("_id").toString()));
            }
        }
        Date today = new Date();

        CommercialAction commercialAction = commercialActionMapper.dtoToModel(commercialActionDto);
        commercialAction.setCommercialOperation(commercialOperation);
        commercialAction.setCommercialActionType(commercialActionType);
        commercialAction.setContacts(contacts);
        commercialAction.setCreationDate(today);

        System.out.println("Service part :" + commercialAction);
        commercialActionRepository.save(commercialAction);
    }

    @Override
    public List<CommercialActionDto> getAllCommercialAction() {
        // Get all actions
        List<CommercialAction> commercialAction = commercialActionRepository.findAll();
        // Create a list of all actions dto
        ArrayList<CommercialActionDto> commercialActionDtos = new ArrayList<>();

        for (CommercialAction commercialAction1 : commercialAction) {
            CommercialActionDto commercialActionDto = commercialActionMapper.modelToDto(commercialAction1);
            commercialActionDtos.add(commercialActionDto);
        }
        return commercialActionDtos;
    }

    @Override
    public List<CommercialAction> getAllCommercialAction2() {
        // Get all actions
        List<CommercialAction> commercialActions = commercialActionRepository.findAll();
        return commercialActions;
    }

    @Override
    public CommercialAction getById(String id) {
        return commercialActionRepository.findAllBy_id(id);
    }

    @Override
    public List<CommercialActionDto> updateCommercialAction(CommercialActionDto commercialActionDto, String id) {
        // save country if note existe
        CommercialAction commercialAction = getById(id);
        Optional<CommercialAction> commercialAction1 = Optional.ofNullable(commercialActionRepository.findAllBy_id(id));

        if (!commercialAction1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        System.out.println(commercialAction);
        Date today = new Date();

        commercialAction.setModificationDate(today);
        commercialAction.setDescriptions(commercialActionDto.getDescriptions());
        commercialAction.setObjectifs(commercialActionDto.getObjectifs());
        commercialAction.setNbrActions(commercialActionDto.getNbrActions());
        commercialAction.setActionDescriptions(commercialActionDto.getActionDescriptions());
        commercialAction.setActionDates(commercialActionDto.getActionDates());
        commercialAction.setConclusions(commercialActionDto.getConclusions());
        commercialAction.setNbrConclusions(commercialActionDto.getNbrConclusions());

        CommercialActionType commercialActionType = commercialActionTypeRepository.findAllBy_id(commercialActionDto.getCommercialActionType().get_id());
        commercialAction.setCommercialActionType(commercialActionType);
        CommercialOperation commercialOperation = commercialOperationRepository.findBy_id(commercialActionDto.getCommercialOperation().get_id());
        commercialAction.setCommercialOperation(commercialOperation);

        ArrayList<Contact> contacts = new ArrayList<>();
        for (LinkedHashMap line : commercialActionDto.getContactsIds()){
            if (line.get("checked").toString().equals("true")) {
                contacts.add(contactRepository.findBy_id(line.get("_id").toString()));
            }
        }
        commercialAction.setContacts(contacts);

        System.out.println(commercialAction);

        commercialActionRepository.save(commercialAction);
        return getAllCommercialAction();
    }

    @Override
    public List<CommercialAction> remove(String id) {
        Optional<CommercialAction> action = Optional.ofNullable(commercialActionRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        commercialActionRepository.deleteById(id);
        return getAllCommercialAction2();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.ContractStatus, exceptionType, args);
    }
}
