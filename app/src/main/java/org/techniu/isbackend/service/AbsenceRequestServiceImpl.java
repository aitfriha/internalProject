package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.AbsenceRequestMapper;
import org.techniu.isbackend.dto.model.AbsenceRequestDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.AbsenceRequestRepository;
import org.techniu.isbackend.repository.AbsenceTypeRepository;
import org.techniu.isbackend.repository.StaffRepository;
import org.techniu.isbackend.repository.StateCountryRepository;
import org.techniu.isbackend.service.utilities.MailMail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
@Transactional
public class AbsenceRequestServiceImpl implements AbsenceRequestService {

    private AbsenceRequestRepository absenceRequestRepository;
    private StateCountryRepository stateCountryRepository;
    private StaffRepository staffRepository;
    private AbsenceTypeRepository absenceTypeRepository;
    private final AbsenceRequestMapper absenceRequestMapper = Mappers.getMapper(AbsenceRequestMapper.class);

    AbsenceRequestServiceImpl(AbsenceRequestRepository absenceRequestRepository,
                              StateCountryRepository stateCountryRepository,
                              StaffRepository staffRepository,
                              AbsenceTypeRepository absenceTypeRepository) {
        this.absenceRequestRepository = absenceRequestRepository;
        this.stateCountryRepository = stateCountryRepository;
        this.staffRepository = staffRepository;
        this.absenceTypeRepository = absenceTypeRepository;
    }

    @Override
    public void save(AbsenceRequestDto absenceRequestDto, String fromName) {
        AbsenceRequest absenceRequest = absenceRequestMapper.dtoToModel(absenceRequestDto);
        Staff staff = staffRepository.findById(absenceRequestDto.getStaffId()).get();
        absenceRequest.setStaff(staff);
        AbsenceType absenceType = absenceTypeRepository.findById(absenceRequestDto.getAbsenceTypeId()).get();
        absenceRequest.setAbsenceType(absenceType);
        absenceRequest.setState("In progress");
        absenceRequestRepository.save(absenceRequest);
        Resource resource=new ClassPathResource("applicationContext.xml");
        BeanFactory b=new XmlBeanFactory(resource);
        MailMail m=(MailMail)b.getBean("mailMail");
        String sender="internal.system.project@gmail.com";//write here sender gmail id
        String[] receivers = {absenceType.getAbsenceResponsible().getCompanyEmail(), absenceType.getInCopyResponsible().getCompanyEmail()};
        //m.sendMail(sender,"Internal System", receivers,"New Absence Request","Hello " + sendToName +",\nYou got a new absence request from " + fromName + ".\n Regards,\n Internal System.");
       String message = "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \">Hello,</span></span></p>\n" +
               "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \">You got a " + absenceType.getName() + " request from <strong>" + fromName + "</strong>.</span></span></p>\n" +
               "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \">Regards,</span></span></p>\n" +
               "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \"><span style=\"color: #999999;\"><strong>Internal System</strong></span>.</span></span></p>\n" +
               "<p>&nbsp;</p>";
        m.sendMail(sender,"Internal System", receivers,"New Absence Request",message);

    }

    @Override
    public void update(AbsenceRequestDto absenceRequestDto) {
        AbsenceRequest absenceRequest = absenceRequestRepository.findById(absenceRequestDto.getAbsenceRequestId()).get();
        absenceRequest.setState(absenceRequestDto.getState());
        absenceRequestRepository.save(absenceRequest);
        Resource resource=new ClassPathResource("applicationContext.xml");
        BeanFactory b=new XmlBeanFactory(resource);
        MailMail m=(MailMail)b.getBean("mailMail");
        String sender="internal.system.project@gmail.com";//write here sender gmail id
        String[] receivers = {absenceRequest.getStaff().getCompanyEmail()};
        //m.sendMail(sender,"Internal System", receivers,"New Absence Request","Hello " + sendToName +",\nYou got a new absence request from " + fromName + ".\n Regards,\n Internal System.");
        String message = "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \">Hello,</span></span></p>\n" +
                "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \">Your absence request has been <strong>" + absenceRequestDto.getState() + "</strong>.</span></span></p>\n" +
                "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \">Regards,</span></span></p>\n" +
                "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \"><span style=\"color: #999999;\"><strong>Internal System</strong></span>.</span></span></p>\n" +
                "<p>&nbsp;</p>";
        m.sendMail(sender,"Internal System", receivers,"Absence request result",message);
    }

    @Override
    public void remove(String id) {

        Optional<AbsenceRequest> absenceRequest = absenceRequestRepository.findById(id);
        if (!absenceRequest.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        absenceRequestRepository.deleteById(id);
    }

    @Override
    public List<AbsenceRequestDto> getAll() {

        List<AbsenceRequest> absenceRequests = absenceRequestRepository.findAll();
        // Create a list of all actions dto
        List<AbsenceRequestDto> absenceRequestDtos = new ArrayList<>();

        for (AbsenceRequest absenceRequest : absenceRequests) {

            absenceRequestDtos.add(absenceRequestToAbsenceRequestDto(absenceRequest));
        }
        return absenceRequestDtos;
    }

    @Override
    public List<AbsenceRequest> getAllByAbsenceType(String absenceTypeId) {
        AbsenceType absenceType = absenceTypeRepository.findById(absenceTypeId).get();
        return absenceRequestRepository.findAllByAbsenceType(absenceType);
    }

    public AbsenceRequestDto absenceRequestToAbsenceRequestDto(AbsenceRequest absenceRequest) {
        AbsenceRequestDto absenceRequestDto=absenceRequestMapper.modelToDto(absenceRequest);
        absenceRequestDto.setStaffId(absenceRequest.getStaff().getStaffId());
        absenceRequestDto.setStaffName(absenceRequest.getStaff().getFirstName() +
                " " + absenceRequest.getStaff().getFatherFamilyName()+
                " " + absenceRequest.getStaff().getMotherFamilyName());
        absenceRequestDto.setAbsenceTypeId(absenceRequest.getAbsenceType().get_id());
        absenceRequestDto.setAbsenceTypeName(absenceRequest.getAbsenceType().getName());
        return absenceRequestDto;
    }


    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.AbsenceRequest, exceptionType, args);
    }
}
