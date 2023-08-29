package com.github.prafitradimas.user.service.controller;


import com.github.prafitradimas.user.service.dto.UserDetailsDto;
import com.github.prafitradimas.user.service.dto.UserDto;
import com.github.prafitradimas.user.service.exception.UserAlreadyExistException;
import com.github.prafitradimas.user.service.exception.UserNotFoundException;
import com.github.prafitradimas.user.service.exception.UserPasswordNotMatchException;
import com.github.prafitradimas.user.service.request.CreateUserRequest;
import com.github.prafitradimas.user.service.request.UpdateUserDetailsRequest;
import com.github.prafitradimas.user.service.request.UpdateUserRequest;
import com.github.prafitradimas.user.service.response.PageableWebResponse;
import com.github.prafitradimas.user.service.response.WebResponse;
import com.github.prafitradimas.user.service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired private final UserService userService;

    @GetMapping
    public ResponseEntity<PageableWebResponse<List<UserDto>>> getAllUser(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        List<UserDto> dtoList = userService.getAllUserByPageSize(page, size);
        PageableWebResponse<List<UserDto>> response = new PageableWebResponse<>(
            200,
            "OK",
            LocalDateTime.now().toString(),
            page,
            size,
            "",
            dtoList
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<WebResponse<UserDto>> getUser(@PathVariable(name = "username") String username) {
        try {
            UserDto userDto = userService.getUserByUsername(username);
            WebResponse<UserDto> response = new WebResponse<>(
                200,
                "OK",
                LocalDateTime.now().toString(),
                "",
                userDto
            );

            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            WebResponse<UserDto> response = new WebResponse<>(
                404,
                "NOT_FOUND",
                LocalDateTime.now().toString(),
                e.getMessage(),
                null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{username}/details")
    public ResponseEntity<WebResponse<UserDetailsDto>> getUserDetails(@PathVariable(name = "username") String username) {
        try {
            UserDetailsDto userDetailsDto = userService.getUserDetailsByUsername(username);
            WebResponse<UserDetailsDto> response = new WebResponse<>(
                200,
                "OK",
                LocalDateTime.now().toString(),
                "",
                userDetailsDto
            );

            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            WebResponse<UserDetailsDto> response = new WebResponse<>(
                404,
                "NOT_FOUND",
                LocalDateTime.now().toString(),
                e.getMessage(),
                null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{username}")
    public ResponseEntity<WebResponse<Object>> createUser(@RequestBody @Valid CreateUserRequest request) {
        try {
            UserDetailsDto userDetailsDto = userService.createUser(request);

            WebResponse<Object> response = new WebResponse<>(
                201,
                "CREATED",
                LocalDateTime.now().toString(),
                "User created successfully.",
                new HashMap<String, String>() {
                    {
                        put("username", userDetailsDto.username());
                    }
                }
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            WebResponse<Object> response = new WebResponse<>(
                400,
                "BAD_REQUEST",
                LocalDateTime.now().toString(),
                e.getMessage(),
               request
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{username}")
    public ResponseEntity<WebResponse<Object>> updateUser(@PathVariable(name = "username") String username, @RequestBody @Valid UpdateUserRequest request) {
        try {
            UserDto userDto = userService.updateUserByUsername(username, request);
            WebResponse<Object> response = new WebResponse<>(
                200,
                "OK",
                LocalDateTime.now().toString(),
                "User updated successfully.",
                new HashMap<String, String>() {
                    {
                        put("username", userDto.username());
                    }
                }
            );
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            WebResponse<Object> response = new WebResponse<>(
                400,
                "BAD_REQUEST",
                LocalDateTime.now().toString(),
                e.getMessage(),
               null
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{username}/details")
    public ResponseEntity<WebResponse<Object>> updateUserDetails(@PathVariable(name = "username") String username, @RequestBody @Valid UpdateUserDetailsRequest request) {
        try {
            UserDetailsDto userDetailsDto = userService.updateUserDetailsByUsername(username, request);
            WebResponse<Object> response = new WebResponse<>(
                200,
                "OK",
                LocalDateTime.now().toString(),
                "User updated successfully.",
                new HashMap<String, String>() {
                    {
                        put("username", userDetailsDto.username());
                    }
                }
            );
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException | UserPasswordNotMatchException e) {
            WebResponse<Object> response = new WebResponse<>(
                400,
                "BAD_REQUEST",
                LocalDateTime.now().toString(),
                e.getMessage(),
                null
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<WebResponse<Object>> deleteUser(@PathVariable(name = "user_id") Long id) {
        try {
            UserDto userDto = userService.deleteUserById(id);
            WebResponse<Object> response = new WebResponse<>(
                200,
                "OK",
                LocalDateTime.now().toString(),
                "User deleted successfully.",
                new HashMap<String, Object>() {
                    {
                        put("id", userDto.id());
                        put("username", userDto.username());
                    }
                }
            );
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            WebResponse<Object> response = new WebResponse<>(
                400,
                "BAD_REQUEST",
                LocalDateTime.now().toString(),
                e.getMessage(),
                null
            );
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
