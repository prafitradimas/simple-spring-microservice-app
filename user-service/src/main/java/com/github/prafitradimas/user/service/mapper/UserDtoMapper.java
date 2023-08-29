package com.github.prafitradimas.user.service.mapper;

import com.github.prafitradimas.user.service.dto.UserDetailsDto;
import com.github.prafitradimas.user.service.dto.UserDto;
import com.github.prafitradimas.user.service.entity.UserDetailsEntity;
import com.github.prafitradimas.user.service.entity.UserEntity;

public class UserDtoMapper {

    public static UserDto toDto(UserEntity user) {
        return new UserDto(
            user.getId(),
            user.getUserDetails().getUsername(),
            user.getFullName(),
            user.getEmail()
        );
    }

    public static UserDetailsDto toDto(UserDetailsEntity user) {
        return new UserDetailsDto(
            user.getUser().getId(),
            user.getUsername(),
            user.getPassword(),
            user.getUser().getFullName(),
            user.getUser().getEmail(),
            user.getCreatedAt(),
            user.getUpdateAt(),
            user.isLocked(),
            user.isEnabled()
        );
    }

}
