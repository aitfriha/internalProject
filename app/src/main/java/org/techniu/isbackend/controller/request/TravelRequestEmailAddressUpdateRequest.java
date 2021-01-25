package org.techniu.isbackend.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

import static org.techniu.isbackend.exception.ValidationConstants.EMAIL_ADDRESS_NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TravelRequestEmailAddressUpdateRequest {

    private String id;
    @NotBlank(message = EMAIL_ADDRESS_NOT_BLANK)
    private String email;
    private String module;
    private String function;
}
