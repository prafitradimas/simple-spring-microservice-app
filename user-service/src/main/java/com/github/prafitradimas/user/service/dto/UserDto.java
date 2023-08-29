package com.github.prafitradimas.user.service.dto;

public record UserDto(
    Long id,
    String username,
    String fullName,
    String email
) {}
