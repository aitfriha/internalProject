package org.techniu.isbackend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.controller.request.SelectionTypeEvaluationAddrequest;
import org.techniu.isbackend.dto.mapper.SelectionTypeEvaluationMapper;
import org.techniu.isbackend.dto.model.SelectionTypeEvaluationDto;
import org.techniu.isbackend.dto.model.StaffDto;
import org.techniu.isbackend.entity.LegalCategoryType;
import org.techniu.isbackend.entity.SelectionTypeEvaluation;
import org.techniu.isbackend.entity.Staff;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.SelectionTypeEvaluationRepository;
import org.techniu.isbackend.repository.StaffRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;

@Service
@Transactional
public class SelectionTypeEvaluationServiceImpl implements SelectionTypeEvaluationService {
    private SelectionTypeEvaluationRepository selectionTypeEvaluationRepository;
    private StaffRepository staffRepository;
    private final SelectionTypeEvaluationMapper selectionTypeEvaluationMapper = Mappers.getMapper(SelectionTypeEvaluationMapper.class);
    SelectionTypeEvaluationServiceImpl(
            SelectionTypeEvaluationRepository selectionTypeEvaluationRepository,
            StaffRepository staffRepository) {
        this.selectionTypeEvaluationRepository = selectionTypeEvaluationRepository;
        this.staffRepository = staffRepository;
    }
    @Override
    public void save(List<SelectionTypeEvaluationDto> selectionTypeEvaluationDtos) {

        SelectionTypeEvaluation child = null;
        if(selectionTypeEvaluationDtos.size() == 2) {
            Optional<SelectionTypeEvaluation>  parentExist = Optional.ofNullable(selectionTypeEvaluationRepository.findByName(selectionTypeEvaluationDtos.get(1).getName()));
            if (parentExist.isPresent()) {
                throw exception(DUPLICATE_ENTITY);
            }
            child = selectionTypeEvaluationRepository.save(selectionTypeEvaluationMapper.dtoToModel(selectionTypeEvaluationDtos.get(1)));
        }

        SelectionTypeEvaluation parent = selectionTypeEvaluationRepository.findByName(selectionTypeEvaluationDtos.get(0).getName());
        if(parent != null) {
            List<SelectionTypeEvaluation> list1 = parent.getChilds();
            list1.add(child);
            parent.setChilds(list1);
        }
        else {
            List<SelectionTypeEvaluation> list1 = new ArrayList<>();
            list1.add(child);
            parent = selectionTypeEvaluationMapper.dtoToModel(selectionTypeEvaluationDtos.get(0));
            parent.setChilds(list1);
        }
        System.out.println(child);
        selectionTypeEvaluationRepository.save(parent);
    }

    @Override
    public void update(SelectionTypeEvaluationDto selectionTypeEvaluationDto) {

        Optional<SelectionTypeEvaluation>  selectionType = Optional.ofNullable(selectionTypeEvaluationRepository.findByName(selectionTypeEvaluationDto.getName()));
        System.out.println(selectionTypeEvaluationDto.getSelectionTypeId());
        if (selectionType.isPresent()) {
            if(!selectionType.get().get_id().equals(selectionTypeEvaluationDto.getSelectionTypeId())) {
                throw exception(DUPLICATE_ENTITY);
            }
        }

        SelectionTypeEvaluation selectionTypeEvaluation = selectionTypeEvaluationRepository.findById(selectionTypeEvaluationDto.getSelectionTypeId()).get();
        SelectionTypeEvaluation selectionTypeEvaluation1 = selectionTypeEvaluationMapper.dtoToModel(selectionTypeEvaluationDto);
        selectionTypeEvaluation1.setChilds(selectionTypeEvaluation.getChilds());
        selectionTypeEvaluationRepository.save(selectionTypeEvaluation1);
    }

    @Override
    public SelectionTypeEvaluation getSelectionTypeByName(String name) {
        return this.selectionTypeEvaluationRepository.findByName(name);
    }

    @Override
    public ResponseEntity<?> remove(String SelectionTypeId) {
        SelectionTypeEvaluation selectionTypeEvaluation = selectionTypeEvaluationRepository.findById(SelectionTypeId).get();
        if(selectionTypeEvaluation.getChilds() != null) {
            List<SelectionTypeEvaluation> list1 = selectionTypeEvaluation.getChilds();
            list1.forEach(type -> {
                selectionTypeEvaluationRepository.delete(type);
            });
        }
        selectionTypeEvaluationRepository.delete(selectionTypeEvaluation);
        return null;
    }

    @Override
    public List<SelectionTypeEvaluationDto> getAll() {
        List<SelectionTypeEvaluation> selectionTypeEvaluations = selectionTypeEvaluationRepository.findAll();
        // Create a list of all actions dto
        List<SelectionTypeEvaluationDto> selectionTypeEvaluationDtos = new ArrayList<>();

        for (SelectionTypeEvaluation selectionType : selectionTypeEvaluations) {
            SelectionTypeEvaluationDto selectionTypeEvaluationDto=selectionTypeEvaluationMapper.modelToDto(selectionType);
            List<SelectionTypeEvaluation> selectionTypeEvaluations1 = selectionType.getChilds();
            if(selectionTypeEvaluations1 != null){
                List<SelectionTypeEvaluationDto> selectionTypeEvaluationDtos1 = new ArrayList<>();
                for (SelectionTypeEvaluation selectionType1 : selectionTypeEvaluations1) {
                    SelectionTypeEvaluationDto selectionTypeEvaluationDto1 = selectionTypeEvaluationMapper.modelToDto(selectionType1);
                    selectionTypeEvaluationDtos1.add(selectionTypeEvaluationDto1);
                }
                selectionTypeEvaluationDto.setChilds(selectionTypeEvaluationDtos1);
            }
            selectionTypeEvaluationDtos.add(selectionTypeEvaluationDto);
        }
        return selectionTypeEvaluationDtos;
    }

    @Override
    public List<SelectionTypeEvaluationDto> getAllByType(String type) {
        List<SelectionTypeEvaluation> selectionTypeEvaluations = selectionTypeEvaluationRepository.findByType(type);
        // Create a list of all actions dto
        List<SelectionTypeEvaluationDto> selectionTypeEvaluationDtos = new ArrayList<>();

        for (SelectionTypeEvaluation selectionType : selectionTypeEvaluations) {
            SelectionTypeEvaluationDto selectionTypeEvaluationDto=selectionTypeEvaluationMapper.modelToDto(selectionType);
            List<SelectionTypeEvaluation> selectionTypeEvaluations1 = selectionType.getChilds();
            if(selectionTypeEvaluations1 != null){
                List<SelectionTypeEvaluationDto> selectionTypeEvaluationDtos1 = new ArrayList<>();
                for (SelectionTypeEvaluation selectionType1 : selectionTypeEvaluations1) {
                    SelectionTypeEvaluationDto selectionTypeEvaluationDto1 = selectionTypeEvaluationMapper.modelToDto(selectionType1);
                    selectionTypeEvaluationDtos1.add(selectionTypeEvaluationDto1);
                }
                selectionTypeEvaluationDto.setChilds(selectionTypeEvaluationDtos1);
            }
            selectionTypeEvaluationDtos.add(selectionTypeEvaluationDto);
        }
        return selectionTypeEvaluationDtos;
    }

    @Override
    public List<Staff> setLevelStaffs(List<Object> objects) {
        ObjectMapper mapper = new ObjectMapper();
        SelectionTypeEvaluation selectionTypeEvaluation = mapper.convertValue(objects.get(0), SelectionTypeEvaluation.class);
        List<Staff> staffs = mapper.convertValue(objects.get(1), new TypeReference<List<Staff>>(){});
        Optional<SelectionTypeEvaluation> level = selectionTypeEvaluationRepository.findById(selectionTypeEvaluation.get_id());
        /*if(level.isPresent()) {
            SelectionTypeEvaluation level1 = level.get();
            List<Staff> list = new ArrayList<>();
            staffs.forEach(staff -> {
                Staff staff1 = staffRepository.findById(staff.getStaffId()).get();
                staff1.setLevel(level1);
                list.add(staff1);
                staffRepository.save(staff1);
            });
            level1.setStaffs(list);
            selectionTypeEvaluationRepository.save(level1);
        }*/
        return null;
    }

    @Override
    public List<SelectionTypeEvaluation> getFunctionalStructureTree(String SelectionTypeId) {
        SelectionTypeEvaluation selectionType = selectionTypeEvaluationRepository.findById(SelectionTypeId).get();
        List<SelectionTypeEvaluation> list = new ArrayList<>();
        if(selectionType.getType().equals("Main Type")) {
            list.add(selectionType);
        } else if(selectionType.getType().equals("Sub Type")) {
            SelectionTypeEvaluation selectionType1 = selectionTypeEvaluationRepository.findByChildsContaining(selectionType);
            list.add(selectionType1);
            list.add(selectionType);
        }
        return list;
    }
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.SelectionTypeEvaluation, exceptionType, args);
    }
}
