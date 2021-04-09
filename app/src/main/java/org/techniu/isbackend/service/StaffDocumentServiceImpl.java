package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.StaffDocumentMapper;
import org.techniu.isbackend.dto.model.StaffDocumentDto;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.LogType;
import org.techniu.isbackend.entity.Staff;
import org.techniu.isbackend.entity.StaffDocument;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.StaffDocumentRepository;
import org.techniu.isbackend.repository.StaffRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
@Transactional
public class StaffDocumentServiceImpl implements StaffDocumentService {

    private StaffDocumentRepository staffDocumentRepository;
    private StaffRepository staffRepository;
    private LogService logService;
    private final StaffDocumentMapper staffDocumentMapper = Mappers.getMapper(StaffDocumentMapper.class);

    StaffDocumentServiceImpl(StaffDocumentRepository staffDocumentRepository, StaffRepository staffRepository, LogService logService) {
        this.staffDocumentRepository = staffDocumentRepository;
        this.staffRepository = staffRepository;
        this.logService = logService;
    }

    @Override
    public void save(StaffDocumentDto staffDocumentDto, String staffId) {

        StaffDocument staffDocument = staffDocumentMapper.dtoToModel(staffDocumentDto);

        if(
                staffDocument.getName().equals("")
                        || staffDocument.getNumber().equals("")
        ) {
            throw exception(FILL_ALL_NECESSARY_FIELDS);
        }

        if(
                staffDocument.getExpeditionDate().equals(staffDocument.getExpirationDate())
        ) {
            throw exception(STAFF_DOCUMENT_DATE_EQUAL);
        }

        Optional<StaffDocument> document = Optional.ofNullable(staffDocumentRepository.findByNumber(staffDocument.getNumber()));
        if (document.isPresent()) {
                throw exception(STAFF_DOCUMENT_NUMBER_EXIST);
        }

        Staff staff = staffRepository.findById(staffId).get();
        List<StaffDocument> list = staff.getStaffDocuments();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(staffDocumentRepository.save(staffDocument));
        staff.setStaffDocuments(list);
        staffRepository.save(staff);
    }

    @Override
    public void remove(String staffDocumentId) {
        StaffDocument staffDocument = staffDocumentRepository.findById(staffDocumentId).get();
        Staff staff = staffRepository.findByStaffDocumentsContaining(staffDocument);
        List<StaffDocument> list = staff.getStaffDocuments();
        list.remove(staffDocument);
        staff.setStaffDocuments(list);
        staffRepository.save(staff);
        logService.addLog(LogType.DELETE, ClassType.STAFF,"delete document "+staffDocument.getName()+" from staff "+staff.getMotherFamilyName()+" "+staff.getFatherFamilyName() + " "+staff.getFirstName());
        staffDocumentRepository.delete(staffDocument);
    }

    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.StaffDocuments, exceptionType, args);
    }
}
