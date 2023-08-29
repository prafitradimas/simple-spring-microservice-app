package com.github.prafitradimas.user.service.service;

import com.github.prafitradimas.user.service.dto.UserDetailsDto;
import com.github.prafitradimas.user.service.dto.UserDto;
import com.github.prafitradimas.user.service.exception.UserAlreadyExistException;
import com.github.prafitradimas.user.service.exception.UserNotFoundException;
import com.github.prafitradimas.user.service.exception.UserPasswordNotMatchException;
import com.github.prafitradimas.user.service.request.CreateUserRequest;
import com.github.prafitradimas.user.service.request.UpdateUserDetailsRequest;
import com.github.prafitradimas.user.service.request.UpdateUserRequest;

import java.util.List;

public interface UserService {

    public List<UserDto> getAllUserByPageSize(int page, int size);
    public UserDto getUserByUsername(String username) throws UserNotFoundException;
    public UserDetailsDto getUserDetailsByUsername(String username) throws UserNotFoundException;
    public UserDto updateUserByUsername(String username, UpdateUserRequest request) throws UserNotFoundException;
    public UserDetailsDto updateUserDetailsByUsername(String username, UpdateUserDetailsRequest request) throws UserNotFoundException, UserPasswordNotMatchException;
    public UserDto deleteUserById(Long id) throws UserNotFoundException;
    public UserDetailsDto createUser(CreateUserRequest request) throws UserAlreadyExistException;
}
