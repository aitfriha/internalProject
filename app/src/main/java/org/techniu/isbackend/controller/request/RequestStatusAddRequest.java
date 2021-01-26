package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.REQUEST_STATUS_CODE_NOT_BLANK;
import static org.techniu.isbackend.exception.ValidationConstants.REQUEST_STATUS_NAME_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RequestStatusAddRequest {

    private String id;
    @NotBlank(message = REQUEST_STATUS_CODE_NOT_BLANK)
    private String code;
    @NotBlank(message = REQUEST_STATUS_NAME_NOT_BLANK)
    private String name;
    private String description;
    private String masterValue;
    private boolean removable = true;
}
