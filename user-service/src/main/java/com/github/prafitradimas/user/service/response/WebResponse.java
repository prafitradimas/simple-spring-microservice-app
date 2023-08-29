package com.github.prafitradimas.user.service.response;

public record WebResponse<T>(
    int code,
    String status,
    String timestamp,
    String message,
    T body
) {

}
