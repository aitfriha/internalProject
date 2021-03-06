package org.techniu.isbackend.exception;

public enum ExceptionType {

    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    ADDED("added"),
    IMPORTED("imported"),
    UPDATED("updated"),
    SENT("password has been sent to you email"),
    INVALID_TOKEN("invalid Token"),
    EXPIRED_TOKEN("EXPIRED Token"),
    APPROVED("approved"),
    APPLICATION_ALREADY_HAS_A_CONTRACT("application.already.has.a.contract"),
    PUBLIC_PROPERTY_ALREADY_HAS_A_METER("public.property.already.has.meter"),
    DOMESTIC_PROPERTY_ALREADY_HAS_A_METER("domestic.property.already.has.meter"),
    INDCOM_PROPERTY_ALREADY_HAS_A_METER("industrial.commercial.property.already.has.meter"),
    APPLICATION_Type_AND_WATER_CONTRACT_CATEGORY_ARE_DIFFERENT("application.type.and.water.contract.category.are.different"),
    CONFIGURATION_DATABASE_TABLE_NAME_NOT_BLANK("configuration.table.name.can't.be.blank"),
    METER_DIAMETER_CAN_T_BE_NULL("meter.diameter.can't.be.null"),
    CODE_SHOULD_NOT_CONTAIN_SPACES("code.should.not.contain.spaces"),
    FILL_ALL_NECESSARY_FIELDS("Please fill all necessary fields"),
    STAFF_NAME_EXIST("Staff full name already exist"),
    STAFF_EMAIL_EXIST("Personal email already exist"),
    STAFF_EMAIL_INVALID("Invalid company email"),
    STAFF_COMPANY_EMAIL_EXIST("Company email already exist"),
    STAFF_COMPANY_MOBILE_PHONE_EXIST("Company mobile already exist"),
    STAFF_PERSONAL_PHONE_EXIST("Personal phone already exist"),
    STAFF_SKYPE_EXIST("Skype account already exist"),
    STAFF_PERSONAL_NUMBER_EXIST("Employee number already exist"),
    STAFF_DOCUMENT_NUMBER_EXIST("Document number already exist"),
    STAFF_DOCUMENT_DATE_EQUAL("Expedition date is the same as expiration date"),
    DELETED("deleted"),
    ASSIGNED_LEVEL_STAFF("staff.assigned.to.level"),
    SECTOR_RELATED_TO_CLIENT("this sector its already related to a client !"),
    STAFF_IS_NOTE_ACTIVE("this staff has left the company !"),
    //STAFF_IS_NOTE_ACTIVE("this staff is note active !"),
    USER_IS_NOTE_ACTIVE("this user is note active"),
    IMPORTATION_STAFF_NOTE_EXIST("Staff not exist in our data base !"),
    Name_SHOULD_NOT_CONTAIN_SPACES("name.should.not.contain.spaces"),
    STAFF_NOT_ASIGNED_TO_COMMERCIAL_LEVEL("Staff is not assigned to a commercial level !"),
    // public static final ExceptionType SECTOR_RELATED_TO_CLIENT = null;

    ASSOCIATED_WITH_SOME_WEEKLY_WORK("associated.with.some.weekly.work"),
    EMPTY_MANDATORY_FIELDS("empty.mandatory.fields"),
    ERRORS_IN_JOURNAL_VALUES("errors.in.journal.values"),
    NOT_GENERATED("not.generated"),
    COULD_NOT_BE_UPDATED("could.not.be.updated"),
    ASSOCIATED_WITH_SOME_TRAVEL_REQUEST("associated.with.some.travel.request"),
    ASSOCIATED_WITH_SOME_EXPENSE("associated.with.some.expense"),
    DUPLICATE_ACTION("duplicate.action"),
    NOT_ASSOCIATED_DATA("not.associated.data"),

    CONFIG_FILE_NOT_FOUND("config.file.not.found");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }
}
