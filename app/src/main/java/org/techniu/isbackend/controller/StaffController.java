package org.techniu.isbackend.controller;

import com.wproducts.administration.controller.request.UserAddRequest;
import com.wproducts.administration.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.StaffAddrequest;
import org.techniu.isbackend.controller.request.StaffUpdaterequest;
import org.techniu.isbackend.dto.mapper.StaffMapper;
import org.techniu.isbackend.dto.model.StaffDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.StaffService;

import static org.techniu.isbackend.exception.EntityType.USER;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;
import static org.techniu.isbackend.exception.EntityType.Staff;

import org.springframework.http.HttpStatus;
import org.techniu.isbackend.service.utilities.MailMail;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin("*")
public class StaffController {

    private StaffService staffService;
    private final MapValidationErrorService mapValidationErrorService;
    private final UserService userService;
    private final StaffMapper staffMapper = Mappers.getMapper(StaffMapper.class);

    StaffController(StaffService staffService, MapValidationErrorService mapValidationErrorService, UserService userService){
        this.staffService = staffService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.userService = userService;
    }

    /**
     * Handles the incoming POST API "/application/add"
     *
     * @param staffAddRequest staffAddRequest
     * @return ApplicationDto
     */
    @PostMapping("/add")
    public ResponseEntity add(@ModelAttribute @Valid StaffAddrequest staffAddRequest, BindingResult bindingResult,
                              @RequestParam("idCardDoc") MultipartFile idCardDoc,
                              @RequestParam("passportDoc") MultipartFile passportDoc,
                              @RequestParam("professionalIdCardDoc") MultipartFile professionalIdCardDoc,
                              @RequestParam("hnsCardDoc") MultipartFile hnsCardDoc,
                              @RequestParam("contractDoc") MultipartFile contractDoc,
                              @RequestParam("internalRulesDoc") MultipartFile internalRulesDoc,
                              @RequestParam("preContractDoc") MultipartFile preContractDoc) throws IOException {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // objet address
        Address address =new Address()
        .setFullAddress(staffAddRequest.getFullAddress())
        .setPostCode(staffAddRequest.getPostCode());
        // staffDocumentList
        List<StaffDocument> staffDocumentList =new ArrayList();
        // id card
        if(!idCardDoc.getContentType().equals("application/json")) {
            StaffDocument staffDocumentCard= new StaffDocument();
            staffDocumentCard.setDocument(idCardDoc.getBytes());
            staffDocumentCard.setDocExtension(staffAddRequest.getIdCardDocExtension());
            staffDocumentCard.setName(staffAddRequest.getIdCardName());
            staffDocumentCard.setNumber(staffAddRequest.getIdCardNumber());
            staffDocumentList.add(staffDocumentCard);
        };
        // passport
        if(!passportDoc.getContentType().equals("application/json")){
            StaffDocument staffDocumentPassport= new StaffDocument();
            staffDocumentPassport.setDocument(passportDoc.getBytes());
            staffDocumentPassport.setDocExtension(staffAddRequest.getPassportDocExtension());
            staffDocumentPassport.setName(staffAddRequest.getPassportName());
            staffDocumentPassport.setNumber(staffAddRequest.getPassportNumber());
            staffDocumentList.add(staffDocumentPassport);
        }
        // professional id
        if(!professionalIdCardDoc.getContentType().equals("application/json")){
            StaffDocument staffDocumentProfessional= new StaffDocument();
            staffDocumentProfessional.setDocument(professionalIdCardDoc.getBytes());
            staffDocumentProfessional.setDocExtension(staffAddRequest.getProfessionalIdCardDocExtension());
            staffDocumentProfessional.setName(staffAddRequest.getProfessionalName());
            staffDocumentProfessional.setNumber(staffAddRequest.getProfessionalIdCardNumber());
            staffDocumentList.add(staffDocumentProfessional);
        }
        // Health National Security
        if(!hnsCardDoc.getContentType().equals("application/json")){
            StaffDocument staffDocumentHns= new StaffDocument();
            staffDocumentHns.setDocument(hnsCardDoc.getBytes());
            staffDocumentHns.setDocExtension(staffAddRequest.getHnsDocExtension());
            staffDocumentHns.setName(staffAddRequest.getHnsName());
            staffDocumentHns.setNumber(staffAddRequest.getHnsNumber());
            staffDocumentList.add(staffDocumentHns);
        }

        // Staff contract
        StaffContract staffContract = new StaffContract();
        staffContract.setAssociateOffice(staffAddRequest.getAssociateOffice());
        staffContract.setHiringCountry(staffAddRequest.getHiringCountry());
        staffContract.setTownContract(staffAddRequest.getTownContract());
        staffContract.setPersonalNumber(staffAddRequest.getPersonalNumber());
        staffContract.setHighDate(staffAddRequest.getHighDate());
        staffContract.setLowDate(staffAddRequest.getLowDate());
        staffContract.setRegistrationDate(staffAddRequest.getRegistrationDate());
        staffContract.setPreContractDate(staffAddRequest.getPreContractDate());
        if(contractDoc.getContentType().equals("application/pdf")){
            staffContract.setContractDoc(contractDoc.getBytes());
        }
        if(internalRulesDoc.getContentType().equals("application/pdf")){
            staffContract.setInternalRulesDoc(internalRulesDoc.getBytes());
        }
        if(preContractDoc.getContentType().equals("application/pdf")){
            staffContract.setPreContractDoc(preContractDoc.getBytes());
        }
        // Staff economic contract
        StaffEconomicContractInformation staffEconomicContractInformation = new StaffEconomicContractInformation();
        staffEconomicContractInformation.setContractSalary(staffAddRequest.getContractSalary());
        staffEconomicContractInformation.setCompanyContractCost(staffAddRequest.getCompanyContractCost());
        staffEconomicContractInformation.setExpenses(staffAddRequest.getExpenses());
        staffEconomicContractInformation.setCompanyExpensesCost(staffAddRequest.getCompanyExpensesCost());
        staffEconomicContractInformation.setObjectives(staffAddRequest.getObjectives());
        staffEconomicContractInformation.setCompanyObjectivesCost(staffAddRequest.getCompanyObjectivesCost());
        staffEconomicContractInformation.setTotalCompanyCost(staffAddRequest.getTotalCompanyCost());
        staffEconomicContractInformation.setContractSalaryDateGoing(staffAddRequest.getContractSalaryDateGoing());
        staffEconomicContractInformation.setContractSalaryDateOut(staffAddRequest.getContractSalaryDateOut());
        staffEconomicContractInformation.setCompanyContractCostDateGoing(staffAddRequest.getCompanyContractCostDateGoing());
        staffEconomicContractInformation.setCompanyContractCostDateOut(staffAddRequest.getCompanyContractCostDateOut());
        staffEconomicContractInformation.setExpensesDateGoing(staffAddRequest.getExpensesDateGoing());
        staffEconomicContractInformation.setExpensesDateOut(staffAddRequest.getExpensesDateOut());
        staffEconomicContractInformation.setCompanyExpensesCostDateGoing(staffAddRequest.getCompanyExpensesCostDateGoing());
        staffEconomicContractInformation.setCompanyExpensesCostDateOut(staffAddRequest.getCompanyExpensesCostDateOut());
        staffEconomicContractInformation.setObjectivesDateGoing(staffAddRequest.getObjectivesDateGoing());
        staffEconomicContractInformation.setObjectivesDateOut(staffAddRequest.getObjectivesDateOut());
        staffEconomicContractInformation.setCompanyObjectivesCostDateGoing(staffAddRequest.getCompanyObjectivesCostDateGoing());
        staffEconomicContractInformation.setCompanyObjectivesCostDateOut(staffAddRequest.getCompanyObjectivesCostDateOut());
        staffEconomicContractInformation.setTotalCompanyCostDateGoing(staffAddRequest.getTotalCompanyCostDateGoing());
        staffEconomicContractInformation.setTotalCompanyCostDateOut(staffAddRequest.getTotalCompanyCostDateOut());
        Resource resource = new ClassPathResource("applicationContext.xml");
        BeanFactory b = new XmlBeanFactory(resource);
        MailMail m = (MailMail) b.getBean("mailMail");
        String sender = "internal.system.project@gmail.com";//write here sender gmail id
        String[] receivers = {staffAddRequest.getCompanyEmail()};
        //m.sendMail(sender,"Internal System", receivers,"New Absence Request","Hello " + sendToName +",\nYou got a new absence request from " + fromName + ".\n Regards,\n Internal System.");
        String message = "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \">Hello " + staffAddRequest.getMotherFamilyName() + ",</span></span></p>\n" +
                "<p><span style=\"font-family: arial, helvetica, sans-serif; \"><span \">you are registered on the internal system internal systems</p>";
        try {
            m.sendMail(sender, "Internal System", receivers, "New registered", message);
        } catch (MailSendException e) {
            throw exception(STAFF_EMAIL_INVALID);
        }
        staffService.save(staffMapper.addRequestToDto(staffAddRequest),
                address, staffEconomicContractInformation, staffContract, staffDocumentList);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Staff, ADDED)), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody @Valid StaffUpdaterequest staffUpdaterequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        Address address =new Address()
                .setAddressId(staffUpdaterequest.getAddressId())
                .setFullAddress(staffUpdaterequest.getFullAddress())
                .setPostCode(staffUpdaterequest.getPostCode());
        staffService.update(staffMapper.updateRequestToDto(staffUpdaterequest), address);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Staff, UPDATED)), HttpStatus.OK);
    }

    @RequestMapping(path = "staff-by-id/{staffId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public StaffDto getStaffById(@PathVariable("staffId") String staffId){
        return staffService.getStaffById(staffId);
    }

    @RequestMapping(path = "staff-by-company-email/{companyEmail}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getStaffByCompanyEmail(@PathVariable("companyEmail") String companyEmail){
       return new ResponseEntity<Response>(Response.ok().setPayload(staffService.getStaffByCompanyEmail(companyEmail)), HttpStatus.OK);
    }

    /**
     * display all staff GET API "/api/staff/all"
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity allStaff() {
        return new ResponseEntity<Response>(Response.ok().setPayload(staffService.getAll()), HttpStatus.OK);
    }

    @RequestMapping(path = "staff-no-assigned-functional",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDto> getFunctionalNotAssignedStaff(){
        return staffService.getAllFunctionalNotAssignedStaffs();
    }

    @RequestMapping(path = "staff-no-assigned-administrative",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDto> getAdministrativeNotAssignedStaff(){
        return staffService.getAllAdministrativeNotAssignedStaffs();
    }

    @RequestMapping(path = "staff-no-assigned-administrative-by-company/{companyId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDto> getAdministrativeNotAssignedStaffByCompany(@PathVariable("companyId") String companyId){
        return staffService.getAllAdministrativeNotAssignedStaffsByCompany(companyId);
    }
    @RequestMapping(path = "by-company/{companyId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDto> getAllStaffsByCompany(@PathVariable("companyId") String companyId){
        return staffService.getAllStaffsByCompany(companyId);
    }

    @RequestMapping(path = "assign-functional-level-staff",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void assignFunctionalLevelToStaff(@RequestBody List<Object> objects){
        staffService.assignFunctionalLevelToStaff(objects);
    }

    @RequestMapping(path = "assign-administrative-level-staff",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void assignAdministrativeLevelToStaff(@RequestBody List<Object> objects){
        staffService.assignAdministrativeLevelToStaff(objects);
    }

    @GetMapping("get-staff-by-functional-level/levelId={levelId}&isFunctionalLeader={isFunctionalLeader}")
    public List<StaffDto> getStaffsByFunctionalLevel(@PathVariable("levelId") String levelId, @PathVariable("isFunctionalLeader") String isFunctionalLeader){
        return staffService.getStaffsByFunctionalLevel(levelId, isFunctionalLeader);
    }

    @GetMapping("get-staff-by-administrative-level/levelId={levelId}&isAdministrativeLeader={isAdministrativeLeader}")
    public List<StaffDto> getStaffsByAdministrativeLevel(@PathVariable("levelId") String levelId, @PathVariable("isAdministrativeLeader") String isAdministrativeLeader){
        return staffService.getStaffsByAdministrativeLevel(levelId, isAdministrativeLeader);
    }

    @GetMapping("get-staff-by-isFunctionalLeader/isFunctionalLeader={isFunctionalLeader}")
    public List<StaffDto> getStaffsByIsFunctionalLeader(@PathVariable("isFunctionalLeader") String isFunctionalLeader){
        return staffService.getStaffsByIsFunctionalLeader(isFunctionalLeader);
    }

    @GetMapping("get-staff-by-isAdministrativeLeader/isAdministrativeLeader={isAdministrativeLeader}")
    public List<StaffDto> getStaffsByIsAdministrativeLeader(@PathVariable("isAdministrativeLeader") String isAdministrativeLeader){
        return staffService.getStaffsByIsAdministrativeLeader(isAdministrativeLeader);
    }

    /**
     * display all staff GET API "/api/staff/allStaffAssignedToFunctionalLevel"
     * @return
     */
    @GetMapping("/allStaffAssignedToFunctionalLevel")
    public ResponseEntity allStaffAssignedToFunctionalLevel() {
        List<FunctionalStructureLevel> listFunctionalStructureLevels;
        List<StaffDto> staffLists=staffService.getAll();
        List<StaffDto> staffListsNew=new ArrayList<>();
        for(StaffDto  staffdto : staffLists){
            listFunctionalStructureLevels= staffdto.getFunctionalStructureLevels();
            for(FunctionalStructureLevel  functionalStructureLevel : listFunctionalStructureLevels){
                if(functionalStructureLevel.getIsCommercialLevel().equals("yes")){
                    staffListsNew.add(staffdto);
                }
            }
        }
        return new ResponseEntity<Response>(Response.ok().setPayload(staffListsNew), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/user/add"
     *
     * @param userAddRequest User Add request
     * @return ActionDto
     */
    @PostMapping("/user/add")
    public ResponseEntity signup(@RequestBody @Valid UserAddRequest userAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        System.out.print("passed .....");
        staffService.saveUser(userAddRequest);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(USER, ADDED)), HttpStatus.OK);
    }
    /**
     * Handles the incoming POST API "/user/signup"
     *
     * @return Response
     */
    @RequestMapping(method = RequestMethod.GET, value = "/user/forgetPassword/{userEmail}")
    public ResponseEntity forgetPassword(@PathVariable String userEmail) {
        staffService.sendPassword(userEmail);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(USER, SENT)), HttpStatus.OK);
    }

    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.AbsenceRequest, exceptionType, args);
    }

}
