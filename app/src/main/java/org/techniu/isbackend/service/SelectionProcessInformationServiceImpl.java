package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.SelectionProcessInformationMapper;
import org.techniu.isbackend.dto.model.SelectionProcessInformationDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
@Transactional
public class SelectionProcessInformationServiceImpl implements SelectionProcessInformationService {

    private SelectionProcessInformationRepository selectionProcessInformationRepository;
    private SelectionTypeEvaluationRepository selectionTypeEvaluationRepository;
    private CurrencyRepository currencyRepository;
    private final SelectionProcessInformationMapper selectionProcessInformationMapper = Mappers.getMapper(SelectionProcessInformationMapper.class);

    SelectionProcessInformationServiceImpl(SelectionProcessInformationRepository selectionProcessInformationRepository,
                                           SelectionTypeEvaluationRepository selectionTypeEvaluationRepository,
                                           CurrencyRepository currencyRepository) {
        this.selectionProcessInformationRepository = selectionProcessInformationRepository;
        this.selectionTypeEvaluationRepository = selectionTypeEvaluationRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void save(SelectionProcessInformationDto selectionProcessInformationDto, List<String> knowledgeIdList) {
        SelectionProcessInformation selectionProcessInformation = selectionProcessInformationMapper.dtoToModel(selectionProcessInformationDto);
        Currency currency = currencyRepository.findById(selectionProcessInformationDto.getCurrencyId()).get();
        List<SelectionTypeEvaluation> knowledge = new ArrayList<>();
        for (String id : knowledgeIdList) {
            knowledge.add(selectionTypeEvaluationRepository.findById(id).get());
        }

        selectionProcessInformation.setKnowledge(knowledge);
        selectionProcessInformation.setCurrency(currency);

        selectionProcessInformationRepository.save(selectionProcessInformation);
    }

    @Override
    public void update(SelectionProcessInformationDto selectionProcessInformationDto, List<String> knowledgeIdList) {
        SelectionProcessInformation selectionProcessInformation = selectionProcessInformationMapper.dtoToModel(selectionProcessInformationDto);
        Currency currency = currencyRepository.findById(selectionProcessInformationDto.getCurrencyId()).get();
        List<SelectionTypeEvaluation> knowledge = new ArrayList<>();
        for (String id : knowledgeIdList) {
            knowledge.add(selectionTypeEvaluationRepository.findById(id).get());
        }

        selectionProcessInformation.setKnowledge(knowledge);
        selectionProcessInformation.setCurrency(currency);

        selectionProcessInformationRepository.save(selectionProcessInformation);
    }

    @Override
    public void remove(String id) {

        Optional<SelectionProcessInformation> action = selectionProcessInformationRepository.findById(id);
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        SelectionProcessInformation selectionProcessInformation = action.get();
        selectionProcessInformationRepository.delete(selectionProcessInformation);
    }

    @Override
    public List<SelectionProcessInformationDto> getAll() {

        List<SelectionProcessInformation> selectionProcessInformations = selectionProcessInformationRepository.findAll();
        // Create a list of all actions dto
        List<SelectionProcessInformationDto> selectionProcessInformationDtos = new ArrayList<>();

        for (SelectionProcessInformation selectionProcessInformation : selectionProcessInformations) {

            selectionProcessInformationDtos.add(selectionProcessInformationToSelectionProcessInformationDto(selectionProcessInformation));
        }
        return selectionProcessInformationDtos;
    }

    public SelectionProcessInformationDto selectionProcessInformationToSelectionProcessInformationDto(SelectionProcessInformation selectionProcessInformation) {
        SelectionProcessInformationDto selectionProcessInformationDto = selectionProcessInformationMapper.modelToDto(selectionProcessInformation);
        selectionProcessInformationDto.setKnowledge(selectionProcessInformation.getKnowledge());
        selectionProcessInformationDto.setCurrencyId(selectionProcessInformation.getCurrency().get_id());
        selectionProcessInformationDto.setCurrencyCode(selectionProcessInformation.getCurrency().getCurrencyCode());
        selectionProcessInformationDto.setCurrencyName(selectionProcessInformation.getCurrency().getCurrencyName());
        selectionProcessInformationDto.setCurrencyMonth(selectionProcessInformation.getCurrency().getMonth());
        selectionProcessInformationDto.setCurrencyYear(selectionProcessInformation.getCurrency().getYear());
        selectionProcessInformationDto.setChangeFactor(selectionProcessInformation.getCurrency().getChangeFactor());

        return selectionProcessInformationDto;
    }


    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.SelectionProcessInformation, exceptionType, args);
    }
}
