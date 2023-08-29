package com.github.prafitradimas.user.service.exception;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException() {}
    public UserAlreadyExistException(String username) {
        super(String.format("User with username: %s already exist!.", username));
    }
}
