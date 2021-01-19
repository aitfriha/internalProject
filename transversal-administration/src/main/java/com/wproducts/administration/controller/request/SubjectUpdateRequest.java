package com.wproducts.administration.controller.request;

import com.wproducts.administration.dto.model.SubjectDto;
import com.wproducts.administration.dto.model.SubjectFieldDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import java.util.List;

import static org.techniu.isbackend.exception.ValidationConstants.SUBJECT_CODE_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SubjectUpdateRequest {
    private String subjectId;
    @NotBlank(message = SUBJECT_CODE_NOT_BLANK)
    private String subjectCode;
    private String subjectType;
    private String subjectDescription;
//    private List<SubjectFieldDto> subjectFields;
    private SubjectDto subjectParent;

}
