package com.wproducts.administration.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserSignupRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
    private String fullName;

    private String mobileNumber;
}
