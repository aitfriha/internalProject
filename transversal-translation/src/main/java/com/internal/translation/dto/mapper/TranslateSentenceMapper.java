package com.internal.translation.dto.mapper;

import com.internal.translation.dto.model.TranslateSentenceDto;
import com.internal.translation.model.TranslateSentence;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TranslateSentenceMapper {

    TranslateSentence dtoToModel(TranslateSentenceDto translateSentenceDto);
}
