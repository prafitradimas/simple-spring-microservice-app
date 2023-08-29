package com.github.prafitradimas.user.service.exception;

public class UserPasswordNotMatchException extends Exception {

    public UserPasswordNotMatchException() {
        super("Invalid password!.");
    }

}
