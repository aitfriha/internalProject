package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.PersonTypeMapper;
import org.techniu.isbackend.dto.model.PersonTypeDto;
import org.techniu.isbackend.entity.PersonType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.PersonTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;
import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;


@Service
public class PersonTypeServiceImpl implements PersonTypeService {

    private PersonTypeRepository personTypeRepository;
    private final PersonTypeMapper personTypeMapper = Mappers.getMapper(PersonTypeMapper.class);

    public PersonTypeServiceImpl(PersonTypeRepository personTypeRepository) {
        this.personTypeRepository = personTypeRepository;
    }

    @Override
    public List<PersonTypeDto> getAllPersonTypes() {
        List<PersonTypeDto> result = new ArrayList<>();
        List<PersonType> list = personTypeRepository.findAll();
        list.forEach(personType -> {
            PersonTypeDto personTypeDto = personTypeMapper.modelToDto(personType);
            result.add(personTypeDto);
        });
        return result;
    }

    @Override
    public void savePersonType(PersonTypeDto personTypeDto) {
        PersonType obj = null;
        obj = personTypeRepository.findPersonTypeByCode(personTypeDto.getCode());
        if (obj != null) {
            throw exception(DUPLICATE_ENTITY);
        } else {
            obj = personTypeRepository.findPersonTypeByName(personTypeDto.getName());
            if (obj != null) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                personTypeDto.setMasterValue(personTypeDto.getName().toUpperCase());
                personTypeRepository.save(personTypeMapper.dtoToModel(personTypeDto));
            }
        }
    }

    @Override
    public void updatePersonType(PersonTypeDto personTypeDto) {
        PersonType obj = null;
        // Find person type by code
        Optional<PersonType> personType = personTypeRepository.findById(personTypeDto.getId());
        if (personType.isPresent()) {
            obj = personTypeRepository.findPersonTypeByCode(personTypeDto.getCode());
            if (obj != null && !obj.get_id().equalsIgnoreCase(personTypeDto.getId())) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                obj = personTypeRepository.findPersonTypeByName(personTypeDto.getName());
                if (obj != null && !obj.get_id().equalsIgnoreCase(personTypeDto.getId())) {
                    throw exception(DUPLICATE_ENTITY);
                } else {
                    // Get person type model
                    PersonType personTypeModel = personType.get();
                    personTypeModel.setCode(personTypeDto.getCode())
                            .setName(personTypeDto.getName())
                            .setDescription(personTypeDto.getDescription())
                            .setMasterValue(personTypeDto.getMasterValue())
                            .setRemovable(personTypeDto.isRemovable());

                    // Update person type data
                    personTypeRepository.save(personTypeModel);
                }
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void deletePersonType(String id) {
        Optional<PersonType> personType = personTypeRepository.findById(id);
        if (personType.isPresent()) {
            PersonType object = personType.get();
            personTypeRepository.delete(object);
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
        return MainException.throwException(EntityType.PersonType, exceptionType, args);
    }

}
