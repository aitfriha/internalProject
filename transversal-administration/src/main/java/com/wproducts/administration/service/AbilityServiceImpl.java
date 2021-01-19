package com.wproducts.administration.service;

import com.wproducts.administration.controller.request.AbilityAddRequest;
import com.wproducts.administration.controller.request.AbilityUpdateRequest;
import com.wproducts.administration.dto.mapper.AbilityMapper;
import com.wproducts.administration.dto.mapper.ActionMapper;
import com.wproducts.administration.dto.mapper.SubjectMapper;
import com.wproducts.administration.dto.model.AbilityDto;
import com.wproducts.administration.dto.model.ActionDto;
import com.wproducts.administration.dto.model.SubjectDto;
import com.wproducts.administration.model.Ability;
import com.wproducts.administration.model.Action;
import com.wproducts.administration.model.Subject;
import com.wproducts.administration.repository.AbilityRepository;
import com.wproducts.administration.repository.ActionRepository;
import com.wproducts.administration.repository.SubjectRepository;
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
public class AbilityServiceImpl implements AbilityService {
    private final AbilityRepository abilityRepository;
    private final ActionRepository actionRepository;
    private final SubjectRepository subjectRepository;
    private final AbilityMapper abilityMapper = Mappers.getMapper(AbilityMapper.class);
    private final ActionMapper actionMapper = Mappers.getMapper(ActionMapper.class);
    private final SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);

    private final ActionService actionService;
    private final SubjectService subjectService;



    public AbilityServiceImpl(AbilityRepository abilityRepository, ActionRepository actionRepository, SubjectRepository subjectRepository, ActionService actionService, SubjectService subjectService) {
        this.abilityRepository = abilityRepository;
        this.actionRepository = actionRepository;
        this.subjectRepository = subjectRepository;
        this.actionService = actionService;
        this.subjectService = subjectService;
    }

    /**
     * Register a new Ability
     *
     * @param abilityAddRequest - abilityAddRequest
     */
    @Override
    public void save(AbilityAddRequest abilityAddRequest) {

        ActionDto actionDto = actionService.getOneAction(abilityAddRequest.getAbilityActionId());
        SubjectDto subjectDto = subjectService.getOneSubject(abilityAddRequest.getAbilitySubjectId());
//        SubjectFieldDto subjectFieldDto = null;
//        if (abilityAddRequest.getAbilityFieldId() != null) {
//            subjectFieldDto = subjectFieldService.getOneSubjectField(abilityAddRequest.getAbilityFieldId());
//        }

        Ability ability1 = new Ability()
                .setAbilityAction(actionMapper.dtoToModel(actionDto))
                .setAbilitySubject(subjectMapper.dtoToModel(subjectDto))
//                .setAbilityField(subjectFieldMapper.dtoToModel(subjectFieldDto))
                .setAbilityCreatedAt(Instant.now());

        abilityRepository.save(ability1);
    }


    /**
     * Register a new Ability
     *
     * @param abilityAddRequest - abilityAddRequest
     */
    @Override
    public Ability saveAndReturnAbility(AbilityAddRequest abilityAddRequest) {

        ActionDto actionDto = actionService.getOneAction(abilityAddRequest.getAbilityActionId());
        SubjectDto subjectDto = subjectService.getOneSubject(abilityAddRequest.getAbilitySubjectId());

        Ability ability1 = new Ability()
                .setAbilityAction(actionMapper.dtoToModel(actionDto))
                .setAbilitySubject(subjectMapper.dtoToModel(subjectDto))
                .setAbilityValue(abilityAddRequest.getAbilityValue())
                .setAbilityCreatedAt(Instant.now());

        return abilityRepository.save(ability1);
    }

    /**
     * Update Ability
     *
     * @param abilityUpdateRequest - abilityUpdateRequest
     */
    @Override
    public void updateAbility(AbilityUpdateRequest abilityUpdateRequest) {
        Optional<Ability> ability = Optional.ofNullable(abilityRepository.findAbilityBy_id(abilityUpdateRequest.getAbilityId()));
        Action action = actionRepository.findBy_id(abilityUpdateRequest.getAbilityActionId());
        Subject subject = subjectRepository.findSubjectBy_id(abilityUpdateRequest.getAbilitySubjectId());
//        SubjectField subjectField = subjectFieldtRepository.findBy_id(abilityUpdateRequest.getAbilityFieldId());
        ability.get().setAbilityUpdatedAt(Instant.now());
        ability.get().setAbilityCreatedAt(ability.get().getAbilityCreatedAt());
        ability.get().setAbilityAction(action);
        ability.get().setAbilitySubject(subject);
//        ability.get().setAbilityField(subjectField);
        // Update ability in database
        abilityRepository.save(ability.get());
    }

    /**
     * delete Ability
     *
     * @param id - id
     */
    @Override
    public void removeAbility(String id) {
        Optional<Ability> ability = Optional.ofNullable(abilityRepository.findAbilityBy_id(id));
        // If ability doesn't exists
        if (!ability.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        // Delete ability
        abilityRepository.deleteById(id);
    }

    /**
     * all AbilitiesDto
     *
     * @return List AbilitiesDto
     */
    @Override
    public List<AbilityDto> getAllAbilities() {
        // Get all abilities
        List<Ability> abilities = abilityRepository.findAll();
        // Create a list of all abilities dto
        ArrayList<AbilityDto> abilityDtos = new ArrayList<>();
        for (Ability ability : abilities) {
            AbilityDto abilityDto = abilityMapper.modelToDto(ability);
            if (ability.getAbilityUpdatedAt() != null) {
                abilityDto.setAbilityUpdatedAt(ability.getAbilityUpdatedAt().toString());
            }
            if (ability.getAbilityCreatedAt() != null) {
                abilityDto.setAbilityCreatedAt(ability.getAbilityCreatedAt().toString());
            }
            abilityDto.setAbilityAction(actionMapper.modelToDto(ability.getAbilityAction()));
            abilityDto.setAbilitySubject(subjectMapper.modelToDto(ability.getAbilitySubject()));

            abilityDtos.add(abilityDto);
        }
        return abilityDtos;
    }

    /**
     * one AbilityDto
     *
     * @param id - id
     * @return AbilityDto
     */
    @Override
    public AbilityDto getOneAbility(String id) {
        Optional<Ability> ability = Optional.ofNullable(abilityRepository.findAbilityBy_id(id));

        // If ability doesn't exists
        if (!ability.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        AbilityDto abilityDto = abilityMapper.modelToDto(ability.get());
        if (ability.get().getAbilityUpdatedAt() != null) {
            abilityDto.setAbilityUpdatedAt(ability.get().getAbilityUpdatedAt().toString());
        }
        if (ability.get().getAbilityCreatedAt() != null) {
            abilityDto.setAbilityCreatedAt(ability.get().getAbilityCreatedAt().toString());
        }
        abilityDto.setAbilityAction(actionMapper.modelToDto(ability.get().getAbilityAction()));
        abilityDto.setAbilitySubject(subjectMapper.modelToDto(ability.get().getAbilitySubject()));

        return abilityDto;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Ability, exceptionType, args);
    }
}
