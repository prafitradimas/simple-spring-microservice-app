package com.github.prafitradimas.user.service.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class filter each request to get user's authority.
 * To prevent other user access/modify other user's sensitive information,
 * API Gateway will adjust the user's authority and put them in request header.
 * <br />
 * for example, user A have authorities "USER_READ", "USER_READ_DETAILS", "USER_WRITE", and "USER_WRITE_DETAILS".
 * In case of user A trying to access user B information, API Gateway will send user A request only with "USER_READ"
 * authority because user A is not the original owner of the information.
 * <br />
 * I know this is bad implementation, i hope future me will fix this
 * */
@Component
public class GatewayAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String username = request.getHeader("X-USER-USERNAME");
        final String authorityHeader = request.getHeader("X-GATEWAY-USER-AUTHORITY");
        if (authorityHeader.isEmpty() || authorityHeader.isBlank() || username.isEmpty() || username.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get user authorities separated by a ","
        final String[] authorities = authorityHeader.split(",");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            username,
            null,
            Arrays.stream(authorities).map(SimpleGrantedAuthority::new).toList());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

}
