package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.PurchaseOrderAcceptanceMapper;
import org.techniu.isbackend.dto.model.PurchaseOrderAcceptanceDto;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.LogType;
import org.techniu.isbackend.entity.PurchaseOrderAcceptance;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.PurchaseOrderAcceptanceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
@Transactional
public class PurchaseOrderAcceptanceServiceImpl implements PurchaseOrderAcceptanceService {
    private PurchaseOrderAcceptanceRepository purchaseOrderAcceptanceRepository;
    private final PurchaseOrderAcceptanceMapper purchaseOrderAcceptanceMapper = Mappers.getMapper(PurchaseOrderAcceptanceMapper.class);
    private LogService logService;
    public PurchaseOrderAcceptanceServiceImpl(PurchaseOrderAcceptanceRepository purchaseOrderAcceptanceRepository, LogService logService) {
        this.purchaseOrderAcceptanceRepository = purchaseOrderAcceptanceRepository;
        this.logService = logService;
    }

    @Override
    public void savePurchaseOrderAcceptance(PurchaseOrderAcceptanceDto purchaseOrderAcceptanceDto) {
       // System.out.println("Implement part :" + purchaseOrderAcceptanceDto);
        purchaseOrderAcceptanceRepository.save(purchaseOrderAcceptanceMapper.dtoToModel(purchaseOrderAcceptanceDto));
        logService.addLog(LogType.CREATE, ClassType.PURCHASEORDERACCEPTANCE,"create purchase order acceptance "+purchaseOrderAcceptanceDto.getGeneratedPurchase());
    }

    @Override
    public List<PurchaseOrderAcceptanceDto> getAllPurchaseOrderAcceptance() {
        // Get all actions
        List<PurchaseOrderAcceptance> purchaseOrderAcceptance = purchaseOrderAcceptanceRepository.findAll();
        // Create a list of all actions dto
        ArrayList<PurchaseOrderAcceptanceDto> purchaseOrderAcceptanceDtos = new ArrayList<>();

        for (PurchaseOrderAcceptance purchaseOrderAcceptance1 : purchaseOrderAcceptance) {
            PurchaseOrderAcceptanceDto purchaseOrderAcceptanceDto = purchaseOrderAcceptanceMapper.modelToDto(purchaseOrderAcceptance1);
            purchaseOrderAcceptanceDtos.add(purchaseOrderAcceptanceDto);
        }
        return purchaseOrderAcceptanceDtos;
    }

    @Override
    public PurchaseOrderAcceptance getById(String id) {
        return purchaseOrderAcceptanceRepository.findAllBy_id(id);
    }

    @Override
    public List<PurchaseOrderAcceptanceDto> updatePurchaseOrderAcceptance(PurchaseOrderAcceptanceDto purchaseOrderAcceptanceDto, String id) {
        // save country if note existe
        PurchaseOrderAcceptance purchaseOrderAcceptance = getById(id);
        Optional<PurchaseOrderAcceptance> purchaseOrderAcceptance1 = Optional.ofNullable(purchaseOrderAcceptanceRepository.findAllBy_id(id));

        if (!purchaseOrderAcceptance1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }

        System.out.println(purchaseOrderAcceptance);

        purchaseOrderAcceptance.setAdminAcceptance(purchaseOrderAcceptanceDto.getAdminAcceptance());
        purchaseOrderAcceptance.setGeneratedPurchase(purchaseOrderAcceptanceDto.getGeneratedPurchase());
        purchaseOrderAcceptance.setOperationalAcceptance(purchaseOrderAcceptanceDto.getOperationalAcceptance());

        System.out.println(purchaseOrderAcceptance);

        purchaseOrderAcceptanceRepository.save(purchaseOrderAcceptance);
        logService.addLog(LogType.UPDATE, ClassType.PURCHASEORDERACCEPTANCE,"update purchase order acceptance "+purchaseOrderAcceptanceDto.getGeneratedPurchase());
        return getAllPurchaseOrderAcceptance();
    }

    @Override
    public List<PurchaseOrderAcceptanceDto> remove(String id) {
        Optional<PurchaseOrderAcceptance> action = Optional.ofNullable(purchaseOrderAcceptanceRepository.findAllBy_id(id));
        // If ContractStatus doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        purchaseOrderAcceptanceRepository.deleteById(id);
        logService.addLog(LogType.DELETE, ClassType.PURCHASEORDERACCEPTANCE,"delete purchase order acceptance "+action.get().getGeneratedPurchase());
        return getAllPurchaseOrderAcceptance();
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.PurchaseOrderAcceptance, exceptionType, args);
    }
}
