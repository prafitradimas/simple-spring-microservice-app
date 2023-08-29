package com.github.prafitradimas.user.service.repository;


import com.github.prafitradimas.user.service.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityJpaRepository extends JpaRepository<AuthorityEntity, String> {

}
