package com.wproducts.administration.dto.mapper;

import com.wproducts.administration.controller.request.UserAddRequest;
import com.wproducts.administration.controller.request.UserUpdateRequest;
import com.wproducts.administration.dto.model.UserDto;
import com.wproducts.administration.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Map dto to model
     *
     * @param userDto userDto
     * @return User
     */
    @Mapping(source = "userId", target="_id")
    @Mapping(target = "userDepartment", ignore=true)
    User dtoToModel(UserDto userDto);
    /**
     * Map model to dto
     *
     * @param user user
     * @return UserDto
     */
    @Mapping(source = "_id", target="userId")
    @Mapping(target = "userCreatedAt", ignore=true)
    @Mapping(target = "userUpdatedAt", ignore=true)
    @Mapping(target = "userRoles", ignore=true)
    @Mapping(target = "userDepartmentId", ignore=true)
    UserDto modelToDto(User user);

    /**
     * Map add request to dto
     *
     * @param userAddRequest userAddRequest
     * @return UserDto
     */
    @Mapping(target = "userRoles", ignore=true)
    @Mapping(target = "userDepartmentId", ignore=true)
    @Mapping(target = "userUpdatedAt", ignore=true)
    UserDto addRequestToDto(UserAddRequest userAddRequest);

    /**
     * Map update request to dto
     *
     * @param userUpdateRequest userUpdateRequest
     * @return UserDto
     */
    @Mapping(target = "userRoles", ignore=true)
    @Mapping(target = "userDepartmentId", ignore=true)
    @Mapping(target = "userCreatedAt", ignore=true)
    UserDto updateRequestToDto(UserUpdateRequest userUpdateRequest);

}
