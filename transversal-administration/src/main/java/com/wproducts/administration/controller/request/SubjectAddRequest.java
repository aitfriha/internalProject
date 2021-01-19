package com.wproducts.administration.controller.request;

import com.wproducts.administration.dto.model.SubjectDto;
import com.wproducts.administration.dto.model.SubjectFieldDto;
import com.wproducts.administration.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ValidationConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SubjectAddRequest {
    @NotBlank(message = SUBJECT_CODE_NOT_BLANK)
    private String subjectCode;
    private String subjectType;
    private String subjectDescription;
//    private List<String> subjectFields;
    private  String  subjectParent;
}
