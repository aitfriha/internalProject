package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.CommercialActionTypeMapper;
import org.techniu.isbackend.dto.model.CommercialActionTypeDto;
import org.techniu.isbackend.entity.CommercialActionType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.CommercialActionTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class CommercialActionTypeServiceImpl implements CommercialActionTypeService {
    private CommercialActionTypeRepository commercialActionTypeRepository;
    private final CommercialActionTypeMapper commercialActionTypeMapper = Mappers.getMapper(CommercialActionTypeMapper.class);

    public CommercialActionTypeServiceImpl(CommercialActionTypeRepository commercialActionTypeRepository) {
        this.commercialActionTypeRepository = commercialActionTypeRepository;
    }

    @Override
    public void saveCommercialActionType(CommercialActionTypeDto commercialActionTypeDto) {
        System.out.println("Implement part :" + commercialActionTypeDto);
        commercialActionTypeRepository.save(commercialActionTypeMapper.dtoToModel(commercialActionTypeDto));
    }

    @Override
    public List<CommercialActionTypeDto> getAllCommercialActionType() {
        // Get all actions
        List<CommercialActionType> commercialActionType = commercialActionTypeRepository.findAll();
        // Create a list of all actions dto
        ArrayList<CommercialActionTypeDto> commercialActionTypeDtos = new ArrayList<>();

        for (CommercialActionType commercialActionType1 : commercialActionType) {
            CommercialActionTypeDto commercialActionTypeDto = commercialActionTypeMapper.modelToDto(commercialActionType1);
            commercialActionTypeDtos.add(commercialActionTypeDto);
        }
        return commercialActionTypeDtos;
    }

    @Override
    public CommercialActionType getById(String id) {
        return commercialActionTypeRepository.findAllBy_id(id);
    }

    @Override
    public List<CommercialActionTypeDto> updateCommercialActionType(CommercialActionTypeDto commercialActionTypeDto, String id) {
        // save country if note existe
        CommercialActionType commercialActionType = getById(id);
        Optional<CommercialActionType> commercialActionType1 = Optional.ofNullable(commercialActionTypeRepository.findAllBy_id(id));

        if (!commercialActionType1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        System.out.println(commercialActionType);
        commercialActionType.setTypeName(commercialActionTypeDto.getTypeName());
        commercialActionType.setDescription(commercialActionTypeDto.getDescription());
        commercialActionType.setPercentage(commercialActionTypeDto.getPercentage());
        commercialActionTypeRepository.save(commercialActionType);
        return getAllCommercialActionType();
    }

    @Override
    public List<CommercialActionTypeDto> remove(String id) {
        Optional<CommercialActionType> action = Optional.ofNullable(commercialActionTypeRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        commercialActionTypeRepository.deleteById(id);
        return getAllCommercialActionType();
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
