package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.BusinessExpenseSubtypeMapper;
import org.techniu.isbackend.dto.mapper.BusinessExpenseTypeMapper;
import org.techniu.isbackend.dto.model.BusinessExpenseSubtypeDto;
import org.techniu.isbackend.dto.model.BusinessExpenseTypeDto;
import org.techniu.isbackend.entity.BusinessExpenseSubtype;
import org.techniu.isbackend.entity.BusinessExpenseType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.BusinessExpenseTypeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class BusinessExpensesTypesServiceImpl implements BusinessExpensesTypesService {

    @Autowired
    private BusinessExpenseTypeRepository businessExpenseTypeRepository;

    @Autowired
    private TravelRequestService travelRequestService;

    private final BusinessExpenseTypeMapper businessExpenseTypeMapper = Mappers.getMapper(BusinessExpenseTypeMapper.class);
    private final BusinessExpenseSubtypeMapper businessExpenseSubtypeMapper = Mappers.getMapper(BusinessExpenseSubtypeMapper.class);

    @Override
    public List<BusinessExpenseTypeDto> getAllTypes() {
        List<BusinessExpenseTypeDto> result = new ArrayList<>();
        List<BusinessExpenseType> list = businessExpenseTypeRepository.findAll();
        list.forEach(businessExpenseType -> {
            List<BusinessExpenseSubtypeDto> subtypes = new ArrayList<>();
            if (businessExpenseType.isAllowSubtypes() && businessExpenseType.getSubtypes() != null) {
                businessExpenseType.getSubtypes().forEach(subtype -> {
                    BusinessExpenseSubtypeDto businessExpenseSubtypeDto = businessExpenseSubtypeMapper.modelToDto(subtype);
                    businessExpenseSubtypeDto.setType(businessExpenseType.get_id());
                    businessExpenseSubtypeDto.setMasterValueType(businessExpenseType.getMasterValue());
                    subtypes.add(businessExpenseSubtypeDto);
                });
            }
            BusinessExpenseTypeDto businessExpenseTypeDto = businessExpenseTypeMapper.modelToDto(businessExpenseType);
            businessExpenseTypeDto.setSubtypes(subtypes);
            result.add(businessExpenseTypeDto);
        });
        return result;
    }

    @Override
    public void saveType(BusinessExpenseTypeDto businessExpenseTypeDto) {
        BusinessExpenseType obj = businessExpenseTypeRepository.findBusinessExpenseTypeByCode(businessExpenseTypeDto.getCode());
        if (obj != null) {
            throw exceptionBusinessExpenseType(DUPLICATE_ENTITY);
        } else {
            obj = businessExpenseTypeRepository.findBusinessExpenseTypeByName(businessExpenseTypeDto.getName());
            if (obj != null) {
                throw exceptionBusinessExpenseType(DUPLICATE_ENTITY);
            } else {
                businessExpenseTypeRepository.save(businessExpenseTypeMapper.dtoToModel(businessExpenseTypeDto));
            }
        }
    }

    @Override
    public void updateType(BusinessExpenseTypeDto businessExpenseTypeDto) {
        BusinessExpenseType obj = null;
        // Find business expenses type by id
        Optional<BusinessExpenseType> businessExpenseType = businessExpenseTypeRepository.findById(businessExpenseTypeDto.getId());
        if (businessExpenseType.isPresent()) {
            obj = businessExpenseTypeRepository.findBusinessExpenseTypeByCode(businessExpenseTypeDto.getCode());
            if (obj != null && !obj.get_id().equalsIgnoreCase(businessExpenseTypeDto.getId())) {
                throw exceptionBusinessExpenseType(DUPLICATE_ENTITY);
            } else {
                obj = businessExpenseTypeRepository.findBusinessExpenseTypeByName(businessExpenseTypeDto.getName());
                if (obj != null && !obj.get_id().equalsIgnoreCase(businessExpenseTypeDto.getId())) {
                    throw exceptionBusinessExpenseType(DUPLICATE_ENTITY);
                } else {
                    // Get business expenses type model
                    BusinessExpenseType businessExpenseTypeModel = businessExpenseType.get();
                    businessExpenseTypeModel.setCode(businessExpenseTypeDto.getCode())
                            .setName(businessExpenseTypeDto.getName())
                            .setAllowSubtypes(businessExpenseTypeDto.isAllowSubtypes())
                            .setMasterValue(businessExpenseTypeDto.getMasterValue())
                            .setRemovable(businessExpenseTypeDto.isRemovable());

                    // Update business expenses type data
                    businessExpenseTypeRepository.save(businessExpenseTypeModel);
                }
            }
        } else {
            throw exceptionBusinessExpenseType(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void deleteType(String businessExpenseTypeId) {
        Optional<BusinessExpenseType> businessExpenseType = businessExpenseTypeRepository.findById(businessExpenseTypeId);
        if (businessExpenseType.isPresent()) {
            BusinessExpenseType object = businessExpenseType.get();
            businessExpenseTypeRepository.delete(object);
        } else {
            throw exceptionBusinessExpenseType(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void saveSubtype(BusinessExpenseSubtypeDto businessExpenseSubtypeDto) {
        Optional<BusinessExpenseType> obj = businessExpenseTypeRepository.findById(businessExpenseSubtypeDto.getType());
        if (obj.isPresent()) {
            BusinessExpenseType businessExpenseType = obj.get();
            List<BusinessExpenseSubtype> subtypes = businessExpenseType.getSubtypes() != null ? businessExpenseType.getSubtypes() : new ArrayList<>();
            Optional<BusinessExpenseSubtype> codeResult = subtypes.stream().filter(businessExpenseSubtype -> businessExpenseSubtype.getCode().equalsIgnoreCase(businessExpenseSubtypeDto.getCode())).findFirst();
            if (codeResult.isPresent() && !codeResult.get().get_id().equalsIgnoreCase(businessExpenseSubtypeDto.getId())) {
                throw exceptionBusinessExpenseSubtype(DUPLICATE_ENTITY);
            } else {
                Optional<BusinessExpenseSubtype> nameResult = subtypes.stream().filter(businessExpenseSubtype -> businessExpenseSubtype.getName().equalsIgnoreCase(businessExpenseSubtypeDto.getName())).findFirst();
                if (nameResult.isPresent() && !nameResult.get().get_id().equalsIgnoreCase(businessExpenseSubtypeDto.getId())) {
                    throw exceptionBusinessExpenseSubtype(DUPLICATE_ENTITY);
                } else {
                    BusinessExpenseSubtype newSubtype = businessExpenseSubtypeMapper.dtoToModel(businessExpenseSubtypeDto);
                    subtypes.add(newSubtype);
                    businessExpenseType.setSubtypes(subtypes);
                    businessExpenseTypeRepository.save(businessExpenseType);
                }
            }
        }
    }

    @Override
    public void updateSubtype(BusinessExpenseSubtypeDto businessExpenseSubtypeDto) {
        Optional<BusinessExpenseType> obj = businessExpenseTypeRepository.findById(businessExpenseSubtypeDto.getType());
        if (obj.isPresent()) {
            BusinessExpenseType businessExpenseType = obj.get();
            List<BusinessExpenseSubtype> subtypes = businessExpenseType.getSubtypes();
            Optional<BusinessExpenseSubtype> codeResult = subtypes.stream().filter(businessExpenseSubtype -> businessExpenseSubtype.getCode().equalsIgnoreCase(businessExpenseSubtypeDto.getCode())).findFirst();
            if (codeResult.isPresent() && !codeResult.get().get_id().equalsIgnoreCase(businessExpenseSubtypeDto.getId())) {
                throw exceptionBusinessExpenseSubtype(DUPLICATE_ENTITY);
            } else {
                Optional<BusinessExpenseSubtype> nameResult = subtypes.stream().filter(businessExpenseSubtype -> businessExpenseSubtype.getName().equalsIgnoreCase(businessExpenseSubtypeDto.getName())).findFirst();
                if (nameResult.isPresent() && !nameResult.get().get_id().equalsIgnoreCase(businessExpenseSubtypeDto.getId())) {
                    throw exceptionBusinessExpenseSubtype(DUPLICATE_ENTITY);
                } else {
                    subtypes.forEach(subtype -> {
                        if (subtype.get_id().equalsIgnoreCase(businessExpenseSubtypeDto.getId())) {
                            subtype.setCode(businessExpenseSubtypeDto.getCode());
                            subtype.setName(businessExpenseSubtypeDto.getName());
                            subtype.setRequiresApproval(businessExpenseSubtypeDto.isRequiresApproval());
                        }
                    });
                    businessExpenseType.setSubtypes(subtypes);
                    businessExpenseTypeRepository.save(businessExpenseType);
                }
            }
        }
    }

    @Override
    public void deleteSubtype(HashMap data) {
        String typeId = (String) data.get("typeId");
        String subtypeId = (String) data.get("subtypeId");
        Optional<BusinessExpenseType> obj = businessExpenseTypeRepository.findById(typeId);
        if (obj.isPresent()) {
            BusinessExpenseType businessExpenseType = obj.get();
            HashMap params = new HashMap();
            params.put("type", businessExpenseType.getMasterValue());
            params.put("id", subtypeId);
            boolean exists = travelRequestService.existsTravelRequestsWithBusinessExpenseSubtype(params);
            if (!exists) {
                List<BusinessExpenseSubtype> newSubtypes = new ArrayList<>();
                businessExpenseType.getSubtypes().forEach(subtype -> {
                    if (!subtype.get_id().equalsIgnoreCase(subtypeId)) {
                        newSubtypes.add(subtype);
                    }
                });
                businessExpenseType.setSubtypes(newSubtypes);
                businessExpenseTypeRepository.save(businessExpenseType);
            } else {
                throw exceptionBusinessExpenseSubtype(ASSOCIATED_WITH_SOME_TRAVEL_REQUEST);
            }
        }
    }

    @Override
    public BusinessExpenseSubtypeDto getSubtypeBy(String masterValue, String subtypeId) {
        BusinessExpenseSubtypeDto businessExpenseSubtypeDto = null;
        BusinessExpenseType businessExpenseType = businessExpenseTypeRepository.findBusinessExpenseTypeByMasterValue(masterValue);
        BusinessExpenseSubtype businessExpenseSubtype = null;
        if (businessExpenseType != null) {
            for (BusinessExpenseSubtype subtype : businessExpenseType.getSubtypes()) {
                if (subtype.get_id().equalsIgnoreCase(subtypeId)) {
                    businessExpenseSubtype = subtype;
                    break;
                }
            }
        }
        if (businessExpenseSubtype != null) {
            businessExpenseSubtypeDto = businessExpenseSubtypeMapper.modelToDto(businessExpenseSubtype);
            businessExpenseSubtypeDto.setType(businessExpenseType.get_id());
            businessExpenseSubtypeDto.setMasterValueType(businessExpenseType.getMasterValue());
        }
        return businessExpenseSubtypeDto;
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exceptionBusinessExpenseType(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.BusinessExpenseType, exceptionType, args);
    }

    private RuntimeException exceptionBusinessExpenseSubtype(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.BusinessExpenseSubtype, exceptionType, args);
    }

}
