package com.github.prafitradimas.user.service.repository;

import com.github.prafitradimas.user.service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserByUserDetailsUsername(String username);
}
