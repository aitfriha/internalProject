package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.dto.model.ExpenseDto;
import org.techniu.isbackend.entity.Expense;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    /**
     * Map dto to model
     *
     * @param expenseDto
     * @return Expense
     */
    @Mapping(source = "id", target="_id")
    @Mapping(target = "expenseType", ignore=true)
    @Mapping(target = "staff", ignore=true)
    @Mapping(target = "voucherType", ignore=true)
    @Mapping(target = "status", ignore=true)
    @Mapping(target = "localCurrencyType", ignore=true)
    @Mapping(target = "euroCurrencyType", ignore=true)
    @Mapping(target = "expenseCountry", ignore=true)
    @Mapping(target = "expenseState", ignore=true)
    @Mapping(target = "expenseCity", ignore=true)
    @Mapping(target = "fromCountry", ignore=true)
    @Mapping(target = "fromState", ignore=true)
    @Mapping(target = "fromCity", ignore=true)
    @Mapping(target = "toCountry", ignore=true)
    @Mapping(target = "toState", ignore=true)
    @Mapping(target = "toCity", ignore=true)
    @Mapping(target = "document", ignore=true)
    Expense dtoToModel(ExpenseDto expenseDto);

    /**
     * Map model to dto
     *
     * @param expense
     * @return ExpenseDto
     */
    @Mapping(source = "_id", target="id")
    @Mapping(source = "expenseType._id", target="expenseTypeId")
    @Mapping(source = "expenseType.allowSubtypes", target="expenseTypeAllowSubtypes")
    @Mapping(source = "expenseType.masterValue", target="expenseTypeMasterValue")
    @Mapping(source = "staff.staffId", target="staffId")
    @Mapping(source = "staff.staffContract.personalNumber", target="staffPersonalNumber")
    @Mapping(source = "staff.firstName", target="staffName")
    @Mapping(source = "staff.fatherFamilyName", target="staffFatherFamilyName")
    @Mapping(source = "staff.motherFamilyName", target="staffMotherFamilyName")
    @Mapping(source = "staff.staffContract.company.name", target="staffCompany")
    @Mapping(source = "staff.companyEmail", target="staffCompanyEmail")
    @Mapping(source = "voucherType._id", target="voucherTypeId")
    @Mapping(source = "voucherType.name", target="voucherTypeName")
    @Mapping(source = "voucherType.masterValue", target="voucherTypeMasterValue")
    @Mapping(source = "localCurrencyType._id", target="localCurrencyTypeId")
    @Mapping(source = "localCurrencyType.currencyName", target="localCurrencyTypeName")
    @Mapping(source = "euroCurrencyType._id", target="euroCurrencyTypeId")
    @Mapping(source = "euroCurrencyType.currencyName", target="euroCurrencyTypeName")
    @Mapping(source = "status._id", target="expenseStatusId")
    @Mapping(source = "status.name", target="expenseStatusName")
    @Mapping(source = "status.masterValue", target="expenseStatusMasterValue")
    @Mapping(source = "expenseCountry.countryId", target="expenseCountryId")
    @Mapping(source = "expenseCountry.countryName", target="expenseCountryName")
    @Mapping(source = "expenseState._id", target="expenseStateId")
    @Mapping(source = "expenseState.stateName", target="expenseStateName")
    @Mapping(source = "expenseCity._id", target="expenseCityId")
    @Mapping(source = "expenseCity.cityName", target="expenseCityName")
    @Mapping(source = "fromCountry.countryId", target="fromCountryId")
    @Mapping(source = "fromCountry.countryName", target="fromCountryName")
    @Mapping(source = "fromState._id", target="fromStateId")
    @Mapping(source = "fromState.stateName", target="fromStateName")
    @Mapping(source = "fromCity._id", target="fromCityId")
    @Mapping(source = "fromCity.cityName", target="fromCityName")
    @Mapping(source = "toCountry.countryId", target="toCountryId")
    @Mapping(source = "toCountry.countryName", target="toCountryName")
    @Mapping(source = "toState._id", target="toStateId")
    @Mapping(source = "toState.stateName", target="toStateName")
    @Mapping(source = "toCity._id", target="toCityId")
    @Mapping(source = "toCity.cityName", target="toCityName")
    ExpenseDto modelToDto(Expense expense);
}
