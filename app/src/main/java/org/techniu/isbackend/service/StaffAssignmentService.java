package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.CommercialOperationDto;
import org.techniu.isbackend.dto.model.FinancialContractDto;
import org.techniu.isbackend.dto.model.StaffAssignmentDto;
import org.techniu.isbackend.dto.model.StaffDto;
import org.techniu.isbackend.entity.AssignmentHistoryReport;
import org.techniu.isbackend.entity.TreeData;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface StaffAssignmentService {

    /**
     * Get all staff
     * @params operationId
     * @return List<StaffDto>
     */
    List<StaffDto> getEligibleStaff(String operationId);

    /**
     * Get all customerContracts
     *
     * @return List<FinancialContractDto>
     */
    List<FinancialContractDto> getAllCustomerContracts();

    /**
     * Get all treeData
     *
     * @return List<TreeData>
     */
    List<TreeData> getAllTreeData();

    /**
     * Get staff Assigned By Operation
     *
     * @param data
     *
     * @return List<StaffAssignment>
     */
    List<StaffAssignmentDto> getStaffAssignedByOperation(HashMap data);


    /**
     * filter staff By email
     *
     * @param email
     *
     * @return List<StaffAssignment>
     */
    List<StaffDto> filterStaffByEmail(String email);


    /**
     * delete Employees assigned to an Operation
     *
     * @param  list
     */
    void updateStaffOperation(List<HashMap<String, Object>> list);

    /**
     * Get All Customer Contracts By Employee
     *
     * @param employeeId
     *
     * @return List<FinancialContractDto>
     */
    List<FinancialContractDto> getAllCustomerContractsByEmployee(String employeeId);

    /**
     * Get All Operations By Employee And Customer
     *
     * @param data
     *
     * @return List<OperationDto>
     */
    List<CommercialOperationDto> getAllOperationsByEmployeeAndCustomer(HashMap data);

    /**
     * Get Assignment History Report
     *
     * @return List<HashMap>
     */
    List<AssignmentHistoryReport> getAssignmentHistoryReport();


    /**
     * exportStaffAssignment
     *
     * @param data
     */
    File exportStaffAssignment(HashMap data);

}
