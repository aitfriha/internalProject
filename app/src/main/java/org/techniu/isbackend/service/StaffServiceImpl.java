package org.techniu.isbackend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.mapper.FunctionalStructureLevelMapper;
import org.techniu.isbackend.dto.mapper.StaffMapper;
import org.techniu.isbackend.dto.model.AdministrativeStructureLevelDto;
import org.techniu.isbackend.dto.model.FunctionalStructureLevelDto;
import org.techniu.isbackend.dto.model.StaffDto;
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
public class StaffServiceImpl implements StaffService {
    private StaffRepository staffRepository;
    private FunctionalStructureLevelRepository functionalStructureLevelRepository;
    private AdministrativeStructureLevelRepository administrativeStructureLevelRepository;
    private AddressRepository addressRepository;
    private StaffDocumentRepository staffDocumentRepository;
    private StaffContractRepository staffContractRepository;
    private StaffContractHistoryRepository staffContractHistoryRepository;
    private CityRepository cityRepository;
    private ContractTypeRepository contractTypeRepository;
    private LegalCategoryTypeRepository legalCategoryTypeRepository;
    private ContractModelRepository contractModelRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private StaffEconomicContractInformationRepository staffEconomicContractInformationRepository;
    private StaffEconomicContractInformationHistoryRepository staffEconomicContractInformationHistoryRepository;
    private FunctionalStructureAssignationHistoryRepository functionalStructureAssignationHistoryRepository;
    private AdministrativeStructureAssignationHistoryRepository administrativeStructureAssignationHistoryRepository;
    private CurrencyRepository currencyRepository;

    private final StaffMapper staffMapper = Mappers.getMapper(StaffMapper.class);

    StaffServiceImpl(
            StaffRepository staffRepository,
            FunctionalStructureLevelRepository functionalStructureLevelRepository,
            AdministrativeStructureLevelRepository administrativeStructureLevelRepository,
            StaffDocumentRepository staffDocumentRepository,
            StaffContractRepository staffContractRepository,
            StaffContractHistoryRepository staffContractHistoryRepository,
            ContractTypeRepository contractTypeRepository,
            ContractModelRepository contractModelRepository,
            LegalCategoryTypeRepository legalCategoryTypeRepository,
            FinancialCompanyRepository financialCompanyRepository,
            AddressRepository addressRepository, CityRepository cityRepository,
            StaffEconomicContractInformationRepository staffEconomicContractInformationRepository,
            StaffEconomicContractInformationHistoryRepository staffEconomicContractInformationHistoryRepository,
            FunctionalStructureAssignationHistoryRepository functionalStructureAssignationHistoryRepository,
            AdministrativeStructureAssignationHistoryRepository administrativeStructureAssignationHistoryRepository,
            CurrencyRepository currencyRepository) {
        this.staffRepository = staffRepository;
        this.functionalStructureLevelRepository = functionalStructureLevelRepository;
        this.administrativeStructureLevelRepository = administrativeStructureLevelRepository;
        this.staffDocumentRepository = staffDocumentRepository;
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
        this.staffEconomicContractInformationRepository = staffEconomicContractInformationRepository;
        this.staffContractRepository = staffContractRepository;
        this.staffContractHistoryRepository = staffContractHistoryRepository;
        this.contractTypeRepository = contractTypeRepository;
        this.legalCategoryTypeRepository = legalCategoryTypeRepository;
        this.contractModelRepository = contractModelRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.staffEconomicContractInformationHistoryRepository = staffEconomicContractInformationHistoryRepository;
        this.functionalStructureAssignationHistoryRepository = functionalStructureAssignationHistoryRepository;
        this.administrativeStructureAssignationHistoryRepository = administrativeStructureAssignationHistoryRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Staff save(StaffDto staffDto, Address address, StaffEconomicContractInformation staffEconomicContractInformation, StaffContract staffContract,  List<StaffDocument> staffDocumentList) {
        address.setCity( cityRepository.findCityBy_id(staffDto.getCityId()));

        if(
                staffDto.getFirstName().equals("")
                || staffDto.getFatherFamilyName().equals("")
                || staffDto.getMotherFamilyName().equals("")
                || staffDto.getCityId().equals("")
                || staffDto.getPostCode().equals("")
                || staffDto.getPersonalPhone().equals("")
                || staffDto.getPersonalEmail().equals("")
                || staffDto.getTownContract().equals("")
                || staffDto.getAssociateOffice().equals("")
                || staffDto.getContractTypeId().equals("")
                || staffDto.getLegalCategoryTypeId().equals("")
                || staffDto.getContractModelId().equals("")
                || staffDto.getCurrencyId().equals("")
                || staffDto.getPersonalNumber().equals("")
        ) {
            throw exception(FILL_ALL_NECESSARY_FIELDS);
        }
        if (staffDto.getStaffDocuments() != null) {
            staffDto.getStaffDocuments().forEach(doc -> {
                if(
                        doc.getName().equals("")
                                || doc.getNumber().equals("")
                ) {
                    throw exception(FILL_ALL_NECESSARY_FIELDS);
                }

                Optional<StaffDocument> document = Optional.ofNullable(staffDocumentRepository.findByNumber(doc.getNumber()));
                if (document.isPresent()) {
                    throw exception(STAFF_DOCUMENT_NUMBER_EXIST);
                }
            });
        }

        Optional<Staff>  staff1= Optional.ofNullable(staffRepository.findByFirstNameAndFatherFamilyNameAndMotherFamilyName(staffDto.getFirstName(), staffDto.getFatherFamilyName(), staffDto.getMotherFamilyName()));
        if (staff1.isPresent()) {
            throw exception(STAFF_NAME_EXIST);
        }

        Optional<Staff>  staff2= Optional.ofNullable(staffRepository.findByPersonalEmail(staffDto.getPersonalEmail()));
        if (staff2.isPresent()) {
            throw exception(STAFF_EMAIL_EXIST);
        }

        Optional<Staff>  staff3= Optional.ofNullable(staffRepository.findByCompanyEmail(staffDto.getCompanyEmail()));
        if (staff3.isPresent()) {
            throw exception(STAFF_COMPANY_EMAIL_EXIST);
        }

        Optional<Staff>  staff4= Optional.ofNullable(staffRepository.findByCompanyMobilePhone(staffDto.getCompanyMobilePhone()));
        if (staff4.isPresent()) {
            throw exception(STAFF_COMPANY_MOBILE_PHONE_EXIST);
        }

        Optional<Staff>  staff5= Optional.ofNullable(staffRepository.findByPersonalPhone(staffDto.getPersonalPhone()));
        if (staff5.isPresent()) {
            throw exception(STAFF_PERSONAL_PHONE_EXIST);
        }

        Optional<Staff>  staff6= Optional.ofNullable(staffRepository.findBySkype(staffDto.getSkype()));
        if (staff6.isPresent()) {
            throw exception(STAFF_SKYPE_EXIST);
        }

        Optional<StaffContract>  staffContract2= Optional.ofNullable(staffContractRepository.findByPersonalNumber(staffDto.getPersonalNumber()));
        if (staffContract2.isPresent()) {
            throw exception(STAFF_PERSONAL_NUMBER_EXIST);
        }

        Staff staff = staffMapper.dtoToModel(staffDto);
        staffContract.setCompany(financialCompanyRepository.findById(staffDto.getCompanyId()).get());
        staffContract.setContractType(contractTypeRepository.findById(staffDto.getContractTypeId()).get());
        staffContract.setLegalCategoryType(legalCategoryTypeRepository.findById(staffDto.getLegalCategoryTypeId()).get());
        staffContract.setContractModel(contractModelRepository.findById(staffDto.getContractModelId()).get());
        StaffContract staffContract1 = staffContractRepository.save(staffContract);
        StaffContractHistory staffContractHistory = new StaffContractHistory();
        staffContractHistory.setStaffContract(staffContract1);
        staffContractHistory.setStaffContractHistory(staffContract1);
        staffContractHistory.setUpdatedAt(staffDto.getCreatedAt());
        staffContractHistoryRepository.save(staffContractHistory);
        staff.setAddress(addressRepository.save(address));
        staff.setStaffContract(staffContract1);
        Currency currency = currencyRepository.findById(staffDto.getCurrencyId()).get();
        staffEconomicContractInformation.setCurrency(currency);
        StaffEconomicContractInformation staffEconomicContractInformation1 = staffEconomicContractInformationRepository.save(staffEconomicContractInformation);
        StaffEconomicContractInformationHistory staffEconomicContractInformationHistory = new StaffEconomicContractInformationHistory();
        staffEconomicContractInformationHistory.setStaffEconomicContractInformation(staffEconomicContractInformation1);
        staffEconomicContractInformationHistory.setStaffEconomicContractInformationHistory(staffEconomicContractInformation1);
        staffEconomicContractInformationHistory.setUpdatedAt(staffDto.getCreatedAt());
        staffEconomicContractInformationHistoryRepository.save(staffEconomicContractInformationHistory);
        staff.setStaffEconomicContractInformation(staffEconomicContractInformation1);
        staff.setStaffDocuments(staffDocumentRepository.saveAll(staffDocumentList));
        staff.setFunctionalStructureLevels(new ArrayList<>());
        staff.setAdministrativeStructureLevels(new ArrayList<>());
        return staffRepository.save(staff);
    }

    @Override
    public Staff update(StaffDto staffDto, Address address) {
        System.out.println(staffDto);
        if(
                staffDto.getFirstName().equals("")
                        || staffDto.getFatherFamilyName().equals("")
                        || staffDto.getMotherFamilyName().equals("")
                        || staffDto.getCityId().equals("")
                        || staffDto.getPostCode().equals("")
                        || staffDto.getPersonalPhone().equals("")
                        || staffDto.getPersonalEmail().equals("")
        ) {
            throw exception(FILL_ALL_NECESSARY_FIELDS);
        }

        Optional<Staff>  staff1= Optional.ofNullable(staffRepository.findByFirstNameAndFatherFamilyNameAndMotherFamilyName(staffDto.getFirstName(), staffDto.getFatherFamilyName(), staffDto.getMotherFamilyName()));
        if (staff1.isPresent()) {
            if(!staff1.get().getStaffId().equals(staffDto.getStaffId())) {
                throw exception(STAFF_NAME_EXIST);
            }
        }

        Optional<Staff>  staff2= Optional.ofNullable(staffRepository.findByPersonalEmail(staffDto.getPersonalEmail()));
        if (staff2.isPresent()) {
            if(!staff2.get().getStaffId().equals(staffDto.getStaffId())) {
                throw exception(STAFF_EMAIL_EXIST);
            }
        }

        Optional<Staff>  staff3= Optional.ofNullable(staffRepository.findByCompanyEmail(staffDto.getCompanyEmail()));
        if (staff3.isPresent()) {
            if(!staff3.get().getStaffId().equals(staffDto.getStaffId())) {
                throw exception(STAFF_COMPANY_EMAIL_EXIST);
            }
        }

        Optional<Staff>  staff4= Optional.ofNullable(staffRepository.findByCompanyMobilePhone(staffDto.getCompanyMobilePhone()));
        if (staff4.isPresent()) {
            if(!staff4.get().getStaffId().equals(staffDto.getStaffId())) {
                throw exception(STAFF_COMPANY_MOBILE_PHONE_EXIST);
            }
        }

        Optional<Staff>  staff5= Optional.ofNullable(staffRepository.findByPersonalPhone(staffDto.getPersonalPhone()));
        if (staff5.isPresent()) {
            if(!staff5.get().getStaffId().equals(staffDto.getStaffId())) {
                throw exception(STAFF_PERSONAL_PHONE_EXIST);
            }
        }

        Optional<Staff>  staff6= Optional.ofNullable(staffRepository.findBySkype(staffDto.getSkype()));
        if (staff6.isPresent()) {
            if(!staff6.get().getStaffId().equals(staffDto.getStaffId())) {
                throw exception(STAFF_SKYPE_EXIST);
            }
        }

        address.setCity( cityRepository.findById(staffDto.getCityId()).get());
        Staff staff = staffRepository.findById(staffDto.getStaffId()).get();
        Staff staff7 = staffMapper.dtoToModel(staffDto);
        System.out.println(staff1);
        staff7.setAddress(addressRepository.save(address));
        staff7.setStaffContract(staff.getStaffContract());
        staff7.setStaffEconomicContractInformation(staff.getStaffEconomicContractInformation());
        staff7.setStaffDocuments(staff.getStaffDocuments());
        staff7.setIsFunctionalLeader(staff.getIsFunctionalLeader());
        staff7.setIsAdministrativeLeader(staff.getIsAdministrativeLeader());
        staff7.setFunctionalStructureLevels(staff.getFunctionalStructureLevels());
        staff7.setAdministrativeStructureLevels(staff.getAdministrativeStructureLevels());
        return staffRepository.save(staff7);
    }

    @Override
    public String deleteStaff(String staffId) {
        Optional<Staff> staff = staffRepository.findById(staffId);
        if (staff.isPresent()) {
            staffRepository.delete(staff.get());
            return "done";
        } else return "invalide to delete";

    }

    @Override
    public StaffDto getStaffById(String staffId) {
        Staff staff = staffRepository.findById(staffId).get();
        return staffToStaffDto(staff);
    }

    @Override
    public List<StaffDto> getAll() {
        // Get all actions
        List<Staff> staffs = staffRepository.findAll();
        // Create a list of all staff dto
        ArrayList<StaffDto> staffDtos = new ArrayList<>();
            for (Staff staff : staffs) {

                staffDtos.add(staffToStaffDto(staff));
            }
        return staffDtos;
    }

    @Override
    public List<StaffDto> getAllFunctionalNotAssignedStaffs() {
        // Get all actions
        List<Staff> staffs = staffRepository.findAllByFunctionalStructureLevelsEquals(new ArrayList<>());
        // Create a list of all staff dto
        ArrayList<StaffDto> staffDtos = new ArrayList<>();
        for (Staff staff : staffs) {

            staffDtos.add(staffToStaffDto(staff));
        }
        return staffDtos;
    }

    @Override
    public List<StaffDto> getAllAdministrativeNotAssignedStaffs() {
        // Get all actions
        List<Staff> staffs = staffRepository.findAllByAdministrativeStructureLevelsEquals(new ArrayList<>());
        // Create a list of all staff dto
        ArrayList<StaffDto> staffDtos = new ArrayList<>();
        for (Staff staff : staffs) {

            staffDtos.add(staffToStaffDto(staff));
        }
        return staffDtos;
    }

    @Override
    public List<StaffDto> getAllAdministrativeNotAssignedStaffsByCompany(String companyId) {
        // Get all actions
        FinancialCompany company = financialCompanyRepository.findById(companyId).get();
        List<StaffContract> staffContracts = staffContractRepository.findAllByCompany(company);
        List<Staff> staffs = new ArrayList<>();
        staffContracts.forEach(staffContract -> {
            List<Staff> staffs1 = staffRepository.findAllByAdministrativeStructureLevelsEqualsAndStaffContract(new ArrayList<>(), staffContract);
            staffs.addAll(staffs1);
        });
        FinancialCompany company1 = financialCompanyRepository.findByCode("100");
        if(company1 != null) {
            List<StaffContract> staffContracts1 = staffContractRepository.findAllByCompany(company1);
            staffContracts1.forEach(staffContract -> {
                List<Staff> staffs1 = staffRepository.findAllByAdministrativeStructureLevelsEqualsAndStaffContract(new ArrayList<>(), staffContract);
                staffs.addAll(staffs1);
            });
        }
        // Create a list of all staff dto
        ArrayList<StaffDto> staffDtos = new ArrayList<>();
        for (Staff staff : staffs) {

            staffDtos.add(staffToStaffDto(staff));
        }
        return staffDtos;
    }

    @Override
    public void assignFunctionalLevelToStaff(List<Object> objects) {
        ObjectMapper mapper = new ObjectMapper();
        FunctionalStructureLevelDto functionalStructureLevelDto = mapper.convertValue(objects.get(0), FunctionalStructureLevelDto.class);
        FunctionalStructureLevel functionalStructureLevel = functionalStructureLevelRepository.findById(functionalStructureLevelDto.getLevelId()).get();
        List<StaffDto> assignedStaffs = mapper.convertValue(objects.get(1), new TypeReference<List<StaffDto>>() { });
        List<StaffDto> notAssignedStaffs = mapper.convertValue(objects.get(2), new TypeReference<List<StaffDto>>() { });
        String startDate =  mapper.convertValue(objects.get(3), String.class);
        String endDate =  mapper.convertValue(objects.get(4), String.class);
        notAssignedStaffs.forEach(staffDto -> {
            Staff staff = staffRepository.findById(staffDto.getStaffId()).get();
            staff.setFunctionalStructureLevels(new ArrayList<>());
            staffRepository.save(staff);
            List<FunctionalStructureAssignationHistory> list = functionalStructureAssignationHistoryRepository.findAllByStaff(staff);
            if(list.size() > 0) {
                FunctionalStructureAssignationHistory functionalStructureAssignationHistory = list.get(list.size()-1);
                functionalStructureAssignationHistory.setEndDate(endDate);
                functionalStructureAssignationHistoryRepository.save(functionalStructureAssignationHistory);
            }
        });
        assignedStaffs.forEach(staffDto -> {
            List<FunctionalStructureLevel> levels = new ArrayList<>();
            levels.add(functionalStructureLevel);
            Staff staff = staffRepository.findById(staffDto.getStaffId()).get();
            staff.setFunctionalStructureLevels(levels);
            FunctionalStructureAssignationHistory functionalStructureAssignationHistory = new FunctionalStructureAssignationHistory();
            functionalStructureAssignationHistory.setStartDate(startDate);
            functionalStructureAssignationHistory.setEndDate("none");
            functionalStructureAssignationHistory.setLevel(functionalStructureLevel);
            functionalStructureAssignationHistory.setStaff(staffRepository.save(staff));
            functionalStructureAssignationHistoryRepository.save(functionalStructureAssignationHistory);
        });
    }

    @Override
    public void assignAdministrativeLevelToStaff(List<Object> objects) {
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeStructureLevelDto administrativeStructureLevelDto = mapper.convertValue(objects.get(0), AdministrativeStructureLevelDto.class);
        AdministrativeStructureLevel administrativeStructureLevel = administrativeStructureLevelRepository.findById(administrativeStructureLevelDto.getLevelId()).get();
        List<StaffDto> assignedStaffs = mapper.convertValue(objects.get(1), new TypeReference<List<StaffDto>>() { });
        List<StaffDto> notAssignedStaffs = mapper.convertValue(objects.get(2), new TypeReference<List<StaffDto>>() { });
        String startDate =  mapper.convertValue(objects.get(3), String.class);
        String endDate =  mapper.convertValue(objects.get(4), String.class);
        notAssignedStaffs.forEach(staffDto -> {
            Staff staff = staffRepository.findById(staffDto.getStaffId()).get();
            staff.setAdministrativeStructureLevels(new ArrayList<>());
            staffRepository.save(staff);
            List<AdministrativeStructureAssignationHistory> list = administrativeStructureAssignationHistoryRepository.findAllByStaff(staff);
            if(list.size() > 0) {
                AdministrativeStructureAssignationHistory administrativeStructureAssignationHistory = list.get(list.size()-1);
                administrativeStructureAssignationHistory.setEndDate(endDate);
                administrativeStructureAssignationHistoryRepository.save(administrativeStructureAssignationHistory);
            }
        });
        assignedStaffs.forEach(staffDto -> {
            List<AdministrativeStructureLevel> levels = new ArrayList<>();
            levels.add(administrativeStructureLevel);
            Staff staff = staffRepository.findById(staffDto.getStaffId()).get();
            staff.setAdministrativeStructureLevels(levels);
            AdministrativeStructureAssignationHistory administrativeStructureAssignationHistory = new AdministrativeStructureAssignationHistory();
            administrativeStructureAssignationHistory.setStartDate(startDate);
            administrativeStructureAssignationHistory.setEndDate("none");
            administrativeStructureAssignationHistory.setLevel(administrativeStructureLevel);
            administrativeStructureAssignationHistory.setStaff(staffRepository.save(staff));
            administrativeStructureAssignationHistoryRepository.save(administrativeStructureAssignationHistory);
        });
    }

    @Override
    public List<StaffDto> getStaffsByFunctionalLevel(String levelId, String isFunctionalLeader) {
        FunctionalStructureLevel level = functionalStructureLevelRepository.findById(levelId).get();
        // Get all actions
        List<Staff> staffs = staffRepository.findAllByFunctionalStructureLevelsContainingAndIsFunctionalLeader(level, isFunctionalLeader);
        // Create a list of all staff dto
        ArrayList<StaffDto> staffDtos = new ArrayList<>();
        for (Staff staff : staffs) {

            staffDtos.add(staffToStaffDto(staff));
        }
        return staffDtos;
    }

    @Override
    public List<StaffDto> getStaffsByAdministrativeLevel(String levelId, String isAdministrativeLeader) {
        AdministrativeStructureLevel level = administrativeStructureLevelRepository.findById(levelId).get();
        // Get all actions
        List<Staff> staffs = staffRepository.findAllByAdministrativeStructureLevelsContainingAndIsAdministrativeLeader(level, isAdministrativeLeader);
        // Create a list of all staff dto
        ArrayList<StaffDto> staffDtos = new ArrayList<>();
        for (Staff staff : staffs) {

            staffDtos.add(staffToStaffDto(staff));
        }
        return staffDtos;
    }

    @Override
    public List<StaffDto> getStaffsByIsFunctionalLeader(String isFunctionalLeader) {
        // Get all actions
        List<Staff> staffs = staffRepository.findAllByIsFunctionalLeader(isFunctionalLeader);
        System.out.println(staffs);
        // Create a list of all staff dto
        ArrayList<StaffDto> staffDtos = new ArrayList<>();
        for (Staff staff : staffs) {

            staffDtos.add(staffToStaffDto(staff));
        }
        return staffDtos;
    }

    @Override
    public List<StaffDto> getStaffsByIsAdministrativeLeader(String isAdministrativeLeader) {
        // Get all actions
        List<Staff> staffs = staffRepository.findAllByIsAdministrativeLeader(isAdministrativeLeader);
        System.out.println(staffs);
        // Create a list of all staff dto
        ArrayList<StaffDto> staffDtos = new ArrayList<>();
        for (Staff staff : staffs) {

            staffDtos.add(staffToStaffDto(staff));
        }
        return staffDtos;
    }

    public StaffDto staffToStaffDto(Staff staff) {
        StaffDto staffDto=staffMapper.modelToDto(staff);
        // Address
        staffDto.setAddressId(staff.getAddress().getAddressId());
        staffDto.setCityId(staff.getAddress().getCity().get_id());
        staffDto.setFullAddress(staff.getAddress().getFullAddress());
        staffDto.setCityName(staff.getAddress().getCity().getCityName());
        staffDto.setStateId(staff.getAddress().getCity().getStateCountry().get_id());
        staffDto.setStateName(staff.getAddress().getCity().getStateCountry().getStateName());
        staffDto.setCountryId(staff.getAddress().getCity().getStateCountry().getCountry().getCountryId());
        staffDto.setCountryName(staff.getAddress().getCity().getStateCountry().getCountry().getCountryName());
        staffDto.setPostCode(staff.getAddress().getPostCode());
        // Documentation
        staffDto.setStaffDocuments(staff.getStaffDocuments());
        // Contract
        staffDto.setStaffContractId(staff.getStaffContract().get_id());
        staffDto.setCompanyId(staff.getStaffContract().getCompany().get_id());
        staffDto.setCompanyName(staff.getStaffContract().getCompany().getName());
        staffDto.setAssociateOffice(staff.getStaffContract().getAssociateOffice());
        staffDto.setHiringCountry(staff.getStaffContract().getHiringCountry());
        staffDto.setTownContract(staff.getStaffContract().getTownContract());
        staffDto.setPersonalNumber(staff.getStaffContract().getPersonalNumber());
        staffDto.setHighDate(staff.getStaffContract().getHighDate());
        staffDto.setLowDate(staff.getStaffContract().getLowDate());
        staffDto.setRegistrationDate(staff.getStaffContract().getRegistrationDate());
        staffDto.setPreContractDate(staff.getStaffContract().getPreContractDate());
        staffDto.setContractTypeId(staff.getStaffContract().getContractType().get_id());
        staffDto.setContractTypeName(staff.getStaffContract().getContractType().getName());
        staffDto.setContractTypeCountryId(staff.getStaffContract().getContractType().getState().getCountry().getCountryId());
        staffDto.setContractTypeCountry(staff.getStaffContract().getContractType().getState().getCountry().getCountryName());
        staffDto.setContractTypeStateId(staff.getStaffContract().getContractType().getState().get_id());
        staffDto.setContractTypeState(staff.getStaffContract().getContractType().getState().getStateName());
        staffDto.setLegalCategoryTypeId(staff.getStaffContract().getLegalCategoryType().get_id());
        staffDto.setLegalCategoryTypeName(staff.getStaffContract().getLegalCategoryType().getName());
        staffDto.setContractModelId(staff.getStaffContract().getContractModel().get_id());
        staffDto.setContractModelName(staff.getStaffContract().getContractModel().getName());
        staffDto.setInternalRulesDoc(staff.getStaffContract().getInternalRulesDoc());
        staffDto.setContractDoc(staff.getStaffContract().getContractDoc());
        staffDto.setPreContractDoc(staff.getStaffContract().getPreContractDoc());
        // Economic contract
        staffDto.setStaffEconomicContractInformationId(staff.getStaffEconomicContractInformation().get_id());
        staffDto.setContractSalary(staff.getStaffEconomicContractInformation().getContractSalary());
        staffDto.setCompanyContractCost(staff.getStaffEconomicContractInformation().getCompanyContractCost());
        staffDto.setExpenses(staff.getStaffEconomicContractInformation().getExpenses());
        staffDto.setCompanyExpensesCost(staff.getStaffEconomicContractInformation().getCompanyExpensesCost());
        staffDto.setObjectives(staff.getStaffEconomicContractInformation().getObjectives());
        staffDto.setCompanyObjectivesCost(staff.getStaffEconomicContractInformation().getCompanyObjectivesCost());
        staffDto.setTotalCompanyCost(staff.getStaffEconomicContractInformation().getTotalCompanyCost());
        staffDto.setContractSalaryDateGoing(staff.getStaffEconomicContractInformation().getContractSalaryDateGoing());
        staffDto.setContractSalaryDateOut(staff.getStaffEconomicContractInformation().getContractSalaryDateOut());
        staffDto.setCompanyContractCostDateGoing(staff.getStaffEconomicContractInformation().getCompanyContractCostDateGoing());
        staffDto.setCompanyContractCostDateOut(staff.getStaffEconomicContractInformation().getCompanyContractCostDateOut());
        staffDto.setExpensesDateGoing(staff.getStaffEconomicContractInformation().getExpensesDateGoing());
        staffDto.setExpensesDateOut(staff.getStaffEconomicContractInformation().getExpensesDateOut());
        staffDto.setCompanyExpensesCostDateGoing(staff.getStaffEconomicContractInformation().getCompanyExpensesCostDateGoing());
        staffDto.setCompanyExpensesCostDateOut(staff.getStaffEconomicContractInformation().getCompanyExpensesCostDateOut());
        staffDto.setObjectivesDateGoing(staff.getStaffEconomicContractInformation().getObjectivesDateGoing());
        staffDto.setObjectivesDateOut(staff.getStaffEconomicContractInformation().getObjectivesDateOut());
        staffDto.setCompanyObjectivesCostDateGoing(staff.getStaffEconomicContractInformation().getCompanyObjectivesCostDateGoing());
        staffDto.setCompanyObjectivesCostDateOut(staff.getStaffEconomicContractInformation().getCompanyObjectivesCostDateOut());
        staffDto.setTotalCompanyCostDateGoing(staff.getStaffEconomicContractInformation().getTotalCompanyCostDateGoing());
        staffDto.setTotalCompanyCostDateOut(staff.getStaffEconomicContractInformation().getTotalCompanyCostDateOut());
        staffDto.setCurrencyId(staff.getStaffEconomicContractInformation().getCurrency().get_id());
        staffDto.setCurrencyCode(staff.getStaffEconomicContractInformation().getCurrency().getCurrencyCode());
        staffDto.setCurrencyName(staff.getStaffEconomicContractInformation().getCurrency().getCurrencyName());
        staffDto.setCurrencyMonth(staff.getStaffEconomicContractInformation().getCurrency().getMonth());
        staffDto.setCurrencyYear(staff.getStaffEconomicContractInformation().getCurrency().getYear());
        staffDto.setChangeFactor(staff.getStaffEconomicContractInformation().getCurrency().getChangeFactor());

        // Functional Structure Level
        staffDto.setFunctionalStructureLevels(staff.getFunctionalStructureLevels());

        // Administrative Structure Level
        staffDto.setAdministrativeStructureLevels(staff.getAdministrativeStructureLevels());
        return  staffDto;

    }

    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Staff, exceptionType, args);
    }

}
