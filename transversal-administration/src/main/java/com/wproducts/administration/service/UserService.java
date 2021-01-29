package com.wproducts.administration.service;

import com.wproducts.administration.controller.request.UserAddRequest;
import com.wproducts.administration.controller.request.UserUpdateRequest;
import com.wproducts.administration.dto.model.ActionDto;
import com.wproducts.administration.dto.model.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    /**
     * Register a new user
     *
     * @param userAddRequest - userAddRequest
     */
    void save(UserAddRequest userAddRequest);
    /**
     * Register a new user
     *
     * @param userDto
     */
    void signup(UserDto userDto);

    /**
     * Search an existing user
     *
     * @param email
     * @return
     */
    UserDto findUserByEmail(String email);

    /**
     * Update profile of the user
     *
     * @param userUpdateRequest
     */
    void updateProfile(UserUpdateRequest userUpdateRequest);

    /**
     * Update password
     *
     * @param newPassword
     */
    void changePassword(UserDto userDto, String newPassword);

    /**
     * oneUser
     *
     * @return One UserDto
     */
    UserDto getOneUser(String id);
    /**
     * oneUser
     *
     * @return One UserDto
     */
    UserDto getOneUserByEmail(String id);

    void sendPassword(String userEmail);
    /**
     * all Users
     *
     * @return List UserDto
     */
    List<UserDto> getAllUsers();

    /**
     * delete User
     *
     * @param id - id
     */
    void removeUser(String id);
}
