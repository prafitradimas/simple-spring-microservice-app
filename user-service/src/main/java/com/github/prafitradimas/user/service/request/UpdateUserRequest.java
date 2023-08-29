package com.github.prafitradimas.user.service.request;

public record UpdateUserRequest(
    String fullName,
    String email
) {

}
