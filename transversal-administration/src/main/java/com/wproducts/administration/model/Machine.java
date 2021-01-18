package com.wproducts.administration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain=true)
@Document()
@Builder
public class Machine {

    @Id
    private String _id;
    private String machineType;
    private String machineBrand;
    private String machineModel;
    private String machineMacAddress;
    private String machineNetBiosName;
    private String machineSerialNumber;
    private Instant machineCreatedAt;
    private Instant machineUpdatedAt;

}
