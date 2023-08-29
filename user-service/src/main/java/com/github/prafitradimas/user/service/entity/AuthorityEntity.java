package com.github.prafitradimas.user.service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "authorities")
@Data
public class AuthorityEntity {

    @Id
    private String authority;

    public AuthorityEntity(String authority) {
        this.authority = authority;
    }

}
