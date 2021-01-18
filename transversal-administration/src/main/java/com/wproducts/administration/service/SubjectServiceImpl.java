package com.wproducts.administration.service;

import com.wproducts.administration.controller.request.SubjectAddRequest;
import com.wproducts.administration.dto.mapper.SubjectMapper;
import com.wproducts.administration.dto.model.SubjectDto;
import com.wproducts.administration.model.Subject;
import com.wproducts.administration.repository.SubjectRepository;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;
@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    private final SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void save(SubjectAddRequest subjectAddRequest) {
        subjectAddRequest.setSubjectCode(subjectAddRequest.getSubjectCode().toLowerCase());

        Optional<Subject> subject = Optional.ofNullable(subjectRepository.findBySubjectCode(subjectAddRequest.getSubjectCode()));
        if (subjectAddRequest.getSubjectCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        if (subject.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }

        Subject subjectParent1 = null;
        if(subjectAddRequest.getSubjectParent() != null && !subjectAddRequest.getSubjectParent().equals("without_subject_parent") ) {
        Optional<Subject> subjectParent = Optional.ofNullable(subjectRepository.findBySubjectCode(subjectAddRequest.getSubjectParent()));
        if (!subjectParent.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
            subjectParent1 = subjectParent.get();
        }

        Subject subject1 = subjectMapper.dtoToModel(subjectMapper.addRequestToDto(subjectAddRequest))
                .setSubjectCreatedAt(Instant.now())
//                .setSubjectFields(fields)
                .setSubjectParent(subjectParent1);
        // save subject
        subjectRepository.save(subject1);
    }

    @Override
    public void updateSubject(SubjectDto subjectDto) {

        subjectDto.setSubjectCode(subjectDto.getSubjectCode().toLowerCase());

        Optional<Subject> subject = Optional.ofNullable(subjectRepository.findSubjectBy_id(subjectDto.getSubjectId()));
        Optional<Subject> subjectCode = Optional.ofNullable(subjectRepository.findBySubjectCode(subjectDto.getSubjectCode()));
        if (!subject.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        if (subjectCode.isPresent() && !(subject.get().getSubjectCode().equals(subjectDto.getSubjectCode())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
        if (subjectDto.getSubjectCode().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }

        Subject subject1 = subjectMapper.dtoToModel(subjectDto) ;
            subject1.setSubjectUpdatedAt(Instant.now())
                    .setSubjectCreatedAt(subject.get().getSubjectCreatedAt());

        if(subjectDto.getSubjectParent() != null) {
            Subject subjectParent  = subjectRepository.findBySubjectCode(subjectDto.getSubjectParent().getSubjectCode());
            //save subjectParent
            subject1.setSubjectParent(subjectParent);
        }else{
            subject1.setSubjectUpdatedAt(Instant.now())
                    .setSubjectCreatedAt(subject.get().getSubjectCreatedAt())
                    .setSubjectParent(subject.get().getSubjectParent());
        }
        subject1.setSubjectUpdatedAt(Instant.now());
        subject1.setSubjectCreatedAt(subject.get().getSubjectCreatedAt());
        // Update subject in database
        subjectRepository.save(subject1);
    }


    @Override
    public void removeSubject(String id) {
        Optional<Subject> subject = Optional.ofNullable(subjectRepository.findSubjectBy_id(id));
        // If subject doesn't exists
        if (!subject.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        subjectRepository.deleteById(id);
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        //list Subject
        List<Subject> subjects = subjectRepository.findAll();
        ArrayList<SubjectDto> list = new ArrayList<>();
        for (Subject subject : subjects) {
            SubjectDto subjectDto = subjectMapper.modelToDto(subject);
            if(subject.getSubjectCreatedAt() !=null ) {
                subjectDto.setSubjectCreatedAt(subject.getSubjectCreatedAt().toString());
            }
            if(subject.getSubjectUpdatedAt() !=null ) {
                subjectDto.setSubjectUpdatedAt(subject.getSubjectUpdatedAt().toString());
            }
//            subjectDto.setSubjectFields(subjectFieldDtos);
            subjectDto.setSubjectParent(subjectMapper.modelToDto(subject.getSubjectParent()));
            list.add(subjectDto);
        }
        return list;
    }

    @Override
    public SubjectDto getOneSubject(String id) {
        Optional<Subject> subject = Optional.ofNullable(subjectRepository.findSubjectBy_id(id));

        // If subject doesn't exists
        if (!subject.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        SubjectDto subjectDto = subjectMapper.modelToDto(subject.get());
        if(subject.get().getSubjectUpdatedAt() !=null ) {
            subjectDto.setSubjectUpdatedAt(subject.get().getSubjectUpdatedAt().toString());
        }
        if(subject.get().getSubjectCreatedAt()!=null ) {
            subjectDto.setSubjectCreatedAt(subject.get().getSubjectCreatedAt().toString());
        }
        return subjectDto;
    }
    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Subject, exceptionType, args);
    }
}
