package com.github.prafitradimas.user.service.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_details")
@Data
@Builder
public class UserDetailsEntity implements UserDetails {

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean locked;

    private boolean enabled;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updateAt;

    @OneToOne(mappedBy = "userDetails", fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @Column(name = "user_id")
    private UserEntity user;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_authorities",
        joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
        inverseJoinColumns = @JoinColumn(name = "authority", referencedColumnName = "authority")
    )
    private List<AuthorityEntity> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthority())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
