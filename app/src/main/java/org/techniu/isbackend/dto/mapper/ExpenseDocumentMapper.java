package org.techniu.isbackend.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.dto.model.ExpenseDocumentDto;
import org.techniu.isbackend.entity.ExpenseDocument;

@Mapper(componentModel = "spring")
public interface ExpenseDocumentMapper {
    /**
     * Map dto to model
     *
     * @param expenseDocumentDto
     * @return ExpenseDocument
     */
    @Mapping(target = "data", ignore=true)
    ExpenseDocument dtoToModel(ExpenseDocumentDto expenseDocumentDto);

    /**
     * Map model to dto
     *
     * @param expenseDocument
     * @return DocDto
     */
    ExpenseDocumentDto modelToDto(ExpenseDocument expenseDocument);
}
