package com.wproducts.administration.service;

import com.wproducts.administration.controller.request.AbilityAddRequest;
import com.wproducts.administration.controller.request.RoleAddAbilitiesRequest;
import com.wproducts.administration.dto.mapper.*;
import com.wproducts.administration.dto.model.ActionDto;
import com.wproducts.administration.dto.model.RoleDto;
import com.wproducts.administration.model.Role;
import com.wproducts.administration.model.Action;
import com.wproducts.administration.repository.ActionRepository;
import com.wproducts.administration.repository.RoleRepository;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static org.techniu.isbackend.exception.ExceptionType.*;


@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AbilityService abilityService;
    private final ActionRepository actionRepository;
    private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
    private final AbilityMapper abilityMapper = Mappers.getMapper(AbilityMapper.class);
    private final ActionMapper actionMapper = Mappers.getMapper(ActionMapper.class);
    private final SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);


    public RoleServiceImpl(RoleRepository roleRepository, AbilityService abilityService, ActionRepository actionRepository) {
        this.roleRepository = roleRepository;
        this.abilityService = abilityService;
        this.actionRepository = actionRepository;
    }


    /**
     * Register a new Role
     *
     * @param roleDto - roleDto
     */
    @Override
    public void save(RoleDto roleDto) {

        roleDto.setRoleName(roleDto.getRoleName().toLowerCase());

        Optional<Role> role = Optional.ofNullable(roleRepository.findByRoleName(roleDto.getRoleName()));
        if (roleDto.getRoleName().contains(" ")) {
            throw exception(Name_SHOULD_NOT_CONTAIN_SPACES);
        }
        if (role.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
       // ArrayList<Action> roleActions = new ArrayList<>();
        /*Map<String, Boolean> actionsNames = new HashMap<String, Boolean>();
        actionsNames.put("admin_user_Management_access", false);
        actionsNames.put("admin_user_Management_create", false);
        actionsNames.put("admin_user_Management_modify", false);
        actionsNames.put("admin_user_Management_delete", false);
        actionsNames.put("admin_user_Management_export", false);
        actionsNames.put("admin_roles_management_access", false);
        actionsNames.put("admin_roles_management_create", false);
        actionsNames.put("admin_roles_management_modify", false);
        actionsNames.put("admin_roles_management_delete", false);
        actionsNames.put("admin_roles_management_export", false);
        actionsNames.put("commercial_customers_access", false);
        actionsNames.put("commercial_customers_create", false);
        actionsNames.put("commercial_customers_modify", false);
        actionsNames.put("commercial_customers_delete", false);
        actionsNames.put("commercial_customers_export", false);
        actionsNames.put("commercial_commercialAssignments_access", false);
        actionsNames.put("commercial_commercialAssignments_create", false);
        actionsNames.put("commercial_commercialAssignments_modify", false);
        actionsNames.put("commercial_commercialAssignments_delete", false);
        actionsNames.put("commercial_commercialAssignments_export", false);
        actionsNames.put("commercial_clientContact_access", false);
        actionsNames.put("commercial_clientContact_create", false);
        actionsNames.put("commercial_clientContact_modify", false);
        actionsNames.put("commercial_clientContact_delete", false);
        actionsNames.put("commercial_clientContact_export", false);
        actionsNames.put("commercial_commercialOperation_access", false);
        actionsNames.put("commercial_commercialOperation_create", false);
        actionsNames.put("commercial_commercialOperation_modify", false);
        actionsNames.put("commercial_commercialOperation_delete", false);
        actionsNames.put("commercial_commercialOperation_export", false);
        actionsNames.put("commercial_commercialAction_access", false);
        actionsNames.put("commercial_commercialAction_create", false);
        actionsNames.put("commercial_commercialAction_modify", false);
        actionsNames.put("commercial_commercialAction_delete", false);
        actionsNames.put("commercial_commercialAction_export", false);
        actionsNames.put("commercial_countriesStatesCities_access", false);
        actionsNames.put("commercial_countriesStatesCities_create", false);
        actionsNames.put("commercial_countriesStatesCities_modify", false);
        actionsNames.put("commercial_countriesStatesCities_delete", false);
        actionsNames.put("commercial_countriesStatesCities_export", false);
        actionsNames.put("commercial_StateOfCommercialOperation_access", false);
        actionsNames.put("commercial_StateOfCommercialOperation_create", false);
        actionsNames.put("commercial_StateOfCommercialOperation_modify", false);
        actionsNames.put("commercial_StateOfCommercialOperation_delete", false);
        actionsNames.put("commercial_StateOfCommercialOperation_export", false);
        actionsNames.put("commercial_contactByOperationStatus_access", false);
        actionsNames.put("commercial_contactByOperationStatus_create", false);
        actionsNames.put("commercial_contactByOperationStatus_modify", false);
        actionsNames.put("commercial_contactByOperationStatus_delete", false);
        actionsNames.put("commercial_contactByOperationStatus_export", false);
        actionsNames.put("commercial_serviceType_access", false);
        actionsNames.put("commercial_serviceType_create", false);
        actionsNames.put("commercial_serviceType_modify", false);
        actionsNames.put("commercial_serviceType_delete", false);
        actionsNames.put("commercial_serviceType_export", false);
        actionsNames.put("commercial_sectorsCompany_access", false);
        actionsNames.put("commercial_sectorsCompany_create", false);
        actionsNames.put("commercial_sectorsCompany_modify", false);
        actionsNames.put("commercial_sectorsCompany_delete", false);
        actionsNames.put("commercial_sectorsCompany_export", false);
        actionsNames.put("commercial_titleType_access", false);
        actionsNames.put("commercial_titleType_create", false);
        actionsNames.put("commercial_titleType_modify", false);
        actionsNames.put("commercial_titleType_delete", false);
        actionsNames.put("commercial_titleType_export", false);
        actionsNames.put("hh_administrativeStructureDefinition_access", false);
        actionsNames.put("hh_administrativeStructureDefinition_create", false);
        actionsNames.put("hh_administrativeStructureDefinition_modify", false);
        actionsNames.put("hh_administrativeStructureDefinition_delete", false);
        actionsNames.put("hh_administrativeStructureDefinition_export", false);
        actionsNames.put("hh_administrativeStructureAssignation_access", false);
        actionsNames.put("hh_administrativeStructureAssignation_modify", false);
        actionsNames.put("hh_functionalStructureDefinition_access", false);
        actionsNames.put("hh_functionalStructureDefinition_create", false);
        actionsNames.put("hh_functionalStructureDefinition_modify", false);
        actionsNames.put("hh_functionalStructureDefinition_delete", false);
        actionsNames.put("hh_functionalStructureDefinition_export", false);
        actionsNames.put("hh_functionalStructureAssignation_access", false);
        actionsNames.put("hh_functionalStructureAssignation_modify", false);
        actionsNames.put("hh_staff_personalInformationManagement_access", false);
        actionsNames.put("hh_staff_personalInformationManagement_create", false);
        actionsNames.put("hh_staff_personalInformationManagement_modify", false);
        actionsNames.put("hh_staff_personalInformationManagement_delete", false);
        actionsNames.put("hh_staff_personalInformationManagement_export", false);
        actionsNames.put("hh_staff_contractInformationManagement_access", false);
        actionsNames.put("hh_staff_contractInformationManagement_create", false);
        actionsNames.put("hh_staff_contractInformationManagement_modify", false);
        actionsNames.put("hh_staff_contractInformationManagement_delete", false);
        actionsNames.put("hh_staff_contractInformationManagement_export", false);
        actionsNames.put("hh_staff_economicObjectiveManagement_access", false);
        actionsNames.put("hh_staff_economicObjectiveManagement_create", false);
        actionsNames.put("hh_staff_economicObjectiveManagement_modify", false);
        actionsNames.put("hh_staff_economicObjectiveManagement_delete", false);
        actionsNames.put("hh_staff_economicObjectiveManagement_export", false);
        actionsNames.put("hh_absenceRequest_access", false);
        actionsNames.put("hh_absenceRequest_create", false);
        actionsNames.put("hh_absenceRequest_modify", false);
        actionsNames.put("hh_absenceRequest_delete", false);
        actionsNames.put("hh_absenceRequest_export", false);
        actionsNames.put("hh_absenceConsult_access", false);
        actionsNames.put("hh_absenceConsult_modify", false);
        actionsNames.put("hh_absenceConsult_export", false);
        actionsNames.put("hh_selectionProcessInformation_access", false);
        actionsNames.put("hh_selectionProcessInformation_create", false);
        actionsNames.put("hh_selectionProcessInformation_modify", false);
        actionsNames.put("hh_selectionProcessInformation_delete", false);
        actionsNames.put("hh_selectionProcessInformation_export", false);
        actionsNames.put("hh_typesOfLegalCategory_access", false);
        actionsNames.put("hh_typesOfLegalCategory_create", false);
        actionsNames.put("hh_typesOfLegalCategory_modify", false);
        actionsNames.put("hh_typesOfLegalCategory_delete", false);
        actionsNames.put("hh_typesOfLegalCategory_export", false);
        actionsNames.put("hh_typesOfContracts_access", false);
        actionsNames.put("hh_typesOfContracts_create", false);
        actionsNames.put("hh_typesOfContracts_modify", false);
        actionsNames.put("hh_typesOfContracts_delete", false);
        actionsNames.put("hh_typesOfContracts_export", false);
        actionsNames.put("hh_typesOfAbsences_access", false);
        actionsNames.put("hh_typesOfAbsences_create", false);
        actionsNames.put("hh_typesOfAbsences_modify", false);
        actionsNames.put("hh_typesOfAbsences_delete", false);
        actionsNames.put("hh_typesOfAbsences_export", false);
        actionsNames.put("hh_contractModels_access", false);
        actionsNames.put("hh_contractModels_create", false);
        actionsNames.put("hh_contractModels_modify", false);
        actionsNames.put("hh_contractModels_delete", false);
        actionsNames.put("hh_contractModels_export", false);
        actionsNames.put("hh_localBankHolidays_access", false);
        actionsNames.put("hh_localBankHolidays_create", false);
        actionsNames.put("hh_localBankHolidays_modify", false);
        actionsNames.put("hh_localBankHolidays_delete", false);
        actionsNames.put("hh_localBankHolidays_export", false);
        actionsNames.put("hh_selectionTypesEvaluation_access", false);
        actionsNames.put("hh_selectionTypesEvaluation_create", false);
        actionsNames.put("hh_selectionTypesEvaluation_modify", false);
        actionsNames.put("hh_selectionTypesEvaluation_delete", false);
        actionsNames.put("hh_selectionTypesEvaluation_export", false);
        actionsNames.put("operativeModule_staffAssignments_access", false);
        actionsNames.put("operativeModule_staffAssignments_create", false);
        actionsNames.put("operativeModule_staffAssignments_modify", false);
        actionsNames.put("operativeModule_staffAssignments_delete", false);
        actionsNames.put("operativeModule_staffAssignments_export", false);
        actionsNames.put("operativeModule_workParts_access", false);
        actionsNames.put("operativeModule_workParts_create", false);
        actionsNames.put("operativeModule_workParts_modify", false);
        actionsNames.put("operativeModule_workParts_delete", false);
        actionsNames.put("operativeModule_workParts_export", false);
        actionsNames.put("operativeModule_AssignmentType_access", false);
        actionsNames.put("operativeModule_AssignmentType_create", false);
        actionsNames.put("operativeModule_AssignmentType_modify", false);
        actionsNames.put("operativeModule_AssignmentType_delete", false);
        actionsNames.put("operativeModule_AssignmentType_export", false);
        actionsNames.put("operativeModule_workPartsConfig_access", false);
        actionsNames.put("operativeModule_workPartsConfig_modify", false);
        actionsNames.put("operativeModule_workPartsConfig_export", false);
        actionsNames.put("financialModule_contacts_access", false);
        actionsNames.put("financialModule_contacts_create", false);
        actionsNames.put("financialModule_contacts_modify", false);
        actionsNames.put("financialModule_contacts_delete", false);
        actionsNames.put("financialModule_contacts_export", false);
        actionsNames.put("financialModule_billingManagement_access", false);
        actionsNames.put("financialModule_billingManagement_create", false);
        actionsNames.put("financialModule_billingManagement_modify", false);
        actionsNames.put("financialModule_billingManagement_delete", false);
        actionsNames.put("financialModule_billingManagement_export", false);
        actionsNames.put("financialModule_staffEconomicManagement_access", false);
        actionsNames.put("financialModule_staffEconomicManagement_create", false);
        actionsNames.put("financialModule_staffEconomicManagement_modify", false);
        actionsNames.put("financialModule_staffEconomicManagement_delete", false);
        actionsNames.put("financialModule_staffEconomicManagement_export", false);
        actionsNames.put("financialModule_staffEconomicPayments_access", false);
        actionsNames.put("financialModule_staffEconomicPayments_create", false);
        actionsNames.put("financialModule_staffEconomicPayments_modify", false);
        actionsNames.put("financialModule_staffEconomicPayments_delete", false);
        actionsNames.put("financialModule_staffEconomicPayments_export", false);
        actionsNames.put("financialModule_suppliersPayments_access", false);
        actionsNames.put("financialModule_suppliersPayments_create", false);
        actionsNames.put("financialModule_suppliersPayments_modify", false);
        actionsNames.put("financialModule_suppliersPayments_delete", false);
        actionsNames.put("financialModule_suppliersPayments_export", false);
        actionsNames.put("financialModule_purchaseOrderManagement_access", false);
        actionsNames.put("financialModule_purchaseOrderManagement_create", false);
        actionsNames.put("financialModule_purchaseOrderManagement_modify", false);
        actionsNames.put("financialModule_purchaseOrderManagement_delete", false);
        actionsNames.put("financialModule_purchaseOrderManagement_export", false);
        actionsNames.put("financialModule_travelRequest_access", false);
        actionsNames.put("financialModule_travelRequest_create", false);
        actionsNames.put("financialModule_travelRequest_modify", false);
        actionsNames.put("financialModule_travelRequest_delete", false);
        actionsNames.put("financialModule_travelRequest_cancel", false);
        actionsNames.put("financialModule_travelRequest_export", false);
        actionsNames.put("financialModule_travelManagement_access", false);
        actionsNames.put("financialModule_travelManagement_modify", false);
        actionsNames.put("financialModule_travelManagement_export", false);
        actionsNames.put("financialModule_expenseRecord_access", false);
        actionsNames.put("financialModule_expenseRecord_create", false);
        actionsNames.put("financialModule_expenseRecord_modify", false);
        actionsNames.put("financialModule_expenseRecord_download", false);
        actionsNames.put("financialModule_expenseRecord_export", false);
        actionsNames.put("financialModule_expensesManagement_access", false);
        actionsNames.put("financialModule_expensesManagement_create", false);
        actionsNames.put("financialModule_expensesManagement_modify", false);
        actionsNames.put("financialModule_expensesManagement_download", false);
        actionsNames.put("financialModule_expensesManagement_export", false);
        actionsNames.put("financialModule_companies_access", false);
        actionsNames.put("financialModule_companies_create", false);
        actionsNames.put("financialModule_companies_modify", false);
        actionsNames.put("financialModule_companies_delete", false);
        actionsNames.put("financialModule_companies_export", false);
        actionsNames.put("financialModule_typeOfCurrency_access", false);
        actionsNames.put("financialModule_typeOfCurrency_create", false);
        actionsNames.put("financialModule_typeOfCurrency_modify", false);
        actionsNames.put("financialModule_typeOfCurrency_delete", false);
        actionsNames.put("financialModule_typeOfCurrency_export", false);
        actionsNames.put("financialModule_currencyManagement_access", false);
        actionsNames.put("financialModule_currencyManagement_create", false);
        actionsNames.put("financialModule_currencyManagement_modify", false);
        actionsNames.put("financialModule_currencyManagement_delete", false);
        actionsNames.put("financialModule_currencyManagement_export", false);
        actionsNames.put("financialModule_contractStatus_access", false);
        actionsNames.put("financialModule_contractStatus_create", false);
        actionsNames.put("financialModule_contractStatus_modify", false);
        actionsNames.put("financialModule_contractStatus_delete", false);
        actionsNames.put("financialModule_contractStatus_export", false);
        actionsNames.put("financialModule_iva_access", false);
        actionsNames.put("financialModule_iva_create", false);
        actionsNames.put("financialModule_iva_modify", false);
        actionsNames.put("financialModule_iva_delete", false);
        actionsNames.put("financialModule_iva_export", false);
        actionsNames.put("financialModule_typeOfRententions_access", false);
        actionsNames.put("financialModule_typeOfRententions_create", false);
        actionsNames.put("financialModule_typeOfRententions_modify", false);
        actionsNames.put("financialModule_typeOfRententions_delete", false);
        actionsNames.put("financialModule_typeOfRententions_export", false);
        actionsNames.put("financialModule_suppliersTypes_access", false);
        actionsNames.put("financialModule_suppliersTypes_create", false);
        actionsNames.put("financialModule_suppliersTypes_modify", false);
        actionsNames.put("financialModule_suppliersTypes_delete", false);
        actionsNames.put("financialModule_suppliersTypes_export", false);
        actionsNames.put("financialModule_externalSuppliers_access", false);
        actionsNames.put("financialModule_externalSuppliers_create", false);
        actionsNames.put("financialModule_externalSuppliers_modify", false);
        actionsNames.put("financialModule_externalSuppliers_delete", false);
        actionsNames.put("financialModule_externalSuppliers_export", false);
        actionsNames.put("financialModule_purchaseOrderAcceptance_access", false);
        actionsNames.put("financialModule_purchaseOrderAcceptance_create", false);
        actionsNames.put("financialModule_purchaseOrderAcceptance_modify", false);
        actionsNames.put("financialModule_purchaseOrderAcceptance_delete", false);
        actionsNames.put("financialModule_purchaseOrderAcceptance_export", false);
        actionsNames.put("financialModule_businessExpenseTypes_access", false);
        actionsNames.put("financialModule_businessExpenseTypes_create", false);
        actionsNames.put("financialModule_businessExpenseTypes_modify", false);
        actionsNames.put("financialModule_businessExpenseTypes_delete", false);
        actionsNames.put("financialModule_businessExpenseTypes_export", false);
        actionsNames.put("financialModule_requestStatus_access", false);
        actionsNames.put("financialModule_requestStatus_create", false);
        actionsNames.put("financialModule_requestStatus_modify", false);
        actionsNames.put("financialModule_requestStatus_delete", false);
        actionsNames.put("financialModule_requestStatus_export", false);
        actionsNames.put("financialModule_travelRequestEmailAddress_access", false);
        actionsNames.put("financialModule_travelRequestEmailAddress_create", false);
        actionsNames.put("financialModule_travelRequestEmailAddress_modify", false);
        actionsNames.put("financialModule_travelRequestEmailAddress_delete", false);
        actionsNames.put("financialModule_travelRequestEmailAddress_export", false);
        actionsNames.put("financialModule_staffExpensesTypes_access", false);
        actionsNames.put("financialModule_staffExpensesTypes_create", false);
        actionsNames.put("financialModule_staffExpensesTypes_modify", false);
        actionsNames.put("financialModule_staffExpensesTypes_delete", false);
        actionsNames.put("financialModule_staffExpensesTypes_export", false);
        actionsNames.put("financialModule_personsTypes_access", false);
        actionsNames.put("financialModule_personsTypes_create", false);
        actionsNames.put("financialModule_personsTypes_modify", false);
        actionsNames.put("financialModule_personsTypes_delete", false);
        actionsNames.put("financialModule_personsTypes_export", false);
        actionsNames.put("financialModule_voucherType_access", false);
        actionsNames.put("financialModule_voucherType_create", false);
        actionsNames.put("financialModule_voucherType_modify", false);
        actionsNames.put("financialModule_voucherType_delete", false);
        actionsNames.put("financialModule_voucherType_export", false);
        actionsNames.put("financialModule_expensesStatus_access", false);
        actionsNames.put("financialModule_expensesStatus_create", false);
        actionsNames.put("financialModule_expensesStatus_modify", false);
        actionsNames.put("financialModule_expensesStatus_delete", false);
        actionsNames.put("financialModule_expensesStatus_export", false);
        actionsNames.put("financialModule_expensesEmailAddress_access", false);
        actionsNames.put("financialModule_expensesEmailAddress_create", false);
        actionsNames.put("financialModule_expensesEmailAddress_modify", false);
        actionsNames.put("financialModule_expensesEmailAddress_delete", false);
        actionsNames.put("financialModule_expensesEmailAddress_export", false);*/
        Role role1 = roleMapper.dtoToModel(roleDto)
                .setRoleDescription(roleDto.getRoleDescription())
                .setRoleCreatedAt(Instant.now());

        roleRepository.save(role1);
    }


    /**
     * Add abilities to certain role
     *
     * @param roleAddAbilitiesRequest - roleAddAbilitiesRequest
     */
    @Override
    public void addAbilities(RoleAddAbilitiesRequest roleAddAbilitiesRequest) {

        Role role = new Role().set_id(roleAddAbilitiesRequest.getRoleId());

        List<Action> actions = new ArrayList<>();
        for (AbilityAddRequest abilityAddRequest :
                roleAddAbilitiesRequest.getRoleAbilities()) {
            ///  actions.add(abilityService.saveAndReturnAbility(abilityAddRequest));
        }

      //  role.setRoleActions(actions);
        role.setRoleUpdatedAt(Instant.now());

        roleRepository.save(role);

    }

    @Override
    public void updateRole(RoleDto roleDto,String oldRoleName) {

        roleDto.setRoleName(roleDto.getRoleName().toLowerCase());
        if (roleDto.getRoleName().contains(" ")) {
            throw exception(Name_SHOULD_NOT_CONTAIN_SPACES);
        }
        Optional<Role> role = Optional.ofNullable(roleRepository.findByRoleName(oldRoleName));
        if (!role.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }

        Optional<Role> role1 = Optional.ofNullable(roleRepository.findByRoleName(oldRoleName));
        if (role1.isPresent() && !(role.get().getRoleName().equals(oldRoleName))) {
            throw exception(DUPLICATE_ENTITY);
        }

        Role role2 = roleMapper.dtoToModel(roleDto)
                .set_id(role1.get().get_id())
                .setRoleUpdatedAt(Instant.now())
                .setRoleDescription(roleDto.getRoleDescription())
                .setRoleCreatedAt(role.get().getRoleCreatedAt());

        roleRepository.save(role2);
    }

    /**
     * delete Role
     *
     * @param id - id
     */
    @Override
    public void removeRole(String id) {
        Optional<Role> role = Optional.ofNullable(roleRepository.findBy_id(id));
        // If role doesn't exists
        if (!role.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        roleRepository.deleteById(id);
    }

    /**
     * all RolesDto
     *
     * @return List RolesDto
     */
    @Override
    public List<RoleDto> getAllRoles() {
        // Get all roles
        List<Role> roles = roleRepository.findAll();
        // Create a list of all roles dto
        ArrayList<RoleDto> roleDtos = new ArrayList<>();
        for (Role role : roles) {
            ArrayList<ActionDto> actionDtos = new ArrayList<>();
            Set<String> actionsIds = new HashSet<>();
            RoleDto roleDto = roleMapper.modelToDto(role).setRoleName(role.getRoleName().toUpperCase());
           /* if (role.getRoleActions() != null) {
                for (Action action : role.getRoleActions()) {
                    ActionDto actionDto = actionMapper.modelToDto(action);
                    actionDto.setActionCreatedAt(action.getActionCreatedAt().toString());
                    if (action.getActionUpdatedAt() != null) {
                        actionDto.setActionUpdatedAt(action.getActionUpdatedAt().toString());
                    }
                    actionDtos.add(actionDto);
                }
            }*/
           // roleDto.setRoleActions(actionDtos);
            if (role.getRoleUpdatedAt() != null) {
                roleDto.setRoleUpdatedAt(role.getRoleUpdatedAt().toString());
            }
            if (role.getRoleCreatedAt() != null) {
                roleDto.setRoleCreatedAt(role.getRoleCreatedAt().toString());
            }
            // ActionDto actionDto =new ActionDto();
            // actionDto.setActionId("5f10ce983d7fb0748720c5da");
            // actionDto.setActionConcerns("Access");
            // actionDtos.add(actionDto);
           // roleDto.setRoleActions(actionDtos);
            // actionsIds.add("Access");
            //  roleDto.setRoleActionsIds(actionsIds);
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }

    /**
     * one RoleDto
     *
     * @param id - id
     * @return RoleDto
     */
    @Override
    public RoleDto getOneRole(String id) {
        Optional<Role> role = Optional.ofNullable(roleRepository.findBy_id(id));

        // If role doesn't exists
        if (!role.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        RoleDto roleDto = roleMapper.modelToDto(role.get());
        ArrayList<ActionDto> actionDtos = new ArrayList<>();
        /*if (role.get().getRoleActions() != null) {
            for (Action action : role.get().getRoleActions()) {
                actionDtos.add(actionMapper.modelToDto(action));
            }
        }*/
        if (role.get().getRoleUpdatedAt() != null) {
            roleDto.setRoleUpdatedAt(role.get().getRoleUpdatedAt().toString());
        }
        if (role.get().getRoleCreatedAt() != null) {
            roleDto.setRoleCreatedAt(role.get().getRoleCreatedAt().toString());
        }
        /*if (role.get().getRoleActions() != null) {
            for (Action action : role.get().getRoleActions()) {
                ActionDto actionDto = actionMapper.modelToDto(action);
                actionDto.setActionCreatedAt(action.getActionCreatedAt().toString());
                if (action.getActionUpdatedAt() != null) {
                    actionDto.setActionUpdatedAt(action.getActionUpdatedAt().toString());
                }
                actionDtos.add(actionDto);
            }
        }*/
        //roleDto.setRoleActions(actionDtos);
        return roleDto;
    }

    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          - args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.Role, exceptionType, args);
    }
}
