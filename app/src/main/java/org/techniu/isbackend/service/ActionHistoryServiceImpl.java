package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.ActionHistoryMapper;
import org.techniu.isbackend.dto.model.ActionHistoryDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.ActionHistoryRepository;
import org.techniu.isbackend.repository.ContactRepository;

import java.util.*;

@Service
@Transactional
public class ActionHistoryServiceImpl implements ActionHistoryService {
    private ActionHistoryRepository actionHistoryRepository;
    private final ActionHistoryMapper actionHistoryMapper = Mappers.getMapper(ActionHistoryMapper.class);
    private LogService logService;
    private ContactRepository contactRepository;
    public ActionHistoryServiceImpl(ActionHistoryRepository actionHistoryRepository, LogService logService,
                                    ContactRepository contactRepository) {
        this.actionHistoryRepository = actionHistoryRepository;
        this.logService = logService;
        this.contactRepository = contactRepository;
    }

    @Override
    public void saveActionHistory(ActionHistoryDto actionHistoryDto) {
        //System.out.println("Implement part :" + actionHistoryDto);
        ArrayList<Contact> contacts = new ArrayList<>();
        for (LinkedHashMap line : actionHistoryDto.getContactsIds()){
            if (line.get("checked").toString().equals("true")) {
                contacts.add(contactRepository.findBy_id(line.get("_id").toString()));
            }
        }
        ActionHistory actionHistory = actionHistoryMapper.dtoToModel(actionHistoryDto);
        actionHistory.setContacts(contacts);
        actionHistory.setActionDate(new Date());

        logService.addLog(LogType.CREATE, ClassType.TYPEOFCURRENCY,"create history of commercial action "+actionHistoryDto.getOperationName());
        actionHistoryRepository.save(actionHistory);
    }

    @Override
    public List<ActionHistory> getAllActionHistory() {
        List<ActionHistory> actionHistory = actionHistoryRepository.findAll();
        /*
        // Create a list of all actions dto
        ArrayList<ActionHistoryDto> actionHistoryDtos = new ArrayList<>();

        for (ActionHistory actionHistory1 : actionHistory) {
            ActionHistoryDto actionHistoryDto = actionHistoryMapper.modelToDto(actionHistory1);
            actionHistoryDtos.add(actionHistoryDto);
        }
        */
        return actionHistory;
    }

    @Override
    public ActionHistory getById(String id) {
        return actionHistoryRepository.findAllBy_id(id);
    }

    @Override
    public List<ActionHistoryDto> updateActionHistory(ActionHistoryDto actionHistoryDto, String id) {
        return null;
    }

    @Override
    public List<ActionHistoryDto> remove(String id) {
        return null;
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
