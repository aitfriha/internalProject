package com.wproducts.administration.service;
import com.wproducts.administration.dto.mapper.ActionMapper;
import com.wproducts.administration.dto.model.ActionDto;
import com.wproducts.administration.model.Action;
import com.wproducts.administration.model.Action;
import com.wproducts.administration.repository.ActionRepository;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;


@Service
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper = Mappers.getMapper(ActionMapper.class);

    public ActionServiceImpl(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    /**
     * Register a new Action
     *
     * @param actionDto - actionDto
     */
    @Override
    public void save(ActionDto actionDto) {
        actionDto.setActionCode(actionDto.getActionCode().toLowerCase());

        List<Action> actions = actionRepository.findByActionConcerns(actionDto.getActionConcerns());
        if (actionDto.getActionCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        for (Action action : actions) {
            if(action.getActionCode().equals(actionDto.getActionCode().toLowerCase())){
                throw exception(DUPLICATE_ENTITY);
            }
        }
        Action action1 = actionMapper.dtoToModel(actionDto)
         .setActionCreatedAt(Instant.now());
        actionRepository.save(action1);
    }
    

    @Override
    public void updateAction(ActionDto actionDto) {
        actionDto.setActionCode(actionDto.getActionCode().toLowerCase());

        if (actionDto.getActionCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        Optional<Action> action = Optional.ofNullable(actionRepository.findBy_id(actionDto.getActionId()));
        if (!action.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        Optional<Action> actionCode = Optional.ofNullable(actionRepository.findByActionCode(actionDto.getActionCode()));

        if (actionCode.isPresent() && !(action.get().getActionCode().equals(actionDto.getActionCode())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
        List<Action> actions = actionRepository.findByActionConcerns(actionDto.getActionConcerns());
        for (Action action1 : actions) {
            if(action1.getActionCode().equals(actionDto.getActionCode())){
                throw exception(DUPLICATE_ENTITY);
            }
        }
        Action action2 = actionMapper.dtoToModel(actionDto);
        action2.setActionUpdatedAt(Instant.now());
        action2.setActionCreatedAt(action.get().getActionCreatedAt());
        // save action
        actionRepository.save(action2);

    }

    /**
     * delete Action
     *
     * @param id - id
     */
    @Override
    public void removeAction(String id) {
        Optional<Action> action = Optional.ofNullable(actionRepository.findBy_id(id));
        // If action doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        actionRepository.deleteById(id);
    }

    /**
     * all ActionsDto
     *
     * @return List ActionsDto
     */
    @Override
    public List<ActionDto> getAllActions() {
        // Get all actions
        List<Action> actions = actionRepository.findAll();

        // Create a list of all actions dto
        ArrayList<ActionDto> actionDtos = new ArrayList<>();

        for (Action action : actions) {
            ActionDto actionDto=actionMapper.modelToDto(action);
            if(action.getActionUpdatedAt() !=null ) {
                actionDto.setActionUpdatedAt(action.getActionUpdatedAt().toString());
            }
            if(action.getActionCreatedAt()!=null ) {
                actionDto.setActionCreatedAt(action.getActionCreatedAt().toString());
            }
            actionDtos.add(actionDto);
        }
        return actionDtos;
    }

    /**
     * one ActionDto
     *
     * @param id - id
     * @return ActionDto
     */
    @Override
    public ActionDto getOneAction(String id) {
        Optional<Action> action = Optional.ofNullable(actionRepository.findBy_id(id));

        // If action doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        ActionDto actionDto=actionMapper.modelToDto(action.get());
        if(action.get().getActionUpdatedAt() !=null ) {
            actionDto.setActionUpdatedAt(action.get().getActionUpdatedAt().toString());
        }
        if(action.get().getActionCreatedAt()!=null ) {
            actionDto.setActionCreatedAt(action.get().getActionCreatedAt().toString());
        }
        return actionDto;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Action, exceptionType, args);
    }
}
