package org.techniu.isbackend.dto.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.techniu.isbackend.controller.request.SelectionProcessInformationAddrequest;
import org.techniu.isbackend.controller.request.SelectionProcessInformationUpdaterequest;
import org.techniu.isbackend.dto.model.SelectionProcessInformationDto;
import org.techniu.isbackend.entity.SelectionProcessInformation;

@Mapper(componentModel = "spring")
public interface SelectionProcessInformationMapper {
    /**
     * Map dto to model
     *
     * @param selectionProcessInformationDto selectionProcessInformationDto
     * @return SelectionProcessInformation
     */
    @Mapping(source = "selectionProcessId", target="_id")
    SelectionProcessInformation dtoToModel(SelectionProcessInformationDto selectionProcessInformationDto);

    /**
     * Map SelectionProcessInformation to SelectionProcessInformationDo
     *
     * @param selectionProcessInformationAddrequest selectionProcessInformationAddrequest
     * @return SelectionProcessInformationDto
     */
    SelectionProcessInformationDto addRequestToDto(SelectionProcessInformationAddrequest selectionProcessInformationAddrequest);

    /**
     * Map SelectionProcessInformation to SelectionProcessInformationDo
     *
     * @param selectionProcessInformationUpdaterequest selectionProcessInformationUpdaterequest
     * @return SelectionProcessInformationDto
     */
    SelectionProcessInformationDto updateRequestToDto(SelectionProcessInformationUpdaterequest selectionProcessInformationUpdaterequest);

    /**
     * Map selectionProcessInformation to selectionProcessInformationDo
     *
     * @param selectionProcessInformation selectionProcessInformation
     * @return SelectionProcessInformationDto
     */
    @Mapping(source = "_id", target="selectionProcessId")
    SelectionProcessInformationDto modelToDto(SelectionProcessInformation selectionProcessInformation);
}
