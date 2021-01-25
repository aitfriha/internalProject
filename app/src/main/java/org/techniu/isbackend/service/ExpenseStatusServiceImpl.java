package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.ExpenseStatusMapper;
import org.techniu.isbackend.dto.model.ExpenseStatusDto;
import org.techniu.isbackend.entity.ExpenseStatus;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.ExpenseStatusRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;


@Service
public class ExpenseStatusServiceImpl implements ExpenseStatusService {

    private ExpenseStatusRepository expenseStatusRepository;
    private final ExpenseStatusMapper expenseStatusMapper = Mappers.getMapper(ExpenseStatusMapper.class);
    private ExpenseService expenseService;

    public ExpenseStatusServiceImpl(ExpenseStatusRepository expenseStatusRepository, ExpenseService expenseService) {
        this.expenseStatusRepository = expenseStatusRepository;
        this.expenseService = expenseService;
    }

    @Override
    public List<ExpenseStatusDto> getAllExpenseStatus() {
        List<ExpenseStatusDto> result = new ArrayList<>();
        List<ExpenseStatus> list = expenseStatusRepository.findAll();
        list.forEach(expenseStatus -> {
            ExpenseStatusDto expenseStatusDto = expenseStatusMapper.modelToDto(expenseStatus);
            result.add(expenseStatusDto);
        });
        return result;
    }

    @Override
    public void saveExpenseStatus(ExpenseStatusDto expenseStatusDto) {
        ExpenseStatus obj = null;
        obj = expenseStatusRepository.findExpenseStatusByCode(expenseStatusDto.getCode());
        if (obj != null) {
            throw exception(DUPLICATE_ENTITY);
        } else {
            obj = expenseStatusRepository.findExpenseStatusByName(expenseStatusDto.getName());
            if (obj != null) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                expenseStatusRepository.save(expenseStatusMapper.dtoToModel(expenseStatusDto));
            }
        }
    }

    @Override
    public void updateExpenseStatus(ExpenseStatusDto expenseStatusDto) {
        ExpenseStatus obj = null;
        // Find expense status by code
        Optional<ExpenseStatus> expenseStatus = expenseStatusRepository.findById(expenseStatusDto.getId());
        if (expenseStatus.isPresent()) {
            obj = expenseStatusRepository.findExpenseStatusByCode(expenseStatusDto.getCode());
            if (obj != null && !obj.get_id().equalsIgnoreCase(expenseStatusDto.getId())) {
                throw exception(DUPLICATE_ENTITY);
            } else {
                obj = expenseStatusRepository.findExpenseStatusByName(expenseStatusDto.getName());
                if (obj != null && !obj.get_id().equalsIgnoreCase(expenseStatusDto.getId())) {
                    throw exception(DUPLICATE_ENTITY);
                } else {
                    // Get expense status model
                    ExpenseStatus expenseStatusModel = expenseStatus.get();
                    expenseStatusModel.setCode(expenseStatusDto.getCode())
                            .setName(expenseStatusDto.getName())
                            .setDescription(expenseStatusDto.getDescription())
                            .setMasterValue(expenseStatusDto.getMasterValue())
                            .setRemovable(expenseStatusDto.isRemovable());

                    // Update expense status data
                    expenseStatusRepository.save(expenseStatusModel);
                }
            }
        } else {
            throw exception(ENTITY_NOT_FOUND);
        }
    }

    @Override
    public void deleteExpenseStatus(String id) {
        boolean exists = expenseService.existsExpensesWithStatus(id);
        if (!exists) {
            Optional<ExpenseStatus> expenseStatus = expenseStatusRepository.findById(id);
            if (expenseStatus.isPresent()) {
                ExpenseStatus object = expenseStatus.get();
                expenseStatusRepository.delete(object);
            } else {
                throw exception(ENTITY_NOT_FOUND);
            }
        }else{
            throw exception(ASSOCIATED_WITH_SOME_EXPENSE);
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
        return MainException.throwException(EntityType.ExpenseStatus, exceptionType, args);
    }

}
