package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.ServiceTypeMapper;
import org.techniu.isbackend.dto.model.ServiceTypeDto;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.CommercialOperation;
import org.techniu.isbackend.entity.LogType;
import org.techniu.isbackend.entity.ServiceType;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.CommercialOperationRepository;
import org.techniu.isbackend.repository.ServiceTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class ServiceTypeImpl implements ServiceTypeService{
    private ServiceTypeRepository serviceTypeRepository;
    private CommercialOperationRepository commercialOperationRepository;
    private LogService logService;
    private final ServiceTypeMapper serviceTypeeMapper = Mappers.getMapper(ServiceTypeMapper.class);
    ServiceTypeImpl(ServiceTypeRepository serviceTypeRepository, CommercialOperationRepository commercialOperationRepository, LogService logService) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.commercialOperationRepository = commercialOperationRepository;
        this.logService = logService;
    }
    @Override
    public void save(ServiceTypeDto serviceTypeeDto) {
        // save country if note existe
        //serviceTypeeDto.setName(serviceTypeeDto.getName().toLowerCase());
        if (serviceTypeeDto.getName().contains(" ")) {
             throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        Optional<ServiceType>  serviceTypee= Optional.ofNullable(serviceTypeRepository.findByNameIgnoreCase(serviceTypeeDto.getName()));
        if (serviceTypee.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        serviceTypeRepository.save(serviceTypeeMapper.dtoToModel(serviceTypeeDto));
        logService.addLog(LogType.CREATE, ClassType.SERVICE_TYPE,"create service type "+serviceTypeeDto.getName());
    }

    @Override
    public void update(ServiceTypeDto serviceTypeeDto) {
        // save country if note existe
       // serviceTypeeDto.setName(serviceTypeeDto.getName().toLowerCase());
        if (serviceTypeeDto.getName().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
       Optional<ServiceType> serviceTypee1 = Optional.ofNullable(serviceTypeRepository.findBy_id(serviceTypeeDto.getServiceTypeId()));
        if (!serviceTypee1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        Optional<ServiceType> serviceTypee2 = Optional.ofNullable(serviceTypeRepository.findByNameIgnoreCase(serviceTypeeDto.getName()));

        if (serviceTypee2.isPresent() && (serviceTypee1.get().getName().equals(serviceTypeeDto.getName()) && !serviceTypee1.get().get_id().equals(serviceTypeeDto.getServiceTypeId())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
         serviceTypeRepository.save(serviceTypeeMapper.dtoToModel(serviceTypeeDto));
        logService.addLog(LogType.UPDATE, ClassType.SERVICE_TYPE,"update service type "+serviceTypeeDto.getName());
    }

    @Override
    public List<ServiceTypeDto> getAll() {
        // Get all actions
        List<ServiceType> serviceTypees = serviceTypeRepository.findAll();
        // Create a list of all actions dto
        ArrayList<ServiceTypeDto> serviceTypeeDtos = new ArrayList<>();

        for (ServiceType serviceTypee : serviceTypees) {
            ArrayList<String> operations = new ArrayList<>();
            List<CommercialOperation> commercialOperations=commercialOperationRepository.findByServiceType(serviceTypee);
            if(commercialOperations != null){
                int a = commercialOperations.size();
                for (CommercialOperation commercialOperation : commercialOperations) {
                    operations.add(commercialOperation.getName());
                    a=a-1;
                    if(a>0) {
                        operations.add(" , ");
                    }
                }
            }
                if(commercialOperations.isEmpty())
            {
                operations.add(" -- ");
            }
            ServiceTypeDto serviceTypeeDto=serviceTypeeMapper.modelToDto(serviceTypee);
            serviceTypeeDto.setOperationCommercial(operations);
            serviceTypeeDtos.add(serviceTypeeDto);
        }
        return serviceTypeeDtos;
    }
    /**
     * delete Action
     *
     * @param id - id
     */
    @Override
    public void remove(String id) {
        Optional<ServiceType> action = Optional.ofNullable(serviceTypeRepository.findBy_id(id));
        // If ServiceType doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }

        serviceTypeRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.SERVICE_TYPE,"delete service type "+action.get().getName());
    }

    /**
     * delete Action
     *
     * @param serviceTypes - serviceTypes
     * @param listOperation - listOperation
     */
    @Override
    public void removeUpdate(List<String> serviceTypes, List<String> listOperation) {
        List<ServiceType> serviceTypetoDeleteOld = new ArrayList<>();
        List<ServiceType> serviceTypetoDeleteNew = new ArrayList<>();
        if(!(listOperation.get(0)).equals("--")) {
            for (String operation : listOperation) {
                CommercialOperation commercialOperation = commercialOperationRepository.findByName(operation);
                ArrayList<ServiceType> serviceTypes1 = new ArrayList<>();
                for (String serviceType : serviceTypes) {
                    ServiceType serviceType1 = serviceTypeRepository.findByName(serviceType);
                    serviceTypetoDeleteNew.add(serviceType1);
                    serviceTypes1.add(serviceType1);
                }
                serviceTypetoDeleteOld = commercialOperation.getServiceType();
                commercialOperation.setServiceType(serviceTypes1);
                commercialOperationRepository.save(commercialOperation);
            }
            List<ServiceType> serviceTypetoDeleteOldx = new ArrayList<>();
            for (ServiceType sOld : serviceTypetoDeleteOld) {
                int i = 0;
                for (ServiceType sNew : serviceTypetoDeleteNew) {
                    if (sNew.equals(sOld)) {
                        //serviceTypetoDeleteOld.remove(sOld);
                    } else {
                        i = i + 1;
                    }
                }
                if (i == serviceTypetoDeleteNew.size()) {
                    serviceTypetoDeleteOldx.add(sOld);
                }
            }
            for (ServiceType serviceType : serviceTypetoDeleteOldx) {
                 serviceTypeRepository.deleteById(serviceType.get_id());
                logService.addLog(LogType.DELETE, ClassType.SERVICE_TYPE,"delete service type "+ serviceType.getName());
            }

        }
        else {
            Optional<ServiceType> serviceType = Optional.ofNullable(serviceTypeRepository.findByName(serviceTypes.get(0)));
            // If ServiceType doesn't exists
            if (!serviceType.isPresent()) {
                throw exception(ENTITY_NOT_FOUND);
            }

            serviceTypeRepository.deleteById(serviceType.get().get_id());
            logService.addLog(LogType.DELETE, ClassType.SERVICE_TYPE,"delete service type "+ serviceType.get().getName());
        }

    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.ServiceType, exceptionType, args);
    }
}
