package org.techniu.isbackend.entity;

import lombok.Data;
import org.techniu.isbackend.dto.model.ClientDto;
import org.techniu.isbackend.dto.model.CommercialOperationDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data()
public class TreeData {

    private ClientDto customer;
    private List<CommercialOperationDto> operations;

    public TreeData() {
        this.operations = new ArrayList<>();
    }

    public void addOperation(CommercialOperationDto op){
        if(!operations.contains(op)){
            operations.add(op);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof TreeData){
            TreeData that = (TreeData) o;
            return Objects.equals(customer.getClientId(), that.getCustomer().getClientId());
        }
        return false;
    }
}
