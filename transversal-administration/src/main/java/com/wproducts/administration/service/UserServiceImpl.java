package com.wproducts.administration.service;

import com.wproducts.administration.controller.request.UserAddRequest;
import com.wproducts.administration.controller.request.UserUpdateRequest;
import com.wproducts.administration.dto.mapper.DepartmentMapper;
import com.wproducts.administration.dto.mapper.RoleMapper;
import com.wproducts.administration.dto.mapper.UserMapper;
import com.wproducts.administration.dto.model.RoleDto;
import com.wproducts.administration.dto.model.SubjectFieldDto;
import com.wproducts.administration.dto.model.UserDto;
import com.wproducts.administration.model.*;
import com.wproducts.administration.repository.DepartmentRepository;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import com.wproducts.administration.repository.RoleRepository;
import com.wproducts.administration.repository.UserRepository;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static org.techniu.isbackend.exception.ExceptionType.DUPLICATE_ENTITY;
import static org.techniu.isbackend.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final DepartmentMapper departmentMapper = Mappers.getMapper(DepartmentMapper.class);
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);

    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, UserRepository userRepository, DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Register a new Action
     *
     * @param userAddRequest - userAddRequest
     */
    @Override
    public void save(UserAddRequest userAddRequest) {
        userAddRequest.setUserEmail(userAddRequest.getUserEmail().toLowerCase());

        Optional<User> user = Optional.ofNullable(userRepository.findByUserEmail(userAddRequest.getUserEmail()));
        Department department = departmentRepository.findByDepartmentCode(userAddRequest.getUserDepartmentId());
        if (user.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        List<Role> roles = new ArrayList<>();
        if (userAddRequest.getUserRolesIds() != null) {
            for (String s : userAddRequest.getUserRolesIds()) {
                Optional<Role> adminField = Optional.ofNullable(roleRepository.findByRoleName(s));
                adminField.ifPresent(roles::add);
            }
        }
        User user1 = userMapper.dtoToModel(userMapper.addRequestToDto(userAddRequest))
                // .setUserPassword(bCryptPasswordEncoder.encode(userAddRequest.getUserPassword()))
                .setUserDepartment(department)
                .setUserRoles(roles)
                .setUserCreatedAt(Instant.now());

        userRepository.save(user1);
    }

    @Override
    public void signup(UserDto userDto) {
        userDto.setUserEmail(userDto.getUserEmail().toLowerCase());

        Role userRole;
        User user = userRepository.findByUserEmail(userDto.getUserEmail());
        if (user == null) {
            // if (userDto.isAdmin()) {
            //     userRole = roleRepository.findByRoleName(UserRoles.ADMIN.name());
            // } else {
            userRole = roleRepository.findByRoleName(UserRoles.USER.name());
            // }
            user = new User()
                    .setUserEmail(userDto.getUserEmail())
                    .setUserPassword(bCryptPasswordEncoder.encode(userDto.getUserPassword()))
                    .setUserRoles(new HashSet<>(Collections.singletonList(userRole)))
                    .setUserFullName(userDto.getUserFullName())
                    .setUserMobileNumber(userDto.getUserMobileNumber());

            userRepository.save(user);
        }
        throw exception(ExceptionType.DUPLICATE_ENTITY, userDto.getUserEmail());
    }

    /**
     * +
     * Search an existing user
     *
     * @param email email
     * @return UserDto
     */
    public UserDto findUserByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUserEmail(email.toLowerCase()));
        if (!user.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);

        }
        return modelMapper.map(user.get(), UserDto.class);
    }

    /**
     * Update User Profile
     *
     * @param userUpdateRequest userUpdateRequest
     */
    @Override
    public void updateProfile(UserUpdateRequest userUpdateRequest) {
        userUpdateRequest.setUserEmail(userUpdateRequest.getUserEmail().toLowerCase());

        // Find user by email
        Optional<User> user = Optional.ofNullable(userRepository.findBy_id(userUpdateRequest.getUserId()));
        Department department = departmentRepository.findByDepartmentCode(userUpdateRequest.getUserDepartmentId());
        if (!user.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        Optional<User> userEmail = Optional.ofNullable(userRepository.findByUserEmail(userUpdateRequest.getUserEmail()));
        if (userEmail.isPresent() && !(user.get().getUserEmail().equals(userUpdateRequest.getUserEmail())) ) {
            throw exception(DUPLICATE_ENTITY);
        }


        // Get user model
        List<Role> roles = new ArrayList<>();
        if (userUpdateRequest.getUserRolesIds() != null) {
            for (String s : userUpdateRequest.getUserRolesIds()) {
                Optional<Role> role = Optional.ofNullable(roleRepository.findByRoleName(s));
                role.ifPresent(roles::add);
            }
        }
        User user1 = userMapper.dtoToModel(userMapper.updateRequestToDto(userUpdateRequest))
                .setUserDepartment(department)
                .setUserRoles(roles)
                .setUserCreatedAt(user.get().getUserCreatedAt())
                .setUserUpdatedAt(Instant.now());

        // save action
        userRepository.save(user1);
    }

    /**
     * Change Password
     *
     * @param userDto     userDto
     * @param newPassword new password
     */
    @Override
    public void changePassword(UserDto userDto, String newPassword) {
        // Find user by email
        Optional<User> user = Optional.ofNullable(userRepository.findByUserEmail(userDto.getUserEmail()));
        if (user.isPresent()) {
            User userModel = user.get();
            userModel.setUserPassword(bCryptPasswordEncoder.encode(newPassword));

            userRepository.save(userModel);
        }
        throw exception(ExceptionType.ENTITY_NOT_FOUND, userDto.getUserEmail());
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

    /**
     * one UserDto
     *
     * @param id - id
     * @return UserDto
     */
    @Override
    public UserDto getOneUser(String id) {
        // Get all actions
        User user = userRepository.findBy_id(id);
        UserDto userDto = userMapper.modelToDto(user);
        if (user.getUserUpdatedAt() != null) {
            userDto.setUserUpdatedAt(user.getUserUpdatedAt().toString());
        }
        if (user.getUserCreatedAt() != null) {
            userDto.setUserCreatedAt(user.getUserCreatedAt().toString());
        }
        Set<RoleDto> roleDtos = new HashSet<>();
        if (user.getUserRoles() != null) {
            for (Role role : user.getUserRoles()) {
                roleDtos.add(roleMapper.modelToDto(role));
            }
        }
        userDto.setUserRoles(roleDtos);
        userDto.setUserDepartmentId(departmentMapper.modelToDto(user.getUserDepartment()).getDepartmentCode());
        return userDto;
    }

    /**
     * all UsersDto
     *
     * @return List UserssDto
     */
    @Override
    public List<UserDto> getAllUsers() {
        // Get all actions
        List<User> users = userRepository.findAll();

        // Create a list of all actions dto
        ArrayList<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = userMapper.modelToDto(user);
            Set<String> rolesId = new HashSet<>();
            if (user.getUserRoles() != null) {
                for (Role role : user.getUserRoles()) {
                    rolesId.add(roleMapper.modelToDto(role).getRoleName());
                }
            }
            if (user.getUserUpdatedAt() != null) {
                userDto.setUserUpdatedAt(user.getUserUpdatedAt().toString());
            }
            if (user.getUserCreatedAt() != null) {
                userDto.setUserCreatedAt(user.getUserCreatedAt().toString());
            }
            if (user.getUserDepartment() != null) {
                userDto.setUserDepartmentId(user.getUserDepartment().getDepartmentCode());
            }
            //userDto.setUserDepartment(departmentMapper.modelToDto(user.getUserDepartment()));
            userDto.setUserRolesIds(rolesId);
            //userDto.setUserDepartment(departmentMapper.modelToDto(user.getUserDepartment()));
            userDtos.add(userDto);
        }
        return userDtos;
    }

    /**
     * delete User
     *
     * @param id - id
     */
    @Override
    public void removeUser(String id) {
        Optional<User> user = Optional.ofNullable(userRepository.findBy_id(id));
        // If action doesn't exists
        if (!user.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }
}