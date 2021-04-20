package org.techniu.isbackend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.controller.request.AdministrativeStructureLevelAddrequest;
import org.techniu.isbackend.dto.mapper.AdministrativeStructureLevelMapper;
import org.techniu.isbackend.dto.model.AdministrativeStructureLevelDto;
import org.techniu.isbackend.dto.model.StaffDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdministrativeStructureLevelServiceImpl implements AdministrativeStructureLevelService {
    private AdministrativeStructureLevelRepository administrativeStructureLevelRepository;
    private AdministrativeStructureAssignationHistoryRepository administrativeStructureAssignationHistoryRepository;
    private StaffRepository staffRepository;
    private LogService logService;
    private StaffContractRepository staffContractRepository;
    private FinancialCompanyRepository financialCompanyRepository;
    private final AdministrativeStructureLevelMapper administrativeStructureLevelMapper = Mappers.getMapper(AdministrativeStructureLevelMapper.class);
    AdministrativeStructureLevelServiceImpl(
            AdministrativeStructureLevelRepository administrativeStructureLevelRepository,
            AdministrativeStructureAssignationHistoryRepository administrativeStructureAssignationHistoryRepository,
            StaffRepository staffRepository,
            LogService logService, StaffContractRepository staffContractRepository,
            FinancialCompanyRepository financialCompanyRepository) {
        this.administrativeStructureLevelRepository = administrativeStructureLevelRepository;
        this.staffRepository = staffRepository;
        this.logService = logService;
        this.staffContractRepository = staffContractRepository;
        this.financialCompanyRepository = financialCompanyRepository;
        this.administrativeStructureAssignationHistoryRepository = administrativeStructureAssignationHistoryRepository;
    }
    @Override
    public Boolean save(List<Object> objects) {
        ObjectMapper mapper = new ObjectMapper();
        List<AdministrativeStructureLevel> list = new ArrayList<>();
        List<StaffDto> leaders  = mapper.convertValue(objects.get(0), new TypeReference<List<StaffDto>>() { });
        for (int i=1; i < objects.size(); i++) {
            AdministrativeStructureLevelAddrequest levelAddrequest = mapper.convertValue(objects.get(i), AdministrativeStructureLevelAddrequest.class);
            AdministrativeStructureLevelDto levelDto = administrativeStructureLevelMapper.addRequestToDto(levelAddrequest);
            FinancialCompany company = financialCompanyRepository.findById(levelAddrequest.getCompanyId()).get();
            AdministrativeStructureLevel lvl = administrativeStructureLevelRepository.findByName(levelDto.getName());
            if (lvl == null) {
                StaffDto staffDto = leaders.get(i-1);
                Staff staff1 = staffRepository.findById(staffDto.getStaffId()).get();
                staff1.setIsAdministrativeLeader("yes");
                AdministrativeStructureLevel administrativeStructureLevel = administrativeStructureLevelMapper.dtoToModel(levelDto);
                administrativeStructureLevel.setCompany(company);
                lvl = administrativeStructureLevelRepository.save(administrativeStructureLevel);
                logService.addLog(LogType.CREATE, ClassType.administrativeStructureLevel,"create administrative structure level "+lvl.getName());
                List<AdministrativeStructureLevel> levels = staff1.getAdministrativeStructureLevels();
                levels.add(lvl);
                staff1.setAdministrativeStructureLevels(levels);
                //System.out.println(staff1);
                staffRepository.save(staff1);
                logService.addLog(LogType.UPDATE, ClassType.STAFF,"update administrative structure level of staff "+staff1.getMotherFamilyName()+" "+staff1.getFatherFamilyName()+" "+staff1.getFirstName());
            }
            if(lvl.getType().equals("Level 1")) {
                List<StaffContract> staffContracts = staffContractRepository.findAllByCompany(company);
                List<Staff> assignedStaffs = new ArrayList<>();
                staffContracts.forEach(staffContract -> {
                    List<Staff> staffs1 = staffRepository.findAllByAdministrativeStructureLevelsEqualsAndStaffContract(new ArrayList<>(), staffContract);
                    assignedStaffs.addAll(staffs1);
                });
                AdministrativeStructureLevel finalLvl = lvl;
                assignedStaffs.forEach(staffDto -> {
                    List<AdministrativeStructureLevel> levels = new ArrayList<>();
                    levels.add(finalLvl);
                    Staff staff = staffRepository.findById(staffDto.getStaffId()).get();
                    staff.setAdministrativeStructureLevels(levels);
                    AdministrativeStructureAssignationHistory administrativeStructureAssignationHistory = new AdministrativeStructureAssignationHistory();
                    administrativeStructureAssignationHistory.setStartDate(new Date().toInstant().toString().substring(0,10));
                    administrativeStructureAssignationHistory.setEndDate("none");
                    administrativeStructureAssignationHistory.setLevel(finalLvl);
                    administrativeStructureAssignationHistory.setStaff(staffRepository.save(staff));
                    administrativeStructureAssignationHistoryRepository.save(administrativeStructureAssignationHistory);
                    //System.out.println("staffDto related");
                    //System.out.println(staffDto.getFirstName());
                });
            }
            list.add(lvl);
        }
        for (int i=list.size()-1; i > 0; i--) {
            AdministrativeStructureLevel level = list.get(i-1);
            List<AdministrativeStructureLevel> childs = new ArrayList<>();
            if(level.getChilds() == null){
                childs.add(list.get(i));
            }
            else {
                childs = level.getChilds();
                boolean exist = false;
                for (int j = 0; j < childs.size(); j++) {

                    if (level.getChilds().get(j).get_id().equals(list.get(i).get_id())) {
                        exist = true;
                        break;
                    }
                }
                if(!exist) {
                    childs.add(list.get(i));
                }
            }
            level.setChilds(childs);
            AdministrativeStructureLevel administrativeStructureLevel = administrativeStructureLevelRepository.save(level);
            logService.addLog(LogType.CREATE, ClassType.administrativeStructureLevel,"create administrative structure level "+level.getName());
            list.set(i-1, administrativeStructureLevel);
        }
        return true;
    }

    @Override
    public AdministrativeStructureLevel update(AdministrativeStructureLevelDto administrativeStructureLevelDto, String oldLeaderId, String newLeaderId) {
        Staff oldLeader = staffRepository.findById(oldLeaderId).get();
        Staff newLeader = staffRepository.findById(newLeaderId).get();
        AdministrativeStructureLevel administrativeStructureLevel = administrativeStructureLevelRepository.findById(administrativeStructureLevelDto.getLevelId()).get();
        if(oldLeader != null) {
            List<AdministrativeStructureLevel> levels = oldLeader.getAdministrativeStructureLevels();
            levels.remove(administrativeStructureLevel);
            oldLeader.setAdministrativeStructureLevels(levels);
            if(levels.size() == 0) {
                oldLeader.setIsAdministrativeLeader("no");
            }
            staffRepository.save(oldLeader);
        }
        AdministrativeStructureLevel administrativeStructureLevel1 = administrativeStructureLevelMapper.dtoToModel(administrativeStructureLevelDto);
        administrativeStructureLevel1.setChilds(administrativeStructureLevel.getChilds());
        List<AdministrativeStructureLevel> levels = newLeader.getAdministrativeStructureLevels();
        levels.add(administrativeStructureLevelRepository.save(administrativeStructureLevel1));
        newLeader.setAdministrativeStructureLevels(levels);
        newLeader.setIsAdministrativeLeader("yes");
        staffRepository.save(newLeader);
        return null;
    }

    @Override
    public AdministrativeStructureLevel getLevelByName(String name) {
        return this.administrativeStructureLevelRepository.findByName(name);
    }

    @Override
    public ResponseEntity<?> remove(String levelId) {
        AdministrativeStructureLevel level = administrativeStructureLevelRepository.findById(levelId).get();
        if(level.getChilds() != null) {
            List<AdministrativeStructureLevel> list1 = level.getChilds();
            list1.forEach(level2 -> {
                if(level2.getChilds() != null ) {
                    List<AdministrativeStructureLevel> list2 = level2.getChilds();
                    list2.forEach(level3 -> {
                        List<Staff> staffs = staffRepository.findAllByAdministrativeStructureLevelsContainingAndIsAdministrativeLeader(level3, "no");
                        staffs.addAll(staffRepository.findAllByAdministrativeStructureLevelsContainingAndIsAdministrativeLeader(level3, "yes"));
                        staffs.forEach(staff -> {
                            List<AdministrativeStructureLevel> levels = staff.getAdministrativeStructureLevels();
                            levels.remove(level3);
                            if(levels.size() == 0) {
                                staff.setIsAdministrativeLeader("no");
                            }
                            staff.setAdministrativeStructureLevels(levels);
                            staffRepository.save(staff);
                        });
                        administrativeStructureLevelRepository.delete(level3);
                    });
                }
                List<Staff> staffs = staffRepository.findAllByAdministrativeStructureLevelsContainingAndIsAdministrativeLeader(level2, "no");
                staffs.addAll(staffRepository.findAllByAdministrativeStructureLevelsContainingAndIsAdministrativeLeader(level2, "yes"));
                staffs.forEach(staff -> {
                    List<AdministrativeStructureLevel> levels = staff.getAdministrativeStructureLevels();
                    levels.remove(level2);
                    if(levels.size() == 0) {
                        staff.setIsAdministrativeLeader("no");
                    }
                    staff.setAdministrativeStructureLevels(levels);
                    staffRepository.save(staff);
                });
                administrativeStructureLevelRepository.delete(level2);
            });
        }
        List<Staff> staffs = staffRepository.findAllByAdministrativeStructureLevelsContainingAndIsAdministrativeLeader(level, "no");
        staffs.addAll(staffRepository.findAllByAdministrativeStructureLevelsContainingAndIsAdministrativeLeader(level, "yes"));
        staffs.forEach(staff -> {
            List<AdministrativeStructureLevel> levels = staff.getAdministrativeStructureLevels();
            levels.remove(level);
            if(levels.size() == 0) {
                staff.setIsAdministrativeLeader("no");
            }
            staff.setAdministrativeStructureLevels(levels);
            staffRepository.save(staff);
        });
        AdministrativeStructureLevel parent = administrativeStructureLevelRepository.findByChildsContaining(level);
        if(parent != null) {
            parent.setChilds(null);
            administrativeStructureLevelRepository.save(parent);
        }
        administrativeStructureLevelRepository.delete(level);
        return null;
    }

    @Override
    public List<AdministrativeStructureLevelDto> getAll() {
        List<AdministrativeStructureLevel> administrativeStructureLevels = administrativeStructureLevelRepository.findAll();
        // Create a list of all actions dto
        List<AdministrativeStructureLevelDto> administrativeStructureLevelDtos = new ArrayList<>();

        for (AdministrativeStructureLevel level : administrativeStructureLevels) {
            AdministrativeStructureLevelDto administrativeStructureLevelDto=administrativeStructureLevelMapper.modelToDto(level);
            administrativeStructureLevelDto.setCompanyId(level.getCompany().get_id());
            administrativeStructureLevelDto.setCompanyName(level.getCompany().getName());
            List<AdministrativeStructureLevel> administrativeStructureLevels1 = level.getChilds();
            if(administrativeStructureLevels1 != null){
                List<AdministrativeStructureLevelDto> administrativeStructureLevelDtos1 = new ArrayList<>();
                for (AdministrativeStructureLevel level1 : administrativeStructureLevels1) {
                    AdministrativeStructureLevelDto administrativeStructureLevelDto1 = administrativeStructureLevelMapper.modelToDto(level1);
                    administrativeStructureLevelDto1.setCompanyId(level1.getCompany().get_id());
                    administrativeStructureLevelDto1.setCompanyName(level1.getCompany().getName());
                    List<AdministrativeStructureLevel> administrativeStructureLevels2 = level1.getChilds();
                    List<AdministrativeStructureLevelDto> administrativeStructureLevelDtos2 = new ArrayList<>();
                    if (administrativeStructureLevels2 != null) {
                        for (AdministrativeStructureLevel level2 : administrativeStructureLevels2) {
                            AdministrativeStructureLevelDto administrativeStructureLevelDto2 = administrativeStructureLevelMapper.modelToDto(level2);
                            administrativeStructureLevelDto2.setCompanyId(level2.getCompany().get_id());
                            administrativeStructureLevelDto2.setCompanyName(level2.getCompany().getName());
                            administrativeStructureLevelDtos2.add(administrativeStructureLevelDto2);
                        }
                        administrativeStructureLevelDto1.setChilds(administrativeStructureLevelDtos2);
                    }
                    administrativeStructureLevelDtos1.add(administrativeStructureLevelDto1);
                }
                administrativeStructureLevelDto.setChilds(administrativeStructureLevelDtos1);
            }
            administrativeStructureLevelDtos.add(administrativeStructureLevelDto);
        }
        return administrativeStructureLevelDtos;
    }

    @Override
    public List<AdministrativeStructureLevelDto> getAllByType(String type) {
        List<AdministrativeStructureLevel> administrativeStructureLevels = administrativeStructureLevelRepository.findByType(type);
        // Create a list of all actions dto
        List<AdministrativeStructureLevelDto> administrativeStructureLevelDtos = new ArrayList<>();

        for (AdministrativeStructureLevel level : administrativeStructureLevels) {
            AdministrativeStructureLevelDto administrativeStructureLevelDto=administrativeStructureLevelMapper.modelToDto(level);
            administrativeStructureLevelDto.setCompanyId(level.getCompany().get_id());
            administrativeStructureLevelDto.setCompanyName(level.getCompany().getName());
            List<AdministrativeStructureLevel> administrativeStructureLevels1 = level.getChilds();
            if(administrativeStructureLevels1 != null){
                List<AdministrativeStructureLevelDto> administrativeStructureLevelDtos1 = new ArrayList<>();
                for (AdministrativeStructureLevel level1 : administrativeStructureLevels1) {
                    AdministrativeStructureLevelDto administrativeStructureLevelDto1 = administrativeStructureLevelMapper.modelToDto(level1);
                    administrativeStructureLevelDto1.setCompanyId(level1.getCompany().get_id());
                    administrativeStructureLevelDto1.setCompanyName(level1.getCompany().getName());
                    List<AdministrativeStructureLevel> administrativeStructureLevels2 = level1.getChilds();
                    List<AdministrativeStructureLevelDto> administrativeStructureLevelDtos2 = new ArrayList<>();
                    if (administrativeStructureLevels2 != null) {
                        for (AdministrativeStructureLevel level2 : administrativeStructureLevels2) {
                            AdministrativeStructureLevelDto administrativeStructureLevelDto2 = administrativeStructureLevelMapper.modelToDto(level2);
                            administrativeStructureLevelDto2.setCompanyId(level2.getCompany().get_id());
                            administrativeStructureLevelDto2.setCompanyName(level2.getCompany().getName());
                            administrativeStructureLevelDtos2.add(administrativeStructureLevelDto2);
                        }
                        administrativeStructureLevelDto1.setChilds(administrativeStructureLevelDtos2);
                    }
                    administrativeStructureLevelDtos1.add(administrativeStructureLevelDto1);
                }
                administrativeStructureLevelDto.setChilds(administrativeStructureLevelDtos1);
            }
            administrativeStructureLevelDtos.add(administrativeStructureLevelDto);
        }
        return administrativeStructureLevelDtos;
    }

    @Override
    public List<Staff> setLevelStaffs(List<Object> objects) {
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeStructureLevel administrativeStructureLevel = mapper.convertValue(objects.get(0), AdministrativeStructureLevel.class);
        List<Staff> staffs = mapper.convertValue(objects.get(1), new TypeReference<List<Staff>>(){});
        Optional<AdministrativeStructureLevel> level = administrativeStructureLevelRepository.findById(administrativeStructureLevel.get_id());
        /*if(level.isPresent()) {
            AdministrativeStructureLevel level1 = level.get();
            List<Staff> list = new ArrayList<>();
            staffs.forEach(staff -> {
                Staff staff1 = staffRepository.findById(staff.getStaffId()).get();
                staff1.setLevel(level1);
                list.add(staff1);
                staffRepository.save(staff1);
            });
            level1.setStaffs(list);
            administrativeStructureLevelRepository.save(level1);
        }*/
        return null;
    }

    @Override
    public List<AdministrativeStructureLevel> getAdministrativeStructureTree(String levelId) {
        AdministrativeStructureLevel level = administrativeStructureLevelRepository.findById(levelId).get();
        List<AdministrativeStructureLevel> list = new ArrayList<>();
        if(level.getType().equals("Level 1")) {
            list.add(level);
        } else if(level.getType().equals("Level 2")) {
            AdministrativeStructureLevel level1 = administrativeStructureLevelRepository.findByChildsContaining(level);
            list.add(level1);
            list.add(level);
        } else if(level.getType().equals("Level 3")) {
            AdministrativeStructureLevel level2 = administrativeStructureLevelRepository.findByChildsContaining(level);
            AdministrativeStructureLevel level1 = administrativeStructureLevelRepository.findByChildsContaining(level2);
            list.add(level1);
            list.add(level2);
            list.add(level);
        }
        return list;
    }
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.AdministrativeStructureLevel, exceptionType, args);
    }
}
