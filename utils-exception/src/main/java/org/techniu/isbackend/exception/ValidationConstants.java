package org.techniu.isbackend.exception;

import java.util.HashMap;

import static org.techniu.isbackend.exception.EntityType.*;

public class ValidationConstants {

    // put the validators constants here
    // Auth
    public final static String AUTH_WRONG_CREDENTIALS = "auth.wrong.credentials";
    //Action
    public final static String ACTION_CODE_NOT_BLANK = "action.code.not.blank";

    //Department
    public final static String DEPARTMENT_CODE_NOT_BLANK = "department.code.not.blank";
    public final static String DEPARTMENT_UPDATED_AT_NOT_BLANK = "department.updated.at.not.blank";
    public final static String DEPARTMENT_CREATED_AT_NOT_BLANK = "department.created.at.not.blank";

    //Subject
    public final static String SUBJECT_CODE_NOT_BLANK = "subject.code.not.blank";
    public final static String SUBJECT_UPDATED_AT_NOT_BLANK = "subject.updated.at.not.blank";
    public final static String SUBJECT_CREATED_AT_NOT_BLANK = "subject.created.at.not.blank";

    //SubjectField
    public final static String FIELD_CODE_NOT_BLANK = "subjectfield.code.not.blank";

    // Role
    public final static String ROLE_NAME_NOT_BLANK = "role.name.not.blank";

    // Ability
    public final static String ACTION_ID_NOT_BLANK = "action.id.not.blank";
    public final static String SUBJECT_ID_NOT_BLANK = "subject.id.not.blank";

    // Machine
    public final static String MACHINE_TYPE_NOT_BLANK = "machine.type.not.blank";
    public final static String MACHINE_MAC_ADDRESS_NOT_BLANK = "machine.mac.address.not.blank";
    public final static String MACHINE_SERIAL_NUMBER_NOT_BLANK = "machine.serial.number.not.blank";

    // Users
    public final static String USER_EMAIL_NOT_BLANK = "user.email.not.blank";
    public final static String USER_FULL_NAME_NOT_BLANK = "user.full.name.not.blank";
    public final static String USER_PASSWORD_NOT_BLANK = "user.password.not.blank";
    public final static String USER_MOBILE_NUMBER_NOT_BLANK = "user.mobile.number.not.blank";
    public final static String USER_STATUS_NOT_BLANK = "user.status.not.blank";
    public final static String USER_ACTIVE_NOT_NULL = "user.active.not.null";
    public final static String USER_ROLES_NOT_EMPTY = "user.roles.not.empty";
    public final static String USER_DEPARTMENT_NOT_BLANK = "user.department.not.blank";
    public final static String USER_COUNTRY_LANGUAGE_NOT_BLANK = "user.country.language.not.blank";

    // Default Sentence
    public final static String DEFAULTSENTENCE_CODE_NOT_BLANK = "defaultsentence.code.not.blank";
    public final static String DEFAULTSENTENCE_VALUE_NOT_BLANK = "defaultsentence.value.not.blank";

    // Translate Sentence
    public final static String TRANSLATESENTENCE_COUNTRY_LANGUAGE_CODE_NOT_BLANK = "translatesentence.countrylanguage.not.blank";
    public final static String TRANSLATESENTENCE_DEFAULT_SENTENCE_CODE_NOT_BLANK = "translatesentence.defaultsentence.not.blank";
    public final static String TRANSLATESENTENCE_TRANSLATION_NOT_BLANK = "translatesentence.translation.not.blank";

    // Measurement Units
    public final static String MEASUREMENTUNIT_NAME_NOT_BLANK = "measurementunit.name.not.blank";
    public final static String MEASUREMENTUNIT_SYMBOL_NOT_BLANK = "measurementunit.symbol.not.blank";
    public final static String MEASUREMENTUNIT_METRIC_NOT_NULL = "measurementunit.metric.not.null";
    public final static String MEASUREMENTUNIT_MEASUREMENT_NOT_BLANK = "measurementunit.measurement.not.blank";
    public final static String MEASUREMENTUNIT_INUSE_NOT_NULL = "measurementunit.inuse.not.null";


    // WaterService
    public final static String WATERSERVICE_TYPE_NOT_BLANK = "waterservice.type.not.blank";
    public final static String WATERSERVICE_NAME_NOT_BLANK = "waterservice.name.not.blank";
    public final static String WATERSERVICE_PRICE_NOT_NULL = "waterservice.price.not.null";
    public final static String WATERSERVICE_PRICEPER_NOT_NULL = "waterservice.priceper.not.null";
    public final static String WATERSERVICE_UNIT_NOT_BLANK = "waterservice.unit.not.blank";
    public final static String WATERSERVICE_DATE_NOT_BLANK = "waterservice.date.not.blank";
    public final static String WATERSERVICE_CURRENCYPRICE_NOT_BLANK = "waterservice.currencyprice.not.blank";

    // WaterContract
    public final static String WATERCONTRACT_NANE_NOT_BLANK = "watercontract.name.not.blank";
    public final static String WATERCONTRACT_CATEGORY_NOT_BLANK = "watercontract.category.not.blank";

    // Contracts
    public final static String CONTRACT_CODE_NOT_BLANK = "contract.number.not.blank";
    public final static String CONTRACT_TYPE_NOT_BLANK = "contract.type.not.blank";
    public final static String CONTRACT_SIGNATURE_NOT_BLANK = "contract.signature.not.blank";
    public final static String CONTRACT_DATE_NOT_BLANK = "contract.date.not.blank";
    public final static String CONTRACT_APPLICATION_NOT_BLANK = "contract.application.not.blank";

    // Meter
    public final static String  METER_CODE_NOT_BLANK = "meter.code.not.blank";
    public final static String  METER_RM_NOT_NULL = "meter.rm.not.null";
    public final static String  METER_DIAMETER_NOT_NULL = "meter.diameter.not.null";
    public final static String  METER_DIAMETER_SHOULD_BE_GREATER_THAN = "meter.diameter.should.be.greater.than";


    // PublicProperty
    public final static String PUBLIC_PROPERTY_NAME_NOT_BLANK = "publicproperty.name.not.blank";
    public final static String PUBLIC_PROPERTY_FLOWNEEDED_NOT_NULL  = "publicproperty.flowNeeded.not.null";
    public final static String PUBLIC_PROPERTY_FLOWUNIT_NOT_BLANK = "publicproperty.name.flowUnit.blank";

    // IndComProperty
    public final static String INDCOM_PROPERTY_NAME_NOT_BLANK = "indcomproperty.name.not.blank";
    public final static String INDCOM_PROPERTY_ECONOMICACTIVITYCODE_NOT_BLANK  = "indcomproperty.economicactivity.not.blank";
    public final static String INDCOM_PROPERTY_FLOWNEEDED_NOT_NULL = "indcomproperty.flowneeded.flowNeeded.not.null";
    public final static String INDCOM_PROPERTY_FLOWUNIT_NOT_BLANK = "indcomproperty.flowneeded.flowUnit.not.blank";
    public final static String INDCOM_PROPERTY_SIZE_NOT_BLANK = "indcomproperty.flowneeded.size.not.blank";

    // DomesticProperty
    public final static String DOMESTIC_PROPERTY_TYPE_NOT_BLANK = "domesticproperty.type.not.blank";
    public final static String DOMESTIC_PROPERTY_AREA_NOT_NULL  = "domesticproperty.economicactivity.not.null";
    public final static String DOMESTIC_PROPERTY_AREA_UNIT_NOT_BLANK = "domesticproperty.flowneeded.flowNeeded.not.blank";
    public final static String DOMESTIC_PROPERTY_FAMILY_SIZE_NOT_NULL = "domesticproperty.flowneeded.flowUnit.not.null";
    public final static String DOMESTIC_PROPERTY_NUMBER_OF_ROOMS_WITH_SUPPLY_NOT_NULL = "domesticproperty.flowneeded.size.not.null";
    public final static String DOMESTIC_PROPERTY_TOTAL_NUMBER_OF_ROOMS_NOT_NULL = "domesticproperty.flowneeded.size.not.null";
    public final static String DOMESTIC_PROPERTY_TOTAL_NUMBER_OF_FLAT_NOT_NULL = "domesticproperty.flowneeded.size.not.null";
    public final static String DOMESTIC_PROPERTY_AREA_OF_FLAT_NOT_NULL = "domesticproperty.flowneeded.size.not.null";
    // Application
    public final static String APPLICATION_CLIENT_ID_CARD_NOT_BLANK = "application.applicationClientIdCard.not.blank";
    public final static String APPLICATION_CLIENT_FULL_NAME_NOT_BLANK= "application.clientfullname.not.blank";
    public final static String APPLICATION_TYPE_NOT_BLANK = "application.type.not.blank";
    public final static String APPLICATION_PUBLIC_PROPERTY_NAME_NOT_BLANK = "application.publicpropertyname.not.blank";
                            // public final static String APPLICATION_PUBLIC_PROPERTY_FLOW_NEEDED_NOT_BLANK = "application.propertyflowneeded.not.blank";
    public final static String APPLICATION_PUBLIC_PROPERTY_FLOW_SHOULD_BE_GREATER_THAN = "application.flow.should.be.greater.than";
                             //indcom
    public final static String APPLICATION_INDCOM_PROPERTY_NAME_NOT_BLANK = "application.indcompropertyname.not.blank";
    public final static String APPLICATION_INDCOM_PROPERTY_ECONOMIC_ACTIVITY_CODE_NOT_BLANK = "application.indcompropertyeconomicactivitycode.not.blank";
    public final static String APPLICATION_INDCOM_PROPERTY_FLOW_SHOULD_BE_GREATER_THAN = "application.flow.should.should.be.greater.than";
                             //domestic
    public final static String APPLICATION_DOMESTIC_PROPERTY_FLOW_SHOULD_BE_GREATER_THAN = "application.area.should.be.greater.than";
    public final static String APPLICATION_DOMESTIC_PROPERTY_TYPE_NOT_BLANK = "application.domesticpropertytype.not.blank";
                            //services
    public final static String APPLICATION_SERVICES_NOT_EMPTY = "application.services.not.empty";
                            // Field
    public final static String FIELD_NAME_NOT_NULL = "field.fieldName.not.null";
    public final static String FIELD_DB_TABLE_NOT_NULL= "field.dbTable.not.null";
    public final static String FIELD_DB_FIELD_NOT_NULL= "field.dbField.not.null";
    public final static String FIELD_DB_EXCEL_COLUMN_NOT_NULL = "field.dbExcelColumn.not.null";
    public final static String FIELD_WEB_SERVICE_KEY_NOT_NULL= "field.webServiceKey.not.null";
    public final static String FIELD_CREATED_AT_NOT_BLANK= "field.createdAt.not.blank";
    public final static String FIELD_UPDATED_AT_NOT_BLANK = "field.updatedAt.not.blank";
    // Configuration
    public final static String CONFIGURATION_NAME_NOT_BLANK = "configuration.configurationname.not.blank";
    public final static String CONFIGURATION_FOR_NOT_BLANK = "configuration.for.not.blank";

    //Action
    //public final static String ACTION_CODE_NOT_BLANK = "action.code.not.blank";
    public final static String ACTION_UPDATE_date_NOT_BLANK = "action.updateDate.not.blank";
    public final static String ACTION_CREATION_date_NOT_BLANK = "action.creationDate.not.blank";

    //Department
    //public final static String DEPARTMENT_CODE_NOT_BLANK = "department.code.not.blank";
    public final static String DEPARTMENT_UPDATE_date_NOT_BLANK = "department.updateDate.not.blank";
    public final static String DEPARTMENT_CREATION_date_NOT_BLANK = "department.creationDate.not.blank";
    //Internal Project
    //CommercialOperationStatus
    public final static String COMMERCIALOPERATIONSTATUS_NAME_NOT_BLANK = "operation status name can not be blank";
    public final static String COMMERCIALOPERATIONSTATUS_CODE_NOT_BLANK = "operation status code can not be blank";
    public final static String COMMERCIALOPERATIONSTATUS_PERCENTAGE_NOT_BLANK = "operation status percentage can not be blank";
    //ServiceType
    public final static String SERVICETYPE_NAME_NOT_BLANK = "service type name can not be blank";

    //ContractType
    public final static String CONTRACTTYPE_NAME_NOT_BLANK = "contract type name can not be blank";
    public final static String CONTRACTTYPE_CODE_NOT_BLANK = "contract type code can not be blank";

    //AbsenceType
    public final static String ABSENCETYPE_NAME_NOT_BLANK = "absence type name can not be blank";
    public final static String ABSENCETYPE_CODE_NOT_BLANK = "absence type code can not be blank";

    //LegalCategoryType
    public final static String LEGALCATEGORYTYPE_NAME_NOT_BLANK = "legal category type name can not be blank";
    public final static String LEGALCATEGORYTYPE_FUNCTIONS_NOT_BLANK = "legal category code can not be blank";

    //LocalBankHoliday
    public final static String LOCALBANKHOLIDAY_NAME_NOT_BLANK = "local bank holiday name can not be blank";
    public final static String LOCALBANKHOLIDAY_CODE_NOT_BLANK = "local bank holiday code can not be blank";

    //FunctionalStructureLevel
    public final static String FUNCTIONALSTRUCTURELEVEL_NAME_NOT_BLANK = "level name can not be blank";
    public final static String FUNCTIONALSTRUCTURELEVEL_TYPE_NOT_BLANK = "level type can not be blank";

    //AdministrativeStructureLevel
    public final static String ADMINISTRATIVESTRUCTURELEVEL_NAME_NOT_BLANK = "level name can not be blank";
    public final static String ADMINISTRATIVESTRUCTURELEVEL_TYPE_NOT_BLANK = "level type can not be blank";

    // Contact
    public final static String CONTACT_FIRSTNAME_NOT_BLANK = "Contact first name can not be blank";
    public final static String CONTACT_POSITION_NOT_BLANK = "Contact position can not be blank";
    public final static String CONTACT_COMPANYEMAIL_NOT_BLANK = "Contact company email can not be blank";
    public final static String CONTACT_PERSONALEMAIL_NOT_BLANK = "Contact personal email can not be blank";
    public final static String CONTACT_COMPANY_NOT_BLANK = "Contact company  can not be blank";
    public final static String CONTACT_CITY_NOT_BLANK = "Contact city  can not be blank";

    //Assignment Type
    public final static String ASSIGNMENT_TYPE_CODE_NOT_BLANK = "assignment.type.code.not.blank";
    public final static String ASSIGNMENT_TYPE_NAME_NOT_BLANK = "assignment.type.name.not.blank";

    //Staff Expenses Type
    public final static String STAFF_EXPENSE_TYPE_CODE_NOT_BLANK = "staffexpensetype.code.not.blank";
    public final static String STAFF_EXPENSE_TYPE_NAME_NOT_BLANK = "staffexpensetype.name.not.blank";

    //Staff Expenses Subtype
    public final static String STAFF_EXPENSE_SUBTYPE_CODE_NOT_BLANK = "staffexpensesubtype.code.not.blank";
    public final static String STAFF_EXPENSE_SUBTYPE_NAME_NOT_BLANK = "staffexpensesubtype.name.not.blank";

    //Person Type
    public final static String PERSON_TYPE_CODE_NOT_BLANK = "persontype.code.not.blank";
    public final static String PERSON_TYPE_NAME_NOT_BLANK = "persontype.name.not.blank";

    //Voucher Type
    public final static String VOUCHER_TYPE_CODE_NOT_BLANK = "vouchertype.code.not.blank";
    public final static String VOUCHER_TYPE_NAME_NOT_BLANK = "vouchertype.name.not.blank";

    //Business Expenses Type
    public final static String BUSINESS_EXPENSE_TYPE_CODE_NOT_BLANK = "businessexpensetype.code.not.blank";
    public final static String BUSINESS_EXPENSE_TYPE_NAME_NOT_BLANK = "businessexpensetype.name.not.blank";

    //Business Expenses Subtype
    public final static String BUSINESS_EXPENSE_SUBTYPE_CODE_NOT_BLANK = "businessexpensesubtype.code.not.blank";
    public final static String BUSINESS_EXPENSE_SUBTYPE_NAME_NOT_BLANK = "businessexpensesubtype.name.not.blank";

    //Request Status
    public final static String REQUEST_STATUS_CODE_NOT_BLANK = "requeststatus.code.not.blank";
    public final static String REQUEST_STATUS_NAME_NOT_BLANK = "requeststatus.name.not.blank";

    //Expense Status
    public final static String EXPENSE_STATUS_CODE_NOT_BLANK = "expensestatus.code.not.blank";
    public final static String EXPENSE_STATUS_NAME_NOT_BLANK = "expensestatus.name.not.blank";

    //Email Address
    public final static String EMAIL_ADDRESS_NOT_BLANK = "emailaddress.not.blank";

    // Static messages, do not modify them
    private static HashMap<String, String> backendTranslation = new HashMap<>();
    private final static String ADDED_SENTENCE = " has been added successfully";
    private final static String UPDATED_SENTENCE = " has been updated successfully";
    private final static String DELETED_SENTENCE = " has been deleted successfully";
    private final static String ENTITY_NOT_FOUND_SENTENCE = " not found";
    private final static String DUPLICATE_ENTITY_SENTENCE = " already exists";
    private final static String APPLICATION_ALREADY_HAS_A_SENTENCE = "application already has a ";
    private final static String APPLICATION_Type_AND_WATER_CONTRACT_CATEGORY_ARE_DIFFERENT_SENTENCE = "application type and water contract category are different";
    private final static String PUBLIC_PROPERTY_ALREADY_HAS_A_METER_SENTENCE = "public property has already meter";
    private final static String DOMESTIC_PROPERTY_ALREADY_HAS_A_METER_SENTENCE = "domestic property has already meter";
    private final static String SECTOR_RELATED_TO_CLIENT_SENTENCE = "you can't delete this sector because its related to client";
    private final static String IMPORTATION_STAFF_NOTE_EXIST_SENTENCE = "Staff not exist in our data base";
    private final static String STAFF_NOT_ASIGNED_TO_COMMERCIAL_LEVEL_SENTENCE = "Staff is not assigned to a commercial level !";
    private final static String INDCOM_PROPERTY_ALREADY_HAS_A_METER_SENTENCE = "industrial commercial property has already meter";
    private final static String METER_DIAMETER_NOT_NULL_SENTENCE = "meter diameter can't be null";
    private final static String CONFIGURATION_DATABASE_TABLE_NAME_NOT_BLANK_SENTENCE = "data base table name can't be blank";
    private final static String CODE_SHOULD_NOT_CONTAIN_SPACES_SENTENCE = "code shouldn't contain spaces";
    private final static String NAME_SHOULD_NOT_CONTAIN_SPACES_SENTENCE = "name shouldn't contain spaces";
    /**
     *  get key & value for translation for the standard error messages
     * @param entityType entityType
     * @param entityName entityName
     * @return HashMap<String, String>
     */
    private static HashMap<String, String> getKeyValueTranslation(EntityType entityType, String entityName) {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.ADDED), entityName + ADDED_SENTENCE);
        hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.UPDATED), entityName + UPDATED_SENTENCE);
        hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.DELETED), entityName + DELETED_SENTENCE);
        hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.ENTITY_NOT_FOUND), entityName + ENTITY_NOT_FOUND_SENTENCE);
        hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.DUPLICATE_ENTITY), entityName + DUPLICATE_ENTITY_SENTENCE);
        if(entityType == SectorCompany) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.SECTOR_RELATED_TO_CLIENT),""+ SECTOR_RELATED_TO_CLIENT_SENTENCE + "");
        }
        if(entityType == Staff) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.IMPORTATION_STAFF_NOTE_EXIST),""+ IMPORTATION_STAFF_NOTE_EXIST_SENTENCE + "");
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.STAFF_NOT_ASIGNED_TO_COMMERCIAL_LEVEL),""+ STAFF_NOT_ASIGNED_TO_COMMERCIAL_LEVEL_SENTENCE + "");
        }
        if(entityType == Department || entityType == Action) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.CODE_SHOULD_NOT_CONTAIN_SPACES), CODE_SHOULD_NOT_CONTAIN_SPACES_SENTENCE + "");
        }
        if(entityType == Role ) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.Name_SHOULD_NOT_CONTAIN_SPACES), NAME_SHOULD_NOT_CONTAIN_SPACES_SENTENCE + "");
        }
        /*if(entityType == Contract) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.APPLICATION_ALREADY_HAS_A_CONTRACT), APPLICATION_ALREADY_HAS_A_SENTENCE + entityName);
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.APPLICATION_Type_AND_WATER_CONTRACT_CATEGORY_ARE_DIFFERENT), APPLICATION_Type_AND_WATER_CONTRACT_CATEGORY_ARE_DIFFERENT_SENTENCE + "");
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.METER_DIAMETER_CAN_T_BE_NULL), METER_DIAMETER_NOT_NULL_SENTENCE + "");

        }
        if(entityType == PublicProperty) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.PUBLIC_PROPERTY_ALREADY_HAS_A_METER), PUBLIC_PROPERTY_ALREADY_HAS_A_METER_SENTENCE + "");
        }
        if(entityType == DomesticProperty) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.DOMESTIC_PROPERTY_ALREADY_HAS_A_METER), DOMESTIC_PROPERTY_ALREADY_HAS_A_METER_SENTENCE + "");
        }
        if(entityType == IndComProperty) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.INDCOM_PROPERTY_ALREADY_HAS_A_METER), INDCOM_PROPERTY_ALREADY_HAS_A_METER_SENTENCE + "");
        }
        if(entityType == Configuration) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.CONFIGURATION_DATABASE_TABLE_NAME_NOT_BLANK), CONFIGURATION_DATABASE_TABLE_NAME_NOT_BLANK_SENTENCE + "");
        }
        if(entityType == Department) {
            hashMap.put(MainException.getMessageTemplate(entityType, ExceptionType.CODE_SHOULD_NOT_CONTAIN_SPACES), CODE_SHOULD_NOT_CONTAIN_SPACES_SENTENCE + "");
        }*/

        return hashMap;
    }
    /**
     * Concat one word of the model attribute and "cannot be blank"
     *
     * @param attribute attribute of a model
     * @return String
     */
    private static String getSentence1NotBlankValue(String attribute) {
        return attribute.split("\\.")[1] + " cannot be blank";
    }
    /**
     * Concat the model attribute and "cannot be blank"
     *
     * @param attribute attribute of a model
     * @return String
     */
    private static String getNotBlankValue(String attribute) {
        return attribute.split("\\.")[1] + " cannot be blank";
    }

    /**
     * Concat the model attribute and "should be more than 0"
     *
     * @param attribute attribute of a model
     * @return String
     */
    private static String getMinValue(String attribute) {
        return attribute.split("\\.")[1] + " should be more than 0";
    }
    /**
     * Concat the model attribute and "cannot be null"
     *
     * @param attribute attribute of a model
     * @return String
     */
    private static String getNotNullValue(String attribute) {
        return attribute.split("\\.")[1] + " cannot be null";
    }

    // put the key values of the messages

    /**
     * Collect all backend validation messages for translation purposes
     *
     * @return HashMap<String, String>
     */
    public static HashMap<String, String> getValidationConstants() {

        // Auth
        backendTranslation.put(AUTH_WRONG_CREDENTIALS, "wrong email or password");

        // DefaultSentence
       // getKeyValueTranslation(DefaultSentence, "default sentence").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(DEFAULTSENTENCE_CODE_NOT_BLANK, getNotBlankValue(DEFAULTSENTENCE_CODE_NOT_BLANK));
        backendTranslation.put(DEFAULTSENTENCE_VALUE_NOT_BLANK, getNotBlankValue(DEFAULTSENTENCE_VALUE_NOT_BLANK));

        // TranslateSentence
        //getKeyValueTranslation(TranslateSentence, "translate sentence").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(TRANSLATESENTENCE_COUNTRY_LANGUAGE_CODE_NOT_BLANK, getNotBlankValue(TRANSLATESENTENCE_COUNTRY_LANGUAGE_CODE_NOT_BLANK));
        backendTranslation.put(TRANSLATESENTENCE_DEFAULT_SENTENCE_CODE_NOT_BLANK, getNotBlankValue(TRANSLATESENTENCE_DEFAULT_SENTENCE_CODE_NOT_BLANK));
        backendTranslation.put(TRANSLATESENTENCE_TRANSLATION_NOT_BLANK, getNotBlankValue(TRANSLATESENTENCE_TRANSLATION_NOT_BLANK));

        // MeasurementUnit
       // getKeyValueTranslation(MeasurementUnit, "measurement unit").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(MEASUREMENTUNIT_NAME_NOT_BLANK, getNotBlankValue(MEASUREMENTUNIT_NAME_NOT_BLANK));
        backendTranslation.put(MEASUREMENTUNIT_SYMBOL_NOT_BLANK, getNotBlankValue(MEASUREMENTUNIT_SYMBOL_NOT_BLANK));
        backendTranslation.put(MEASUREMENTUNIT_METRIC_NOT_NULL, getNotBlankValue(MEASUREMENTUNIT_METRIC_NOT_NULL));
        backendTranslation.put(MEASUREMENTUNIT_MEASUREMENT_NOT_BLANK, getNotBlankValue(MEASUREMENTUNIT_MEASUREMENT_NOT_BLANK));
        backendTranslation.put(MEASUREMENTUNIT_INUSE_NOT_NULL, getNotBlankValue(MEASUREMENTUNIT_INUSE_NOT_NULL));

        // WaterService
       // getKeyValueTranslation(WaterService, "water service").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(WATERSERVICE_TYPE_NOT_BLANK, getNotBlankValue(WATERSERVICE_TYPE_NOT_BLANK));
        backendTranslation.put(WATERSERVICE_NAME_NOT_BLANK, getNotBlankValue(WATERSERVICE_NAME_NOT_BLANK));
        backendTranslation.put(WATERSERVICE_PRICE_NOT_NULL, getNotBlankValue(WATERSERVICE_PRICE_NOT_NULL));
        backendTranslation.put(WATERSERVICE_PRICEPER_NOT_NULL, getNotBlankValue(WATERSERVICE_PRICEPER_NOT_NULL));
        backendTranslation.put(WATERSERVICE_UNIT_NOT_BLANK, getNotBlankValue(WATERSERVICE_UNIT_NOT_BLANK));
        backendTranslation.put(WATERSERVICE_DATE_NOT_BLANK, getNotBlankValue(WATERSERVICE_DATE_NOT_BLANK));
        backendTranslation.put(WATERSERVICE_CURRENCYPRICE_NOT_BLANK, getNotBlankValue(WATERSERVICE_CURRENCYPRICE_NOT_BLANK));

        // WaterContract
       // getKeyValueTranslation(WaterContract, "water contract").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(WATERCONTRACT_NANE_NOT_BLANK, getNotBlankValue(WATERCONTRACT_NANE_NOT_BLANK));
        backendTranslation.put(WATERCONTRACT_CATEGORY_NOT_BLANK, getNotBlankValue(WATERCONTRACT_CATEGORY_NOT_BLANK));

        // Contract
       // getKeyValueTranslation(Contract, "contract").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(CONTRACT_CODE_NOT_BLANK, getNotBlankValue(CONTRACT_CODE_NOT_BLANK));
        backendTranslation.put(CONTRACT_TYPE_NOT_BLANK, getNotBlankValue(CONTRACT_TYPE_NOT_BLANK));
        backendTranslation.put(CONTRACT_SIGNATURE_NOT_BLANK, getNotBlankValue(CONTRACT_SIGNATURE_NOT_BLANK));
        backendTranslation.put(CONTRACT_DATE_NOT_BLANK, getNotBlankValue(CONTRACT_DATE_NOT_BLANK));
        backendTranslation.put(CONTRACT_APPLICATION_NOT_BLANK, getNotBlankValue(CONTRACT_APPLICATION_NOT_BLANK));


        // Meter
       // getKeyValueTranslation(Meter, "meter").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(METER_CODE_NOT_BLANK, getNotBlankValue(METER_CODE_NOT_BLANK));
        backendTranslation.put(METER_RM_NOT_NULL, getNotNullValue(METER_RM_NOT_NULL));
        backendTranslation.put(METER_DIAMETER_NOT_NULL, getNotNullValue(METER_DIAMETER_NOT_NULL));
        backendTranslation.put(METER_DIAMETER_SHOULD_BE_GREATER_THAN, getMinValue(METER_DIAMETER_SHOULD_BE_GREATER_THAN));

        // PublicProperty
       // getKeyValueTranslation(PublicProperty, "public property").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(PUBLIC_PROPERTY_NAME_NOT_BLANK, getNotBlankValue(PUBLIC_PROPERTY_NAME_NOT_BLANK));
        backendTranslation.put(PUBLIC_PROPERTY_FLOWNEEDED_NOT_NULL, getNotBlankValue(PUBLIC_PROPERTY_FLOWNEEDED_NOT_NULL));
        backendTranslation.put(PUBLIC_PROPERTY_FLOWUNIT_NOT_BLANK, getNotBlankValue(PUBLIC_PROPERTY_FLOWUNIT_NOT_BLANK));

        // IndComProperty
      //  getKeyValueTranslation(IndComProperty, "indcom property").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(INDCOM_PROPERTY_NAME_NOT_BLANK, getNotBlankValue(INDCOM_PROPERTY_NAME_NOT_BLANK));
        backendTranslation.put(INDCOM_PROPERTY_ECONOMICACTIVITYCODE_NOT_BLANK, getNotBlankValue(INDCOM_PROPERTY_ECONOMICACTIVITYCODE_NOT_BLANK));
        backendTranslation.put(INDCOM_PROPERTY_FLOWNEEDED_NOT_NULL, getNotBlankValue(INDCOM_PROPERTY_FLOWNEEDED_NOT_NULL));
        backendTranslation.put(INDCOM_PROPERTY_FLOWUNIT_NOT_BLANK, getNotBlankValue(INDCOM_PROPERTY_FLOWUNIT_NOT_BLANK));
        backendTranslation.put(INDCOM_PROPERTY_SIZE_NOT_BLANK, getNotBlankValue(INDCOM_PROPERTY_SIZE_NOT_BLANK));

        // DomesticProperty
      //  getKeyValueTranslation(DomesticProperty, "domestic property").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(DOMESTIC_PROPERTY_TYPE_NOT_BLANK, getNotBlankValue(DOMESTIC_PROPERTY_TYPE_NOT_BLANK));
        backendTranslation.put(DOMESTIC_PROPERTY_AREA_NOT_NULL, getNotBlankValue(DOMESTIC_PROPERTY_AREA_NOT_NULL));
        backendTranslation.put(DOMESTIC_PROPERTY_AREA_UNIT_NOT_BLANK, getNotBlankValue(DOMESTIC_PROPERTY_AREA_UNIT_NOT_BLANK));
        backendTranslation.put(DOMESTIC_PROPERTY_FAMILY_SIZE_NOT_NULL, getNotBlankValue(DOMESTIC_PROPERTY_FAMILY_SIZE_NOT_NULL));
        backendTranslation.put(DOMESTIC_PROPERTY_NUMBER_OF_ROOMS_WITH_SUPPLY_NOT_NULL, getNotBlankValue(DOMESTIC_PROPERTY_NUMBER_OF_ROOMS_WITH_SUPPLY_NOT_NULL));
        backendTranslation.put(DOMESTIC_PROPERTY_TOTAL_NUMBER_OF_ROOMS_NOT_NULL, getNotBlankValue(DOMESTIC_PROPERTY_TOTAL_NUMBER_OF_ROOMS_NOT_NULL));
        backendTranslation.put(DOMESTIC_PROPERTY_TOTAL_NUMBER_OF_FLAT_NOT_NULL, getNotBlankValue(DOMESTIC_PROPERTY_TOTAL_NUMBER_OF_FLAT_NOT_NULL));
        backendTranslation.put(DOMESTIC_PROPERTY_AREA_OF_FLAT_NOT_NULL, getNotBlankValue(DOMESTIC_PROPERTY_AREA_OF_FLAT_NOT_NULL));
        // Role
        getKeyValueTranslation(Role, "role").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(ROLE_NAME_NOT_BLANK, getSentence1NotBlankValue(ROLE_NAME_NOT_BLANK));
        // Application
        //getKeyValueTranslation(Application, "application").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(APPLICATION_CLIENT_ID_CARD_NOT_BLANK, getNotBlankValue(APPLICATION_CLIENT_ID_CARD_NOT_BLANK));
        backendTranslation.put(APPLICATION_CLIENT_FULL_NAME_NOT_BLANK, getNotBlankValue(APPLICATION_CLIENT_FULL_NAME_NOT_BLANK));
        backendTranslation.put(APPLICATION_TYPE_NOT_BLANK, getNotBlankValue(APPLICATION_TYPE_NOT_BLANK));
        backendTranslation.put(APPLICATION_PUBLIC_PROPERTY_NAME_NOT_BLANK, getNotBlankValue(APPLICATION_PUBLIC_PROPERTY_NAME_NOT_BLANK));
       // backendTranslation.put(APPLICATION_PUBLIC_PROPERTY_FLOW_NEEDED_NOT_BLANK, getNotBlankValue(APPLICATION_PUBLIC_PROPERTY_FLOW_NEEDED_NOT_BLANK));
        backendTranslation.put(APPLICATION_PUBLIC_PROPERTY_FLOW_SHOULD_BE_GREATER_THAN, getMinValue(APPLICATION_PUBLIC_PROPERTY_FLOW_SHOULD_BE_GREATER_THAN));

        backendTranslation.put(APPLICATION_INDCOM_PROPERTY_NAME_NOT_BLANK, getNotBlankValue(APPLICATION_INDCOM_PROPERTY_NAME_NOT_BLANK));
        backendTranslation.put(APPLICATION_INDCOM_PROPERTY_ECONOMIC_ACTIVITY_CODE_NOT_BLANK, getNotBlankValue(APPLICATION_INDCOM_PROPERTY_ECONOMIC_ACTIVITY_CODE_NOT_BLANK));
        backendTranslation.put(APPLICATION_INDCOM_PROPERTY_FLOW_SHOULD_BE_GREATER_THAN, getMinValue(APPLICATION_INDCOM_PROPERTY_FLOW_SHOULD_BE_GREATER_THAN));


        backendTranslation.put(APPLICATION_DOMESTIC_PROPERTY_FLOW_SHOULD_BE_GREATER_THAN, getMinValue(APPLICATION_DOMESTIC_PROPERTY_FLOW_SHOULD_BE_GREATER_THAN));
        backendTranslation.put(APPLICATION_DOMESTIC_PROPERTY_TYPE_NOT_BLANK, getNotBlankValue(APPLICATION_DOMESTIC_PROPERTY_TYPE_NOT_BLANK));

        backendTranslation.put(APPLICATION_SERVICES_NOT_EMPTY, getNotBlankValue(APPLICATION_SERVICES_NOT_EMPTY));
        // Configuration
       // getKeyValueTranslation(Configuration, "configuration").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(CONFIGURATION_NAME_NOT_BLANK, getNotBlankValue(CONFIGURATION_NAME_NOT_BLANK));
        backendTranslation.put(CONFIGURATION_FOR_NOT_BLANK, getNotBlankValue(CONFIGURATION_FOR_NOT_BLANK));
        // Field
       // getKeyValueTranslation(Field, "field").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(FIELD_NAME_NOT_NULL, getNotBlankValue(FIELD_NAME_NOT_NULL));
        backendTranslation.put(FIELD_DB_TABLE_NOT_NULL, getNotBlankValue(FIELD_DB_TABLE_NOT_NULL));
        backendTranslation.put(FIELD_DB_FIELD_NOT_NULL, getNotBlankValue(FIELD_DB_FIELD_NOT_NULL));
        backendTranslation.put(FIELD_DB_EXCEL_COLUMN_NOT_NULL, getNotBlankValue(FIELD_DB_EXCEL_COLUMN_NOT_NULL));
        backendTranslation.put(FIELD_WEB_SERVICE_KEY_NOT_NULL, getNotBlankValue(FIELD_WEB_SERVICE_KEY_NOT_NULL));
        backendTranslation.put(FIELD_CREATED_AT_NOT_BLANK, getNotBlankValue(FIELD_CREATED_AT_NOT_BLANK));
        backendTranslation.put(FIELD_UPDATED_AT_NOT_BLANK, getNotBlankValue(FIELD_UPDATED_AT_NOT_BLANK));
        // Action
       // getKeyValueTranslation(Action, "action").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(ACTION_CODE_NOT_BLANK, getNotBlankValue(ACTION_CODE_NOT_BLANK));
        // Department
       // getKeyValueTranslation(Department, "department").forEach(backendTranslation::putIfAbsent);
        backendTranslation.put(DEPARTMENT_CODE_NOT_BLANK, getNotBlankValue(DEPARTMENT_CODE_NOT_BLANK));
        //internal project
        //COMMERCIALOPERATIONSTATUS
        backendTranslation.put(COMMERCIALOPERATIONSTATUS_NAME_NOT_BLANK, getNotBlankValue(COMMERCIALOPERATIONSTATUS_NAME_NOT_BLANK));
        backendTranslation.put(COMMERCIALOPERATIONSTATUS_CODE_NOT_BLANK, getNotBlankValue(COMMERCIALOPERATIONSTATUS_CODE_NOT_BLANK));
        backendTranslation.put(COMMERCIALOPERATIONSTATUS_PERCENTAGE_NOT_BLANK, getNotBlankValue(COMMERCIALOPERATIONSTATUS_PERCENTAGE_NOT_BLANK));
        //SERVICETYPE
        backendTranslation.put(SERVICETYPE_NAME_NOT_BLANK, getNotBlankValue(SERVICETYPE_NAME_NOT_BLANK));

        //CONTRACTTYPE
        backendTranslation.put(CONTRACTTYPE_NAME_NOT_BLANK, getNotBlankValue(CONTRACTTYPE_NAME_NOT_BLANK));
        backendTranslation.put(CONTRACTTYPE_CODE_NOT_BLANK, getNotBlankValue(CONTRACTTYPE_CODE_NOT_BLANK));

        //ABSENCETYPE
        backendTranslation.put(ABSENCETYPE_NAME_NOT_BLANK, getNotBlankValue(ABSENCETYPE_NAME_NOT_BLANK));
        backendTranslation.put(ABSENCETYPE_CODE_NOT_BLANK, getNotBlankValue(ABSENCETYPE_CODE_NOT_BLANK));

        //LEGALCATEGORYTYPE
        backendTranslation.put(LEGALCATEGORYTYPE_NAME_NOT_BLANK, getNotBlankValue(LEGALCATEGORYTYPE_NAME_NOT_BLANK));
        backendTranslation.put(LEGALCATEGORYTYPE_FUNCTIONS_NOT_BLANK, getNotBlankValue(LEGALCATEGORYTYPE_FUNCTIONS_NOT_BLANK));

        //LOCALBANKHOLIDAY
        backendTranslation.put(LOCALBANKHOLIDAY_NAME_NOT_BLANK, getNotBlankValue(LOCALBANKHOLIDAY_NAME_NOT_BLANK));
        backendTranslation.put(LOCALBANKHOLIDAY_CODE_NOT_BLANK, getNotBlankValue(LOCALBANKHOLIDAY_CODE_NOT_BLANK));

        //FUNCTIONALSTRUCTURELEVEL
        backendTranslation.put(FUNCTIONALSTRUCTURELEVEL_NAME_NOT_BLANK, getNotBlankValue(FUNCTIONALSTRUCTURELEVEL_NAME_NOT_BLANK));
        backendTranslation.put(FUNCTIONALSTRUCTURELEVEL_TYPE_NOT_BLANK, getNotBlankValue(FUNCTIONALSTRUCTURELEVEL_TYPE_NOT_BLANK));

        //ADMINISTRATIVESTRUCTURELEVEL
        backendTranslation.put(ADMINISTRATIVESTRUCTURELEVEL_NAME_NOT_BLANK, getNotBlankValue(ADMINISTRATIVESTRUCTURELEVEL_NAME_NOT_BLANK));
        backendTranslation.put(ADMINISTRATIVESTRUCTURELEVEL_TYPE_NOT_BLANK, getNotBlankValue(ADMINISTRATIVESTRUCTURELEVEL_TYPE_NOT_BLANK));

        //CONTACT
        backendTranslation.put(CONTACT_FIRSTNAME_NOT_BLANK, getNotBlankValue(CONTACT_FIRSTNAME_NOT_BLANK));
        backendTranslation.put(CONTACT_POSITION_NOT_BLANK, getNotBlankValue(CONTACT_POSITION_NOT_BLANK));
        backendTranslation.put(CONTACT_COMPANYEMAIL_NOT_BLANK, getNotBlankValue(CONTACT_COMPANYEMAIL_NOT_BLANK));
        backendTranslation.put(CONTACT_PERSONALEMAIL_NOT_BLANK, getNotBlankValue(CONTACT_PERSONALEMAIL_NOT_BLANK));
        backendTranslation.put(CONTACT_COMPANY_NOT_BLANK, getNotBlankValue(CONTACT_COMPANY_NOT_BLANK));
        backendTranslation.put(CONTACT_CITY_NOT_BLANK, getNotBlankValue(CONTACT_CITY_NOT_BLANK));

        // commercialProject
        return backendTranslation;
    }

    /**
     * Clear the hash map used (optimization purposes)
     *
     */
    public static void clearTheHashMap(){
        backendTranslation.clear();
    }

}
