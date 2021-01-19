package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.SelectionTypeEvaluationAddrequest;
import org.techniu.isbackend.controller.request.SelectionTypeEvaluationUpdaterequest;
import org.techniu.isbackend.dto.model.SelectionTypeEvaluationDto;
import org.techniu.isbackend.entity.SelectionTypeEvaluation;

@Mapper(componentModel = "spring")
public interface SelectionTypeEvaluationMapper {
    /**
     * Map dto to model
     *
     * @param selectionTypeEvaluationDto selectionTypeEvaluationDto
     * @return SelectionTypeEvaluation
     */
    @Mapping(source = "selectionTypeId", target="_id")
    SelectionTypeEvaluation dtoToModel(SelectionTypeEvaluationDto selectionTypeEvaluationDto);

    /**
     * Map SelectionTypeEvaluation to SelectionTypeEvaluationDo
     *
     * @param selectionTypeEvaluationAddrequest selectionTypeEvaluationAddrequest
     * @return SelectionTypeEvaluationDto
     */
    SelectionTypeEvaluationDto addRequestToDto(SelectionTypeEvaluationAddrequest selectionTypeEvaluationAddrequest);

    /**
     * Map SelectionTypeEvaluation to SelectionTypeEvaluationDo
     *
     * @param selectionTypeEvaluationUpdaterequest selectionTypeEvaluationUpdaterequest
     * @return SelectionTypeEvaluationDto
     */
    SelectionTypeEvaluationDto updateRequestToDto(SelectionTypeEvaluationUpdaterequest selectionTypeEvaluationUpdaterequest);

    /**
     * Map selectionTypeEvaluation to selectionTypeEvaluationDo
     *
     * @param selectionTypeEvaluation selectionTypeEvaluation
     * @return SelectionTypeEvaluationDto
     */
    @Mapping(source = "_id", target="selectionTypeId")
    SelectionTypeEvaluationDto modelToDto(SelectionTypeEvaluation selectionTypeEvaluation);
}
