package org.techniu.isbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.techniu.isbackend.repository.*;



import java.io.File;

@SpringBootApplication(scanBasePackages = {"com.wproducts.*","org.techniu.*", "com.internal.*"})
@EnableMongoRepositories(basePackages = {"com.wproducts.*","org.techniu.*", "com.internal.*"})
public class IsBackendApplication implements CommandLineRunner {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private LeaderRepository leaderRepository;
    @Autowired
    private SectorConfigRepository sectorConfigRepository;


    public static void main(String[] args) {

        SpringApplication.run(IsBackendApplication.class, args);
        File documentsDirectory = new File("TEMPORAL DOCUMENTS");
        if(!documentsDirectory.exists()){
            documentsDirectory.mkdirs();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        /*
        Sector s = new Sector();
        s.setName("Energy");
        s.setType("primary");
        s.setPrimary(null);
        s = sectorRepository.save(s);
        Sector sector = new Sector();
        sector.setType("secondary");
        sector.setName("Electricity");
        sector.setPrimary(s);
        sector = sectorRepository.save(sector);
        Sector sector1 = new Sector();
        sector1.setType("third");
        sector1.setName("Distribution");
        sector1.setPrimary(sector);
        sector1 = sectorRepository.save(sector1);
        Sector sector2 = new Sector();
        sector2.setType("third");
        sector2.setName("Generation");
        sector2.setPrimary(sector);
        sector2 = sectorRepository.save(sector2);
        SectorConfig sectorConfig = new SectorConfig();
        sectorConfig.setPrimarySector("Energy");
        sectorConfig.setSecondarySector("Electricity");
        sectorConfig.setThirdSector("Generation");
        Leader l = new Leader();
        l.setName("Name 1");
        l = leaderRepository.save(l);
        sectorConfig.setLeader(l);
        sectorConfigRepository.save(sectorConfig);
        SectorConfig sectorConfig1 = new SectorConfig();
        sectorConfig1.setPrimarySector("Energy");
        sectorConfig1.setSecondarySector("Electricity");
        sectorConfig1.setThirdSector("Distribution");
        Leader l1 = new Leader();
        l1.setName("Name 2");
        l1 = leaderRepository.save(l1);
        sectorConfig1.setLeader(l1);
        sectorConfigRepository.save(sectorConfig1);
        Commercial commercial = new Commercial();
        commercial.setName("Diobrai Sogo");
        commercial.setType("responsible");
        commercial = commercialRepository.save(commercial);
        Commercial commercial1 = new Commercial();
        commercial1.setName("Diobrai SogoDiobrai");
        commercial1.setType("assistant");
        commercial1 = commercialRepository.save(commercial1);


        Direction d = new Direction();
        d.setDirectionName("Direction 1");
        d = directionRepository.save(d);
        Direction d1 = new Direction();
        d1.setDirectionName("Direction 2");
        d1 = directionRepository.save(d1);

        Area area = new Area();
        area.setAreaName("Area 1");
        area.setDirection(d);
        areaRepository.save(area);
        Area area1 = new Area();
        area1.setAreaName("Area 2");
        area1.setDirection(d);
        areaRepository.save(area1);
        Area area2 = new Area();
        area2.setAreaName("Area 3");
        area2.setDirection(d);
        areaRepository.save(area2);
        Area area3 = new Area();
        area3.setAreaName("Area 4");
        area3.setDirection(d);
        areaRepository.save(area3);
        Area area4 = new Area();
        area4.setAreaName("Area 1");
        area4.setDirection(d1);
        areaRepository.save(area4);
        Area area5 = new Area();
        area5.setAreaName("Area 2");
        area5.setDirection(d1);
        areaRepository.save(area5);
        Area area6 = new Area();
        area6.setAreaName("Area 3");
        area6.setDirection(d1);
        areaRepository.save(area6);

         */

    }

}
