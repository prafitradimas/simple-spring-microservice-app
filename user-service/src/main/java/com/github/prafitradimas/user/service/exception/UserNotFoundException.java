package com.github.prafitradimas.user.service.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {}
    public UserNotFoundException(String username) {
        super(String.format("User with username: %s not found!.", username));
    }
    public UserNotFoundException(Long id) {
        super(String.format("User with id: %s not found!.", id));
    }
}
