package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClientAddrequest {

    private String code;
    private String logo;
    private String name;
    private String type;
    private String responsibleCommercial;
    private String assistantCommercial;
    private String multinational = "No";
    private String isActive = "Yes";
    private String phone;
    private String email;
    private String cityId;
    private String addressName;
    private String postCode;

    private String sector1;
    private String sector2;
    private String sector3;
    private String sectorLeader;
}
