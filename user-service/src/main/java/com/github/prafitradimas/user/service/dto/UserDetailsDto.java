package com.github.prafitradimas.user.service.dto;

import java.time.LocalDateTime;

public record UserDetailsDto(
    Long id,
    String username,
    String password,
    String fullName,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    boolean locked,
    boolean enabled
) {

}
