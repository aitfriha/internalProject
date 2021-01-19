package com.wproducts.administration.service;

import com.wproducts.administration.dto.mapper.MachineMapper;
import com.wproducts.administration.dto.model.MachineDto;
import com.wproducts.administration.model.Machine;
import com.wproducts.administration.repository.MachineRepository;
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
import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;
    private final MachineMapper machineMapper = Mappers.getMapper(MachineMapper.class);

    public MachineServiceImpl(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    /**
     * Register a new Machine
     *
     * @param machineDto - machineDto
     */
    @Override
    public void save(MachineDto machineDto) {

        machineDto.setMachineSerialNumber(machineDto.getMachineSerialNumber().toLowerCase());

        Optional<Machine> machine = Optional.ofNullable(machineRepository.findByMachineSerialNumber(machineDto.getMachineSerialNumber()));
        if (machineDto.getMachineSerialNumber().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        if (machine.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        Machine machine1 = machineMapper.dtoToModel(machineDto)
                .setMachineCreatedAt(Instant.now());
        machineRepository.save(machine1);
    }

    /**
     * Update Machine
     *
     * @param machineDto - machineDto
     */
    @Override
    public void updateMachine(MachineDto machineDto) {

        machineDto.setMachineSerialNumber(machineDto.getMachineSerialNumber().toLowerCase());
        Optional<Machine> machine = Optional.ofNullable(machineRepository.findMachineBy_id(machineDto.getMachineId()));
        Optional<Machine> machineCode = Optional.ofNullable(machineRepository.findByMachineSerialNumber(machineDto.getMachineSerialNumber()));
        if (!machine.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        if (machineCode.isPresent() && !(machine.get().getMachineSerialNumber().equals(machineDto.getMachineSerialNumber())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
        if (machineDto.getMachineSerialNumber().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }

        Machine machine1 = machineMapper.dtoToModel(machineDto)
                .setMachineUpdatedAt(Instant.now())
                .setMachineCreatedAt(machine.get().getMachineCreatedAt());
        // Update machine in database
        machineRepository.save(machine1);

    }

    /**
     * delete Machine
     *
     * @param id - id
     */
    @Override
    public void removeMachine(String id) {
        Optional<Machine> machine = Optional.ofNullable(machineRepository.findMachineBy_id(id));
        // If machine doesn't exists
        if (!machine.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        machineRepository.deleteById(id);
    }

    /**
     * all MachinesDto
     *
     * @return List MachinesDto
     */
    @Override
    public List<MachineDto> getAllMachines() {
        // Get all machines
        List<Machine> machines = machineRepository.findAll();

        // Create a list of all machines dto
        ArrayList<MachineDto> machineDtos = new ArrayList<>();

        for (Machine machine : machines) {
            MachineDto machineDto = machineMapper.modelToDto(machine);
            if(machine.getMachineUpdatedAt() !=null ) {
                machineDto.setMachineUpdatedAt(machine.getMachineUpdatedAt().toString());
            }
            if(machine.getMachineCreatedAt()!=null ) {
                machineDto.setMachineCreatedAt(machine.getMachineCreatedAt().toString());
            }

            machineDtos.add(machineDto);
        }
        return machineDtos;
    }

    /**
     * one MachineDto
     *
     * @param id - id
     * @return MachineDto
     */
    @Override
    public MachineDto getOneMachine(String id) {

        Optional<Machine> machine = Optional.ofNullable(machineRepository.findMachineBy_id(id));

        // If machine doesn't exists
        if (!machine.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        MachineDto machineDto = machineMapper.modelToDto(machine.get());
        if(machine.get().getMachineUpdatedAt() !=null ) {
            machineDto.setMachineUpdatedAt(machine.get().getMachineUpdatedAt().toString());
        }
        if(machine.get().getMachineCreatedAt()!=null ) {
            machineDto.setMachineCreatedAt(machine.get().getMachineCreatedAt().toString());
        }

        return machineDto;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Machine, exceptionType, args);
    }
}
