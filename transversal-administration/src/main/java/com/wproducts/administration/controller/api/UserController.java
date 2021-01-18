package com.wproducts.administration.controller.api;

import com.wproducts.administration.controller.request.ActionAddRequest;
import com.wproducts.administration.controller.request.UserAddRequest;
import com.wproducts.administration.controller.request.UserSignupRequest;
import com.wproducts.administration.controller.request.UserUpdateRequest;
import com.wproducts.administration.dto.mapper.DepartmentMapper;
import com.wproducts.administration.dto.mapper.UserMapper;
import com.wproducts.administration.dto.model.UserDto;
import com.wproducts.administration.service.UserService;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.Response;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.techniu.isbackend.exception.EntityType.USER;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = { "http://localhost:3001" })
public class UserController {

    private final UserService userService;

    private final MapValidationErrorService mapValidationErrorService;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    public UserController(UserService userService, MapValidationErrorService mapValidationErrorService) {
        this.userService = userService;
        this.mapValidationErrorService = mapValidationErrorService;
    }

    /**
     * Handles the incoming POST API "/action/add"
     *
     * @param userAddRequest User Add request
     * @return ActionDto
     */
    @PostMapping("/add")
    public ResponseEntity signup(@RequestBody @Valid UserAddRequest userAddRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        System.out.print("passed .....");
        userService.save(userAddRequest);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(USER, ADDED)), HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/user/signup"
     *
     * @param userSignupRequest user signup request
     * @return UserDto
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid UserSignupRequest userSignupRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        System.out.println(userSignupRequest);
        // Create userDto from userSignupRequest
        UserDto userDto = new UserDto()
                .setUserEmail(userSignupRequest.getEmail())
                .setUserPassword(userSignupRequest.getPassword())
                .setUserFullName(userSignupRequest.getFullName())
                .setUserMobileNumber(userSignupRequest.getMobileNumber());
               // .setAdmin(false);

        // Save user
        userService.signup(userDto);

        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Handles the incoming POST API "/user/signup"
     *
     * @param userUpdateRequest user update request
     * @return Response
     */
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody @Valid UserUpdateRequest userUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Update user
        System.out.println(userUpdateRequest);
        userService.updateProfile(userUpdateRequest);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(USER, UPDATED)), HttpStatus.OK);
    }

    /**
     * display an object GET API "/user/id"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getOneUser(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(userService.getOneUser(id)), HttpStatus.OK);
    }
    /**
     * display all objects GET API "/user/all"
     */
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity getAllUsers() {
        return new ResponseEntity<Response>(Response.ok().setPayload(userService.getAllUsers()), HttpStatus.OK);
    }
    /**
     * Handles the incoming DELETE API "/user/delete"
     *
     * @param id user delete request
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        userService.removeUser(id);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(USER, DELETED)), HttpStatus.OK);
    }

}
