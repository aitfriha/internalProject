package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.StaffExpenseSubtypeMapper;
import org.techniu.isbackend.dto.mapper.StaffExpenseTypeMapper;
import org.techniu.isbackend.dto.model.StaffExpenseSubtypeDto;
import org.techniu.isbackend.dto.model.StaffExpenseTypeDto;
import org.techniu.isbackend.entity.StaffExpenseSubtype;
import org.techniu.isbackend.entity.StaffExpenseType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.StaffExpenseTypeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;
import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
public class StaffExpensesTypesServiceImpl implements StaffExpensesTypesService {

    @Autowired
    private StaffExpenseTypeRepository staffExpenseTypeRepository;
    private final StaffExpenseTypeMapper staffExpenseTypeMapper = Mappers.getMapper(StaffExpenseTypeMapper.class);
    private final StaffExpenseSubtypeMapper staffExpenseSubtypeMapper = Mappers.getMapper(StaffExpenseSubtypeMapper.class);

    public StaffExpensesTypesServiceImpl(StaffExpenseTypeRepository staffExpenseTypeRepository) {
        this.staffExpenseTypeRepository = staffExpenseTypeRepository;
    }

    @Override
    public List<StaffExpenseTypeDto> getAllTypes() {
        List<StaffExpenseTypeDto> result = new ArrayList<>();
        List<StaffExpenseType> list = staffExpenseTypeRepository.findAll();
        list.forEach(staffExpenseType -> {
            List<StaffExpenseSubtypeDto> subtypes = new ArrayList<>();
            if (staffExpenseType.isAllowSubtypes() && staffExpenseType.getSubtypes() != null) {
                staffExpenseType.getSubtypes().forEach(subtype -> {
                    StaffExpenseSubtypeDto staffExpenseSubtypeDto = staffExpenseSubtypeMapper.modelToDto(subtype);
                    staffExpenseSubtypeDto.setType(staffExpenseType.get_id());
                    staffExpenseSubtypeDto.setMasterValueType(staffExpenseType.getMasterValue());
                    subtypes.add(staffExpenseSubtypeDto);
                });
            }
            StaffExpenseTypeDto staffExpenseTypeDto = staffExpenseTypeMapper.modelToDto(staffExpenseType);
            staffExpenseTypeDto.setSubtypes(subtypes);
            result.add(staffExpenseTypeDto);
        });
        return result;
    }

    @Override
    public void saveType(StaffExpenseTypeDto staffExpenseTypeDto) {
        StaffExpenseType obj = staffExpenseTypeRepository.findStaffExpenseTypeByCode(staffExpenseTypeDto.getCode());
        if (obj != null) {
            throw exceptionStaffExpenseType(DUPLICATE_ENTITY);
        } else {
            obj = staffExpenseTypeRepository.findStaffExpenseTypeByName(staffExpenseTypeDto.getName());
            if (obj != null) {
                throw exceptionStaffExpenseType(DUPLICATE_ENTITY);
            } else {
                staffExpenseTypeRepository.save(staffExpenseTypeMapper.dtoToModel(staffExpenseTypeDto));
            }
        }
    }

    @Override
    public void updateType(StaffExpenseTypeDto staffExpenseTypeDto) {
        StaffExpenseType obj = null;
        // Find staff expenses type by code
        Optional<StaffExpenseType> staffExpenseType = staffExpenseTypeRepository.findById(staffExpenseTypeDto.getId());
        if (staffExpenseType.isPresent()) {
            obj = staffExpenseTypeRepository.findStaffExpenseTypeByCode(staffExpenseTypeDto.getCode());
            if (obj != null && !obj.get_id().equalsIgnoreCase(staffExpenseTypeDto.getId())) {
                throw exceptionStaffExpenseType(DUPLICATE_ENTITY);
            } else {
                obj = staffExpenseTypeRepository.findStaffExpenseTypeByName(staffExpenseTypeDto.getName());
                if (obj != null && !obj.get_id().equalsIgnoreCase(staffExpenseTypeDto.getId())) {
                    throw exceptionStaffExpenseType(DUPLICATE_ENTITY);
                } else {
                    // Get staff expenses type model
                    StaffExpenseType staffExpenseTypeModel = staffExpenseType.get();
                    staffExpenseTypeModel.setCode(staffExpenseTypeDto.getCode())
                            .setName(staffExpenseTypeDto.getName())
                            .setAllowSubtypes(staffExpenseTypeDto.isAllowSubtypes())
                            .setMasterValue(staffExpenseTypeDto.getMasterValue())
                            .setRemovable(staffExpenseTypeDto.isRemovable());

                    // Update staff expenses type data
                    staffExpenseTypeRepository.save(staffExpenseTypeModel);
                }
            }
        } else {
            throw exceptionStaffExpenseType(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void deleteType(String staffExpenseTypeId) {
        Optional<StaffExpenseType> staffExpenseType = staffExpenseTypeRepository.findById(staffExpenseTypeId);
        if (staffExpenseType.isPresent()) {
            StaffExpenseType object = staffExpenseType.get();
            staffExpenseTypeRepository.delete(object);
        } else {
            throw exceptionStaffExpenseType(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void saveSubtype(StaffExpenseSubtypeDto staffExpenseSubtypeDto) {
        Optional<StaffExpenseType> obj = staffExpenseTypeRepository.findById(staffExpenseSubtypeDto.getType());
        if (obj.isPresent()) {
            StaffExpenseType staffExpenseType = obj.get();
            List<StaffExpenseSubtype> subtypes = staffExpenseType.getSubtypes() != null ? staffExpenseType.getSubtypes() : new ArrayList<>();
            Optional<StaffExpenseSubtype> codeResult = subtypes.stream().filter(staffExpenseSubtype -> staffExpenseSubtype.getCode().equalsIgnoreCase(staffExpenseSubtypeDto.getCode())).findFirst();
            if (codeResult.isPresent() && !codeResult.get().get_id().equalsIgnoreCase(staffExpenseSubtypeDto.getId())) {
                throw exceptionStaffExpenseSubtype(DUPLICATE_ENTITY);
            } else {
                Optional<StaffExpenseSubtype> nameResult = subtypes.stream().filter(staffExpenseSubtype -> staffExpenseSubtype.getName().equalsIgnoreCase(staffExpenseSubtypeDto.getName())).findFirst();
                if (nameResult.isPresent() && !nameResult.get().get_id().equalsIgnoreCase(staffExpenseSubtypeDto.getId())) {
                    throw exceptionStaffExpenseSubtype(DUPLICATE_ENTITY);
                } else {
                    StaffExpenseSubtype newSubtype = staffExpenseSubtypeMapper.dtoToModel(staffExpenseSubtypeDto);
                    subtypes.add(newSubtype);
                    staffExpenseType.setSubtypes(subtypes);
                    staffExpenseTypeRepository.save(staffExpenseType);
                }
            }
        }
    }

    @Override
    public void updateSubtype(StaffExpenseSubtypeDto staffExpenseSubtypeDto) {
        Optional<StaffExpenseType> obj = staffExpenseTypeRepository.findById(staffExpenseSubtypeDto.getType());
        if (obj.isPresent()) {
            StaffExpenseType staffExpenseType = obj.get();
            List<StaffExpenseSubtype> subtypes = staffExpenseType.getSubtypes();
            Optional<StaffExpenseSubtype> codeResult = subtypes.stream().filter(staffExpenseSubtype -> staffExpenseSubtype.getCode().equalsIgnoreCase(staffExpenseSubtypeDto.getCode())).findFirst();
            if (codeResult.isPresent() && !codeResult.get().get_id().equalsIgnoreCase(staffExpenseSubtypeDto.getId())) {
                throw exceptionStaffExpenseSubtype(DUPLICATE_ENTITY);
            } else {
                Optional<StaffExpenseSubtype> nameResult = subtypes.stream().filter(staffExpenseSubtype -> staffExpenseSubtype.getName().equalsIgnoreCase(staffExpenseSubtypeDto.getName())).findFirst();
                if (nameResult.isPresent() && !nameResult.get().get_id().equalsIgnoreCase(staffExpenseSubtypeDto.getId())) {
                    throw exceptionStaffExpenseSubtype(DUPLICATE_ENTITY);
                } else {
                    subtypes.forEach(subtype -> {
                        if (subtype.get_id().equalsIgnoreCase(staffExpenseSubtypeDto.getId())) {
                            subtype.setCode(staffExpenseSubtypeDto.getCode());
                            subtype.setName(staffExpenseSubtypeDto.getName());
                            subtype.setRequirement(staffExpenseSubtypeDto.getRequirement());
                            subtype.setValidate(staffExpenseSubtypeDto.isValidate());
                        }
                    });
                    staffExpenseType.setSubtypes(subtypes);
                    staffExpenseTypeRepository.save(staffExpenseType);
                }
            }
        }
    }

    @Override
    public void deleteSubtype(HashMap data) {
        String typeId = (String) data.get("typeId");
        String subtypeId = (String) data.get("subtypeId");
        Optional<StaffExpenseType> obj = staffExpenseTypeRepository.findById(typeId);
        if (obj.isPresent()) {
            StaffExpenseType staffExpenseType = obj.get();
            List<StaffExpenseSubtype> newSubtypes = new ArrayList<>();
            staffExpenseType.getSubtypes().forEach(subtype -> {
                if (!subtype.get_id().equalsIgnoreCase(subtypeId)) {
                    newSubtypes.add(subtype);
                }
            });
            staffExpenseType.setSubtypes(newSubtypes);
            staffExpenseTypeRepository.save(staffExpenseType);
        }
    }

    @Override
    public StaffExpenseSubtypeDto getSubtypeBy(String masterValue, String subtypeId) {
        StaffExpenseSubtypeDto staffExpenseSubtypeDto = null;
        StaffExpenseType staffExpenseType = staffExpenseTypeRepository.findStaffExpenseTypeByMasterValue(masterValue);
        StaffExpenseSubtype staffExpenseSubtype = null;
        if (staffExpenseType != null) {
            for (StaffExpenseSubtype subtype : staffExpenseType.getSubtypes()) {
                if (subtype.get_id().equalsIgnoreCase(subtypeId)) {
                    staffExpenseSubtype = subtype;
                    break;
                }
            }
        }
        if (staffExpenseSubtype != null) {
            staffExpenseSubtypeDto = staffExpenseSubtypeMapper.modelToDto(staffExpenseSubtype);
            staffExpenseSubtypeDto.setType(staffExpenseType.get_id());
            staffExpenseSubtypeDto.setMasterValueType(staffExpenseType.getMasterValue());
        }
        return staffExpenseSubtypeDto;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exceptionStaffExpenseType(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.StaffExpenseType, exceptionType, args);
    }

    private RuntimeException exceptionStaffExpenseSubtype(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.StaffExpenseSubtype, exceptionType, args);
    }

}
