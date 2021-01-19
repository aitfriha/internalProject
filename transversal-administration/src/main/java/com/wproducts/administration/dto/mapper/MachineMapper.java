package com.wproducts.administration.dto.mapper;

import com.wproducts.administration.controller.request.MachineAddRequest;
import com.wproducts.administration.controller.request.MachineUpdateRequest;
import com.wproducts.administration.dto.model.MachineDto;
import com.wproducts.administration.model.Machine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MachineMapper {

    /**
     * Map dto to model
     *
     * @param machineDto machineDto
     * @return Machine
     */
    @Mapping(source = "machineId", target="_id")
    Machine dtoToModel(MachineDto machineDto);

    /**
     * Map model to dto
     *
     * @param machine machine
     * @return machineDto
     */
    @Mapping(source = "_id", target="machineId")
    @Mapping(target = "machineCreatedAt", ignore=true)
    @Mapping(target = "machineUpdatedAt", ignore=true)
    MachineDto modelToDto(Machine machine);

    /**
     * Map add request to dto
     *
     * @param machineAddRequest machineAddRequest
     * @return machineDto
     */
    MachineDto addRequestToDto(MachineAddRequest machineAddRequest);

    /**
     * Map update request to dto
     *
     * @param MachineUpdateRequest MachineUpdateRequest
     * @return machineDto
     */
    MachineDto updateRequestToDto(MachineUpdateRequest MachineUpdateRequest);
}
