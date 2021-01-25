package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.AssignmentTypeMapper;
import org.techniu.isbackend.dto.model.AssignmentTypeDto;
import org.techniu.isbackend.entity.AssignmentType;
import org.techniu.isbackend.entity.WeeklyWork;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.AssignmentTypeRepository;
import org.techniu.isbackend.repository.WeeklyWorkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class AssignmentTypeServiceImpl implements AssignmentTypeService {

    private AssignmentTypeRepository assignmentTypeRepository;
    private WeeklyWorkRepository weeklyWorkRepository;
    private final AssignmentTypeMapper assignmentTypeMapper = Mappers.getMapper(AssignmentTypeMapper.class);


    public AssignmentTypeServiceImpl(AssignmentTypeRepository assignmentTypeRepository,
                                     WeeklyWorkRepository weeklyWorkRepository) {
        this.assignmentTypeRepository = assignmentTypeRepository;
        this.weeklyWorkRepository = weeklyWorkRepository;
    }

    @Override
    public List<AssignmentTypeDto> getAllAssignmentTypes() {
        // Get all assignment types
        List<AssignmentType> list = assignmentTypeRepository.findAll();
        // Create a list of all assignment types dto
        ArrayList<AssignmentTypeDto> listDto = new ArrayList<>();
        list.forEach(obj -> {
            AssignmentTypeDto assignmentTypeDto = assignmentTypeMapper.modelToDto(obj);
            listDto.add(assignmentTypeDto);
        });
        return listDto;
    }

    @Override
    public void saveAssignmentType(AssignmentTypeDto assignmentTypeDto) {
        AssignmentType obj = null;
        obj = assignmentTypeRepository.findAssignmentTypeByCode(assignmentTypeDto.getCode());
        if (obj != null) {
            throw exception(DUPLICATE_ENTITY);
        } else {
            obj = assignmentTypeRepository.findAssignmentTypeByName(assignmentTypeDto.getName());
            if (obj != null) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                assignmentTypeRepository.save(assignmentTypeMapper.dtoToModel(assignmentTypeDto));
            }
        }
    }

    @Override
    public void updateAssignmentType(AssignmentTypeDto assignmentTypeDto) {
        AssignmentType obj = null;
        // Find assignment type by code
        Optional<AssignmentType> assignmentType = Optional.ofNullable(assignmentTypeRepository.findAssignmentTypeBy_id(assignmentTypeDto.getId()));
        if (assignmentType.isPresent()) {
            obj = assignmentTypeRepository.findAssignmentTypeByCode(assignmentTypeDto.getCode());
            if (obj != null && !obj.get_id().equalsIgnoreCase(assignmentTypeDto.getId())) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                obj = assignmentTypeRepository.findAssignmentTypeByName(assignmentTypeDto.getName());
                if (obj != null && !obj.get_id().equalsIgnoreCase(assignmentTypeDto.getId())) {
                    throw exception(DUPLICATE_ENTITY);
                } else {
                    // Get assignment type model
                    AssignmentType assignmentTypeModel = assignmentType.get();
                    assignmentTypeModel.setCode(assignmentTypeDto.getCode())
                            .setName(assignmentTypeDto.getName())
                            .setDescription(assignmentTypeDto.getDescription());

                    // Update assignment type data
                    assignmentTypeRepository.save(assignmentTypeModel);
                }
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void deleteAssignmentType(String id) {
        Optional<AssignmentType> assignmentType = Optional.ofNullable(assignmentTypeRepository.findAssignmentTypeBy_id(id));
        if (assignmentType.isPresent()) {
            AssignmentType object = assignmentType.get();
            ArrayList<WeeklyWork> associated = weeklyWorkRepository.findAllByAssignmentType(object);
            if (associated.size() == 0) {
                assignmentTypeRepository.delete(object);
            }else{
                throw exception(ASSOCIATED_WITH_SOME_WEEKLY_WORK);
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }




    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.AssignmentType, exceptionType, args);
    }

}
