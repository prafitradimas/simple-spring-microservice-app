package com.github.prafitradimas.user.service.request;

public record CreateUserRequest(
    String username,
    String password,
    String fullName,
    String email
) {

}
