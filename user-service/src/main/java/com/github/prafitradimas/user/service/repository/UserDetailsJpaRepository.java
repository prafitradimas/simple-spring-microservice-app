package com.github.prafitradimas.user.service.repository;


import com.github.prafitradimas.user.service.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsJpaRepository extends JpaRepository<UserDetailsEntity, String> {

}
