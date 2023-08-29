package com.github.prafitradimas.user.service.request;

public record UpdateUserDetailsRequest(
    String username,
    String oldPassword,
    String newPassword
) {

}
