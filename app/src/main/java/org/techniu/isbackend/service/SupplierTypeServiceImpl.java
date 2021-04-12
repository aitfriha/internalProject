package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.SupplierTypeMapper;
import org.techniu.isbackend.dto.model.SupplierTypeDto;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.LogType;
import org.techniu.isbackend.entity.SupplierType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.SupplierTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class SupplierTypeServiceImpl implements SupplierTypeService {
    private SupplierTypeRepository supplierTypeRepository;
    private LogService logService;
    private final SupplierTypeMapper supplierTypeMapper = Mappers.getMapper(SupplierTypeMapper.class);

    public SupplierTypeServiceImpl(SupplierTypeRepository supplierTypeRepository, LogService logService) {
        this.supplierTypeRepository = supplierTypeRepository;
        this.logService = logService;
    }

    @Override
    public void saveSupplierType(SupplierTypeDto supplierTypeDto) {
        supplierTypeRepository.save(supplierTypeMapper.dtoToModel(supplierTypeDto));
        logService.addLog(LogType.CREATE, ClassType.SUPPLIERTYPE,"create supplier type "+supplierTypeDto.getName());
    }

    @Override
    public List<SupplierTypeDto> getAllSupplierType() {
        // Get all actions
        List<SupplierType> supplierType = supplierTypeRepository.findAll();
        // Create a list of all actions dto
        ArrayList<SupplierTypeDto> supplierTypeDtos = new ArrayList<>();
        for (SupplierType supplierType1 : supplierType) {
            SupplierTypeDto supplierTypeDto = supplierTypeMapper.modelToDto(supplierType1);
            supplierTypeDtos.add(supplierTypeDto);
        }
        return supplierTypeDtos;
    }

    @Override
    public SupplierType getById(String id) {
        return supplierTypeRepository.findAllBy_id(id);
    }

    @Override
    public List<SupplierTypeDto> updateSupplierType(SupplierTypeDto supplierTypeDto, String id) {
        // save country if note existe
        SupplierType supplierType = getById(id);
        Optional<SupplierType> supplierType1 = Optional.ofNullable(supplierTypeRepository.findAllBy_id(id));
        if (!supplierType1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        supplierType.setName(supplierTypeDto.getName());
        supplierType.setDescription(supplierTypeDto.getDescription());
        supplierType.setInternalOrder(supplierTypeDto.isInternalOrder());
        supplierType.setOperationAssociated(supplierTypeDto.isOperationAssociated());
        supplierTypeRepository.save(supplierType);
        logService.addLog(LogType.UPDATE, ClassType.SUPPLIERTYPE,"update supplier type "+supplierType1.get().getName());
        return getAllSupplierType();
    }

    @Override
    public List<SupplierTypeDto> remove(String id) {
        Optional<SupplierType> action = Optional.ofNullable(supplierTypeRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        supplierTypeRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.SUPPLIERTYPE,"delete supplier type "+action.get().getName());
        return getAllSupplierType();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.SupplierType, exceptionType, args);
    }
}
