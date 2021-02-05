package com.wproducts.administration.controller.api;

import com.wproducts.administration.controller.request.UserAddRequest;
import com.wproducts.administration.controller.request.UserSignupRequest;
import com.wproducts.administration.controller.request.UserUpdateRequest;
import com.wproducts.administration.dto.mapper.UserMapper;
import com.wproducts.administration.dto.model.PasswordResetTokenDto;
import com.wproducts.administration.dto.model.UserDto;
import com.wproducts.administration.model.PasswordResetToken;
import com.wproducts.administration.repository.PasswordResetTokenRepository;
import com.wproducts.administration.service.UserService;
import org.springframework.web.servlet.ModelAndView;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.Response;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Calendar;

import static org.techniu.isbackend.exception.EntityType.USER;
import static org.techniu.isbackend.exception.ExceptionType.*;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = { "*" })
public class UserController {

    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final MapValidationErrorService mapValidationErrorService;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    public UserController(UserService userService, PasswordResetTokenRepository passwordResetTokenRepository, MapValidationErrorService mapValidationErrorService) {
        this.userService = userService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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
     * Handles the incoming POST API "/user/signup"
     *
     * @return Response
     */
    @RequestMapping(method = RequestMethod.GET, value = "/forgetPassword/{userEmail}")
    public ResponseEntity forgetPassword(@PathVariable String userEmail) {
        userService.sendPassword(userEmail);
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(USER, SENT)), HttpStatus.OK);
    }
    /**
     * Handles the incoming POST API "/user/token"
     *
     * @return Response
     */
    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@RequestBody @Valid PasswordResetTokenDto passwordResetTokenDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        String result = this.validatePasswordResetToken(passwordResetTokenDto.getToken());
        System.out.println(result);
        if(result != null) {
            /*String message = messages.getMessage("auth.message." + result, null, locale);
            return "redirect:/login.html?lang="
                    + locale.getLanguage() + "&message=" + message;*/
        }
        userService.editPassword(passwordResetTokenDto.getToken(),passwordResetTokenDto.getPassword());
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(USER, SENT)), HttpStatus.OK);
    }
    /**
     * Handles the incoming POST API "/user/token"
     *
     * @return Response
     */
    @RequestMapping(method = RequestMethod.GET, value = "/resetPassword")
    public ModelAndView  resertPassword(@RequestParam String token) {
        String result = this.validatePasswordResetToken(token);
        System.out.println("wwwwwwwwwwwwwwwwwwwww :"+result);
        if(result != null) {
            /*String message = messages.getMessage("auth.message." + result, null, locale);
            return "redirect:/login.html?lang="
                    + locale.getLanguage() + "&message=" + message;*/
        }
        return new ModelAndView("redirect:" + "http://localhost:3001/reset-password?token="+token);
     //   return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(USER, SENT)), HttpStatus.OK);
    }
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        if(passToken == null){
            throw exception(INVALID_TOKEN);
        }
        return passToken != null;
    }
    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args          args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.USER, exceptionType, args);
    }
    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
         if(passToken.getExpiryDate().before(cal.getTime())==true){
             throw exception(EXPIRED_TOKEN);
         }
        return passToken.getExpiryDate().before(cal.getTime());
    }

        /**
         * display an object GET API "/user/id"
         */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getOneUser(@PathVariable String id) {
        return new ResponseEntity<Response>(Response.ok().setPayload(userService.getOneUser(id)), HttpStatus.OK);
    }

    /**
     * display an object GET API "/user/email
     */
    @RequestMapping(method = RequestMethod.GET, value = "/byemail/{email}")
    public ResponseEntity getOneUserByemail(@PathVariable String email) {
        return new ResponseEntity<Response>(Response.ok().setPayload(userService.findUserByEmail(email)), HttpStatus.OK);
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
