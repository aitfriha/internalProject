package org.techniu.isbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.model.SectorCompanyDto;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.Client;
import org.techniu.isbackend.entity.LogType;
import org.techniu.isbackend.entity.SectorCompany;

import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.ClientRepository;
import org.techniu.isbackend.repository.SectorCompanyRepository;
import static org.techniu.isbackend.exception.ExceptionType.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SectorCompanyServiceImpl implements SectorCompanyService {
    private SectorCompanyRepository sectorCompanyRepository;
    private ClientRepository clientRepository;
    private ClientService clientService;
    private LogService logService;
    SectorCompanyServiceImpl(SectorCompanyRepository sectorCompanyRepository, ClientRepository clientRepository, ClientService clientService, LogService logService) {
        this.sectorCompanyRepository = sectorCompanyRepository;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
        this.logService = logService;
    }

    @Override
    public SectorCompany save(SectorCompany sectorCompany) {
       /* SectorCompany secondSector = sectorCompanyRepository.findByName(sectorCompany.getName());
        if(secondSector != null)
        {
            return secondSector;
        }*/
        logService.addLog(LogType.CREATE, ClassType.SECTOR,"create sector "+sectorCompany.getName());
        return sectorCompanyRepository.save(sectorCompany);
    }

    @Override
    public SectorCompany checkIfSectorExist(String sectorCompanyName) {
        SectorCompany secondSector = sectorCompanyRepository.findByName(sectorCompanyName);
        if (secondSector != null) {
            return secondSector;
        }
        return null;
    }

    @Override
    public SectorCompany update(SectorCompany sectorCompany) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(String sectorId) {
        return null;
    }

    @Override
    public List<SectorCompanyDto> getAll() {
        // Get all SectorCompany
        List<SectorCompany> SectorCompanys = sectorCompanyRepository.findAll();
        // Create a list of all actions dto
        ArrayList<SectorCompanyDto> sectorCompanyDtos = new ArrayList<>();

        for (SectorCompany sectorCompany : SectorCompanys) {
            if (sectorCompany.getParent() == null) {
                List<SectorCompany> secondSectors = sectorCompanyRepository.findByParent(sectorCompany);
                System.out.println(secondSectors);
                if (!secondSectors.isEmpty()) {
                    for (SectorCompany secondsectorCompany : secondSectors) {
                        List<SectorCompany> thirdSectors = sectorCompanyRepository.findByParent(secondsectorCompany);
                        if (!thirdSectors.isEmpty()) {
                            for (SectorCompany thirdsectorCompany : thirdSectors) {
                                SectorCompanyDto sectorCompanyDto = new SectorCompanyDto();
                                sectorCompanyDto.setFirstSectorId(sectorCompany.get_id());
                                sectorCompanyDto.setFirstSectorName(sectorCompany.getName());
                                sectorCompanyDto.setFirstSectorDescription(sectorCompany.getDescription());

                                sectorCompanyDto.setSecondSectorId(secondsectorCompany.get_id());
                                sectorCompanyDto.setSecondSectorName(secondsectorCompany.getName());
                                sectorCompanyDto.setSecondSectorDescription(secondsectorCompany.getDescription());

                                sectorCompanyDto.setThirdSectorId(thirdsectorCompany.get_id());
                                sectorCompanyDto.setThirdSectorName(thirdsectorCompany.getName());
                                sectorCompanyDto.setThirdSectorDescription(thirdsectorCompany.getDescription());
                                sectorCompanyDtos.add(sectorCompanyDto);
                            }
                        } else {
                            SectorCompanyDto sectorCompanyDto = new SectorCompanyDto();
                            sectorCompanyDto.setFirstSectorId(sectorCompany.get_id());
                            sectorCompanyDto.setFirstSectorName(sectorCompany.getName());
                            sectorCompanyDto.setFirstSectorDescription(sectorCompany.getDescription());

                            sectorCompanyDto.setSecondSectorId(secondsectorCompany.get_id());
                            sectorCompanyDto.setSecondSectorName(secondsectorCompany.getName());
                            sectorCompanyDto.setSecondSectorDescription(secondsectorCompany.getDescription());
                            sectorCompanyDtos.add(sectorCompanyDto);
                        }
                    }
                } else {
                    SectorCompanyDto sectorCompanyDto = new SectorCompanyDto();
                    sectorCompanyDto.setFirstSectorId(sectorCompany.get_id());
                    sectorCompanyDto.setFirstSectorName(sectorCompany.getName());
                    sectorCompanyDto.setFirstSectorDescription(sectorCompany.getDescription());
                    sectorCompanyDtos.add(sectorCompanyDto);
                }
            }

        }
        return sectorCompanyDtos;
    }

    @Override
    public List<SectorCompanyDto> getAllBysectorByParent(String sectorName) {
        ArrayList<SectorCompanyDto> sectorCompanyDtos = new ArrayList<>();
        List<SectorCompany> SectorCompanys = sectorCompanyRepository.findByParent(sectorCompanyRepository.findByName(sectorName));

        for (SectorCompany sectorCompany : SectorCompanys) {
            SectorCompanyDto sectorCompanyDto = new SectorCompanyDto();
            sectorCompanyDto.setSecondSectorId(sectorCompany.get_id());
            sectorCompanyDto.setSecondSectorName(sectorCompany.getName());
            sectorCompanyDto.setSecondSectorDescription(sectorCompany.getDescription());
           /* List<SectorCompany> thirdSectors = sectorCompanyRepository.findByParent(sectorCompany);
            if (thirdSectors != null) {
                for (SectorCompany thirdsectorCompany : thirdSectors) {
                    sectorCompanyDto.setThirdSectorId(thirdsectorCompany.get_id());
                    sectorCompanyDto.setThirdSectorName(thirdsectorCompany.getName());
                    sectorCompanyDto.setThirdSectorDescription(thirdsectorCompany.getDescription());
                }
            }*/
            sectorCompanyDtos.add(sectorCompanyDto);
        }

        return sectorCompanyDtos;
    }

    @Override
    public List<SectorCompanyDto> getSectorsPrimary() {
        // Get all SectorCompany
        List<SectorCompany> SectorCompanys = sectorCompanyRepository.findAll();
        // Create a list of all actions dto
        ArrayList<SectorCompanyDto> sectorCompanyDtos = new ArrayList<>();

        for (SectorCompany sectorCompany : SectorCompanys) {
            if (sectorCompany.getParent() == null) {
                SectorCompanyDto sectorCompanyDto = new SectorCompanyDto();
                sectorCompanyDto.setFirstSectorId(sectorCompany.get_id());
                sectorCompanyDto.setFirstSectorName(sectorCompany.getName());
                sectorCompanyDto.setFirstSectorDescription(sectorCompany.getDescription());
                sectorCompanyDtos.add(sectorCompanyDto);
            }
        }
           return sectorCompanyDtos;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.SectorCompany, exceptionType, args);
    }

    @Override
    public void remove(String firstSectorName, String secondSectorName, String thirdSectorName) {
        List<Client> client=clientRepository.findBySector1OrSector2OrSector3(firstSectorName,secondSectorName,thirdSectorName);
        if(client.size() != 0){
            throw exception(SECTOR_RELATED_TO_CLIENT);
        }
      /*  else {
            if (!thirdSectorName.equals("null") && !secondSectorName.equals("null")) {
                SectorCompany secondSector3 = sectorCompanyRepository.findByName(thirdSectorName);
                SectorCompany secondSector2 = secondSector3.getParent();
                //liste de sector 3
                List<SectorCompany> sectorCompanys3 = sectorCompanyRepository.findByParent(secondSector2);
                if (sectorCompanys3.size() == 1) {
                    SectorCompany secondSector1 = sectorCompanys3.get(0).getParent().getParent();
                    System.out.println("secondSector1 " + secondSector1);
                    List<SectorCompany> sectorCompanys2 = sectorCompanyRepository.findByParent(secondSector1);
                    if (sectorCompanys2.size() == 1) {
                        sectorCompanyRepository.delete(secondSector3);
                        sectorCompanyRepository.delete(secondSector2);
                        sectorCompanyRepository.delete(secondSector1);
                    } else {
                        sectorCompanyRepository.delete(secondSector3);
                    }
                } else {
                    sectorCompanyRepository.delete(secondSector3);
                }
            }
            //when thirdSectorName is null
            if (thirdSectorName.equals("null") && !secondSectorName.equals("null")) {
                System.out.println(secondSectorName);
                SectorCompany secondSector2 = sectorCompanyRepository.findByName(secondSectorName);
                SectorCompany secondSector1 = secondSector2.getParent();
                //liste de sector 2 ho has secondSector1 parent
                List<SectorCompany> sectorCompanys2 = sectorCompanyRepository.findByParent(secondSector1);
                System.out.println(sectorCompanys2.size());
                if (sectorCompanys2.size() == 1) {
                    sectorCompanyRepository.delete(secondSector2);
                    sectorCompanyRepository.delete(secondSector1);
                } else {
                    sectorCompanyRepository.delete(secondSector2);
                }
            }
            //when secondSectorName is null
            if (secondSectorName.equals("null")) {
                System.out.println(secondSectorName);
                SectorCompany secondSector2 = sectorCompanyRepository.findByName(firstSectorName);
                sectorCompanyRepository.delete(secondSector2);
            }
        }*/
    }

    @Override
    public void removeConfirmation(String firstSectorName, String secondSectorName, String thirdSectorName) {
        List<Client> client=clientRepository.findBySector1OrSector2OrSector3(firstSectorName,secondSectorName,thirdSectorName);
            if (!thirdSectorName.equals("null") && !secondSectorName.equals("null")) {
                SectorCompany secondSector3 = sectorCompanyRepository.findByName(thirdSectorName);
                SectorCompany secondSector2 = secondSector3.getParent();
                //liste de sector 3
                List<SectorCompany> sectorCompanys3 = sectorCompanyRepository.findByParent(secondSector2);
                if (sectorCompanys3.size() == 1) {
                    SectorCompany secondSector1 = sectorCompanys3.get(0).getParent().getParent();
                    List<SectorCompany> sectorCompanys2 = sectorCompanyRepository.findByParent(secondSector1);
                    if (sectorCompanys2.size() == 1) {
                        clientService.deleteSectorFromclient(secondSector1,secondSector2,secondSector3);
                        sectorCompanyRepository.delete(secondSector3);
                        logService.addLog(LogType.DELETE, ClassType.SECTOR,"delete sector "+secondSector3.getName());
                        sectorCompanyRepository.delete(secondSector2);
                        logService.addLog(LogType.DELETE, ClassType.SECTOR,"delete sector "+secondSector2.getName());
                        sectorCompanyRepository.delete(secondSector1);
                        logService.addLog(LogType.DELETE, ClassType.SECTOR,"delete sector "+secondSector1.getName());

                    } else {
                        clientService.deleteSectorFromclient(null,null,secondSector3);
                        sectorCompanyRepository.delete(secondSector3);
                        logService.addLog(LogType.DELETE, ClassType.SECTOR,"delete sector "+secondSector3.getName());
                    }
                } else {
                    clientService.deleteSectorFromclient(null,null,secondSector3);
                    sectorCompanyRepository.delete(secondSector3);
                    logService.addLog(LogType.DELETE, ClassType.SECTOR,"delete sector "+secondSector3.getName());
                }
            }
            //when thirdSectorName is null
            if (thirdSectorName.equals("null") && !secondSectorName.equals("null")) {
                SectorCompany secondSector2 = sectorCompanyRepository.findByName(secondSectorName);
                SectorCompany secondSector1 = secondSector2.getParent();
                //liste de sector 2 ho has secondSector1 parent
                List<SectorCompany> sectorCompanys2 = sectorCompanyRepository.findByParent(secondSector1);
                if (sectorCompanys2.size() == 1) {
                    clientService.deleteSectorFromclient(secondSector1,secondSector2,null);
                    sectorCompanyRepository.delete(secondSector2);
                    logService.addLog(LogType.DELETE, ClassType.SECTOR,"delete sector "+secondSector2.getName());
                    sectorCompanyRepository.delete(secondSector1);
                    logService.addLog(LogType.DELETE, ClassType.SECTOR,"delete sector "+secondSector1.getName());
                } else {
                    clientService.deleteSectorFromclient(null,secondSector2,null);
                    sectorCompanyRepository.delete(secondSector2);
                    logService.addLog(LogType.DELETE, ClassType.SECTOR,"delete sector "+secondSector2.getName());
                }
            }
            //when secondSectorName is null
            if (secondSectorName.equals("null")) {
                SectorCompany secondSector2 = sectorCompanyRepository.findByName(firstSectorName);
                clientService.deleteSectorFromclient(null,secondSector2,null);
                sectorCompanyRepository.delete(secondSector2);
                logService.addLog(LogType.DELETE, ClassType.SECTOR,"delete sector "+secondSector2.getName());
            }

    }

    @Override
    public List<SectorCompanyDto> getAllSectorsByParent(String sectorName) {
        ArrayList<SectorCompanyDto> sectorCompanyDtos = new ArrayList<>();
        List<SectorCompany> SectorCompanys = sectorCompanyRepository.findByParent(sectorCompanyRepository.findByName(sectorName));
        SectorCompany sectorCompanyParent = sectorCompanyRepository.findByName(sectorName);
       // System.out.println(SectorCompanys);
        for (SectorCompany sectorCompany : SectorCompanys) {
            SectorCompanyDto sectorCompanyDto = new SectorCompanyDto();
            sectorCompanyDto.setFirstSectorId(sectorCompanyParent.get_id());
            sectorCompanyDto.setFirstSectorName(sectorCompanyParent.getName());
            sectorCompanyDto.setFirstSectorDescription(sectorCompanyParent.getDescription());
            sectorCompanyDto.setSecondSectorId(sectorCompany.get_id());
            sectorCompanyDto.setSecondSectorName(sectorCompany.getName());
            sectorCompanyDto.setSecondSectorDescription(sectorCompany.getDescription());
            List<SectorCompany> thirdSectors = sectorCompanyRepository.findByParent(sectorCompany);
         //   System.out.println(thirdSectors);
            if (thirdSectors != null) {
                for (SectorCompany thirdsectorCompany : thirdSectors) {
                    SectorCompanyDto sectorCompanyDtoCopy = new SectorCompanyDto(
                            sectorCompanyParent.get_id(),
                            sectorCompanyParent.getName(),
                            sectorCompanyParent.getDescription(),
                            sectorCompany.get_id(),
                            sectorCompany.getName(),
                           sectorCompany.getDescription()
                    );
                    sectorCompanyDtoCopy.setThirdSectorId(thirdsectorCompany.get_id());
                    sectorCompanyDtoCopy.setThirdSectorName(thirdsectorCompany.getName());
                    sectorCompanyDtoCopy.setThirdSectorDescription(thirdsectorCompany.getDescription());
                    sectorCompanyDtos.add(sectorCompanyDtoCopy);
                }
            }
            else {
                sectorCompanyDtos.add(sectorCompanyDto);
            }
        }
        return sectorCompanyDtos;
    }

}
