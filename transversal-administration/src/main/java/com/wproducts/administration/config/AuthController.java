package com.wproducts.administration.config;


import com.wproducts.administration.controller.request.UserAuthRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NamingConventions;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This is a global controller written merely for showing the login and logout apis in the
 * swagger documentation allowing users to get the authorisation token from the same interface
 * and use it for executing the secured API operations.
 */

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:3001" })
public class AuthController {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR)
                .setAmbiguityIgnored(true);
        return modelMapper;
    }
    @PostMapping("/auth")
    public void login(@RequestBody @Valid UserAuthRequest userAuthRequest) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @PostMapping("/logout")
    public void logout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }
}