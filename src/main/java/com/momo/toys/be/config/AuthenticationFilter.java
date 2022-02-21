package com.momo.toys.be.config;

import static com.momo.toys.be.factory.ConstantUtility.AUTHORIZATION;
import static com.momo.toys.be.factory.ConstantUtility.BEARER;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.momo.toys.be.factory.TokenHelper;

public class AuthenticationFilter extends OncePerRequestFilter{

    private UserDetailsService userDetailsService;

    private TokenHelper tokenHelper;

    AuthenticationFilter(UserDetailsService userDetailsService, TokenHelper tokenHelper){
        this.userDetailsService = userDetailsService;
        this.tokenHelper = tokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException{
        final String header = request.getHeader(AUTHORIZATION);
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (header == null || !header.startsWith(BEARER)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER.length());
        if (!this.tokenHelper.validateToken(token, request)) {
            chain.doFilter(request, response);
            return;
        }

        String username = tokenHelper.getUsernameFromToken(token);
        if (username != null && !username.isEmpty()) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
