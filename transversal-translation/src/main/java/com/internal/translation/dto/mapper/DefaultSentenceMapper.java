package com.internal.translation.dto.mapper;

import com.internal.translation.controller.request.DefaultSentenceAddListRequest;
import com.internal.translation.controller.request.DefaultSentenceAddRequest;
import com.internal.translation.controller.request.DefaultSentenceUpdateRequest;
import com.internal.translation.dto.model.DefaultSentenceDto;
import com.internal.translation.model.DefaultSentence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DefaultSentenceMapper {

    /**
     * Map dto to model
     *
     * @param defaultSentenceDto defaultSentenceDto
     * @return DefaultSentence
     */
    @Mapping(source = "defaultSentenceId", target="_id")
    DefaultSentence dtoToModel(DefaultSentenceDto defaultSentenceDto);

    /**
     * Map model to dto
     *
     * @param defaultSentence defaultSentence
     * @return DefaultSentenceDto
     */
    @Mapping(source = "_id", target="defaultSentenceId")
    DefaultSentenceDto modelToDto(DefaultSentence defaultSentence);


    /**
     * Map add request to dto
     *
     * @param defaultSentenceAddRequest defaultSentenceAddRequest
     * @return DefaultSentenceDto
     */
    DefaultSentenceDto addRequestToDto(DefaultSentenceAddRequest defaultSentenceAddRequest);

    /**
     * Map update request to dto
     *
     * @param defaultSentenceUpdateRequest defaultSentenceUpdateRequest
     * @return DefaultSentenceDto
     */
    DefaultSentenceDto updateRequestToDto(DefaultSentenceUpdateRequest defaultSentenceUpdateRequest);

    /**
     * Map add list request to dto
     *
     * @param defaultSentenceAddListRequest defaultSentenceAddListRequest
     * @return DefaultSentenceDto
     */
    DefaultSentenceDto addListRequestToDto(DefaultSentenceAddListRequest defaultSentenceAddListRequest);
}
