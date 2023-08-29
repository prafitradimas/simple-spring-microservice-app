package com.github.prafitradimas.user.service.service.impl;

import com.github.prafitradimas.user.service.dto.UserDetailsDto;
import com.github.prafitradimas.user.service.dto.UserDto;
import com.github.prafitradimas.user.service.entity.AuthorityEntity;
import com.github.prafitradimas.user.service.entity.UserDetailsEntity;
import com.github.prafitradimas.user.service.entity.UserEntity;
import com.github.prafitradimas.user.service.exception.UserAlreadyExistException;
import com.github.prafitradimas.user.service.exception.UserNotFoundException;
import com.github.prafitradimas.user.service.exception.UserPasswordNotMatchException;
import com.github.prafitradimas.user.service.mapper.UserDtoMapper;
import com.github.prafitradimas.user.service.repository.UserDetailsJpaRepository;
import com.github.prafitradimas.user.service.repository.UserJpaRepository;
import com.github.prafitradimas.user.service.request.CreateUserRequest;
import com.github.prafitradimas.user.service.request.UpdateUserDetailsRequest;
import com.github.prafitradimas.user.service.request.UpdateUserRequest;
import com.github.prafitradimas.user.service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired private final UserJpaRepository userRepository;
    @Autowired private final UserDetailsJpaRepository userDetailsRepository;
    @Autowired private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAllUserByPageSize(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<UserEntity> userPage = userRepository.findAll(pageRequest);
        List<UserEntity> userEntityList = userPage.getContent();
        return userEntityList.stream().map(UserDtoMapper::toDto).toList();
    }

    @Override
    public UserDto getUserByUsername(String username) throws UserNotFoundException {
        UserEntity user = userRepository.findUserByUserDetailsUsername(username).orElseThrow(() -> 
            new UserNotFoundException(username));
        return UserDtoMapper.toDto(user);
    }

    @Override
    public UserDetailsDto getUserDetailsByUsername(String username) throws UserNotFoundException {
        UserDetailsEntity user = userDetailsRepository.findById(username).orElseThrow(() ->
            new UserNotFoundException(username));
        return UserDtoMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUserByUsername(String username, UpdateUserRequest request) throws UserNotFoundException {
        UserEntity user = userRepository.findUserByUserDetailsUsername(username).orElseThrow(() ->
            new UserNotFoundException(username));
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        return UserDtoMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDetailsDto updateUserDetailsByUsername(String username, UpdateUserDetailsRequest request) throws UserNotFoundException, UserPasswordNotMatchException {
        UserDetailsEntity user = userDetailsRepository.findById(username).orElseThrow(() ->
            new UserNotFoundException(username));

        if (!passwordEncoder.matches(user.getPassword(), passwordEncoder.encode(request.oldPassword()))) {
            throw new UserPasswordNotMatchException();
        }

        var userEntity = user.getUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setUpdateAt(LocalDateTime.now());
        user = userDetailsRepository.save(user);
        userEntity.setUserDetails(user);
        userRepository.save(userEntity);

        return UserDtoMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto deleteUserById(Long id) throws UserNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(() ->
            new UserNotFoundException(id));
        userRepository.delete(user);
        return UserDtoMapper.toDto(user);
    }

    @Override
    public UserDetailsDto createUser(CreateUserRequest request) throws UserAlreadyExistException {
        if (userDetailsRepository.existsById(request.username())) {
            throw new UserAlreadyExistException(request.username());
        }

        var authorities = new ArrayList<AuthorityEntity>();
        authorities.add(new AuthorityEntity("USER_READ"));
        authorities.add(new AuthorityEntity("USER_READ_DETAILS"));
        authorities.add(new AuthorityEntity("USER_WRITE"));
        authorities.add(new AuthorityEntity("USER_WRITE_DETAILS"));

        var userDetails = UserDetailsEntity.builder()
            .username(request.username())
            .password(passwordEncoder.encode(request.password()))
            .createdAt(LocalDateTime.now())
            .updateAt(LocalDateTime.now())
            .enabled(true)
            .locked(false)
            .authorities(authorities)
            .build();

        var userEntity = UserEntity.builder()
            .fullName(request.fullName())
            .email(request.email())
            .userDetails(userDetails)
            .build();

        userDetails.setUser(userRepository.save(userEntity));
        return UserDtoMapper.toDto(userDetails);
    }

}
