package org.techniu.isbackend.service;

import com.wproducts.administration.controller.request.UserAddRequest;
import org.techniu.isbackend.dto.model.StaffDto;
import org.techniu.isbackend.entity.*;

import java.util.List;

public interface StaffService {
    String deleteStaff(String staffId);
    StaffDto getStaffById( String staffId);
    StaffDto getStaffByCompanyEmail(String companyEmail);
    List<StaffDto> getAll();
    List<StaffDto> getAllFunctionalNotAssignedStaffs();
    List<StaffDto> getAllAdministrativeNotAssignedStaffs();
    List<StaffDto> getAllAdministrativeNotAssignedStaffsByCompany(String companyId);
    List<StaffDto> getAllStaffsByCompany(String companyId);
    void assignFunctionalLevelToStaff(List<Object> objects);
    void assignAdministrativeLevelToStaff(List<Object> objects);
    List<StaffDto> getStaffsByFunctionalLevel(String levelId, String isFunctionalLeader);
    List<StaffDto> getStaffsByAdministrativeLevel(String levelId, String isAdministrativeLeader);
    List<StaffDto> getStaffsByIsFunctionalLeader(String isFunctionalLeader);
    List<StaffDto> getStaffsByIsAdministrativeLeader(String isAdministrativeLeader);
    Staff save(StaffDto staffDto, Address address, StaffEconomicContractInformation staffEconomicContractInformation, StaffContract staffContract, List<StaffDocument> staffDocumentList);
    Staff update(StaffDto staffDto, Address address);
    void saveUser(UserAddRequest userAddRequest);
    void sendPassword(String email);

}
