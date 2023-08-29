package com.github.prafitradimas.user.service.response;

public record PageableWebResponse<T>(
    int code,
    String status,
    String timestamp,
    int page,
    int size,
    String message,
    T body
) {

}
