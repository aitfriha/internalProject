package com.wproducts.administration.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MachineAddRequest {
    @NotBlank(message = MACHINE_TYPE_NOT_BLANK)
    private String machineType;
    private String machineBrand;
    private String machineModel;
    @NotBlank(message = MACHINE_MAC_ADDRESS_NOT_BLANK)
    private String machineMacAddress;
    private String machineNetBiosName;
    @NotBlank(message = MACHINE_SERIAL_NUMBER_NOT_BLANK)
    private String machineSerialNumber;
}
