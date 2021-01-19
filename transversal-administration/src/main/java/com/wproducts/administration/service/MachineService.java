package com.wproducts.administration.service;

import com.wproducts.administration.dto.model.MachineDto;

import java.util.List;

public interface MachineService {

    /**
     * Register a new Machine
     *
     * @param machineDto - machineDto
     */
    void save(MachineDto machineDto);

    /**
     * Update Machine
     *
     * @param machineDto - machineDto
     */
    void updateMachine(MachineDto machineDto);

    /**
     * delete Machine
     *
     * @param id - id
     */
    void removeMachine(String id);

    /**
     * all MachinesDto
     *
     * @return List MachinesDto
     */
    List<MachineDto> getAllMachines();

    /**
     * one MachineDto
     *
     * @param id - id
     * @return MachineDto
     */
    MachineDto getOneMachine(String id);
}
