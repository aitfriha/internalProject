package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.FUNCTIONALSTRUCTURELEVEL_NAME_NOT_BLANK;
import static org.techniu.isbackend.exception.ValidationConstants.FUNCTIONALSTRUCTURELEVEL_TYPE_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SelectionTypeEvaluationAddrequest {

    private String name;
    private String description;
    private String type;

}
