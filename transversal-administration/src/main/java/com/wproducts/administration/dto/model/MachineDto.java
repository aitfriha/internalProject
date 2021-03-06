package com.wproducts.administration.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineDto {

    private String machineId;
    private String machineType;
    private String machineBrand;
    private String machineModel;
    private String machineMacAddress;
    private String machineNetBiosName;
    private String machineSerialNumber;
    private String machineCreatedAt;
    private String machineUpdatedAt;
}
