package com.github.prafitradimas.user.service.security;


import com.github.prafitradimas.user.service.security.filter.GatewayAuthorizationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, GatewayAuthorizationFilter gatewayAuthorizationFilter) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .securityContext(AbstractHttpConfigurer::disable)
            .sessionManagement(mngmnt -> mngmnt
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(request -> request
                // secure user information with authority USER_READ or USER_READ_DETAILS
                .requestMatchers(HttpMethod.GET, "/users", "/users/{username}").hasAnyAuthority("USER_READ", "USER_READ_DETAILS")
                // secure user sensitive information (e.g. password) with authority USER_READ_DETAILS
                .requestMatchers(HttpMethod.GET, "/users/{username}/details").hasAuthority("USER_READ_DETAILS")
                // permit all user POST operation
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                // secure all user PATCH operation with authority USER_WRITE or USER_WRITE_DETAILS
                .requestMatchers(HttpMethod.PATCH, "/users/{username}").hasAnyAuthority("USER_WRITE", "USER_WRITE_DETAILS")
                // secure all user PATCH operation on sensitive information (e.g. password) with authority USER_WRITE_DETAILS
                .requestMatchers(HttpMethod.PATCH, "/users/{username}/details").hasAuthority("USER_WRITE_DETAILS")
                .anyRequest().hasAuthority("ADMIN"))
            .exceptionHandling(excHandler -> excHandler
                .authenticationEntryPoint(
                    (req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage())
                )
            )
            .addFilter(gatewayAuthorizationFilter)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
