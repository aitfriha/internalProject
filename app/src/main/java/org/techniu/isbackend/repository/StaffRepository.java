package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.*;

import java.util.List;
@Repository
public interface StaffRepository extends MongoRepository<Staff, String> {

    //List<Staff> findAllByFunctionalStructureLevel(FunctionalStructureLevel functionalStructureLevel);

    List<Staff> findAllByFunctionalStructureLevelsContaining(FunctionalStructureLevel functionalStructureLevel);

    List<Staff> findAllByFunctionalStructureLevelsEquals(List<FunctionalStructureLevel> list);

    List<Staff> findAllByFunctionalStructureLevelsContainingAndIsFunctionalLeader(FunctionalStructureLevel level, String isFunctionalLeader);

    List<Staff> findAllByIsFunctionalLeader(String isFunctionalLeader);

    List<Staff> findAllByAdministrativeStructureLevelsEquals(List<AdministrativeStructureLevel> list);

    List<Staff> findAllByAdministrativeStructureLevelsContainingAndIsAdministrativeLeader(AdministrativeStructureLevel level, String isAdministrativeLeader);

    List<Staff> findAllByIsAdministrativeLeader(String isAdministrativeLeader);

    List<Staff> findAllByAdministrativeStructureLevelsEqualsAndStaffContract(List<AdministrativeStructureLevel> list, StaffContract staffContract);

    List<Staff> findAllByStaffContract(StaffContract staffContract);

    Staff findByStaffEconomicContractInformation(StaffEconomicContractInformation staffEconomicContractInformation);

    Staff findByFirstNameAndFatherFamilyNameAndMotherFamilyName(String firstName, String fatherFamilyName, String motherFamilyName);

    Staff findByPersonalEmail(String personalEmail);

    Staff findByCompanyEmail(String companyEmail);

    Staff findByCompanyMobilePhone(String companyMobilePhone);

    Staff findByPersonalPhone(String personalPhone);

    Staff findBySkype(String skype);

    Staff findAllByStaffId(String id);

    Staff findByStaffDocumentsContaining(StaffDocument staffDocument);

    Staff findByAndFirstNameAndFatherFamilyName(String firstName,String fatherFamilyName);
    Staff findByAndFirstNameAndFatherFamilyNameAndMotherFamilyName(String firstName,String fatherFamilyName,String motherFamilyName);
}
