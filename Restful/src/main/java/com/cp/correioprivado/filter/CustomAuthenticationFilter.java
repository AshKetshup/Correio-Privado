package com.cp.correioprivado.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    static final int access_token_expiration_time = 1000 * 60 * 60 * 24; //currently set to 24h
    //--------------------------------------milsec--sec--min--hour----------------------
    static final int refresh_token_expiration_time = 1000 * 60 * 60 * 24; //currently set to 24h
    //--------------------------------------milsec--sec--min--hour----------------------
    static final String super_secret_seed_for_tokens = "secret";

    /*
    TODO: Redefine secret for the algorithm in the successfulAuthentication() to a proper secret that's stored safely
          Rewrite Authentication to use a salt with the password for added security
     */
    private final AuthenticationManager authenticationManager;
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is {}", username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(super_secret_seed_for_tokens.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt((new Date(System.currentTimeMillis() + access_token_expiration_time)))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", String.valueOf(user.getAuthorities()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt((new Date(System.currentTimeMillis() + refresh_token_expiration_time)))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);
    }
}
