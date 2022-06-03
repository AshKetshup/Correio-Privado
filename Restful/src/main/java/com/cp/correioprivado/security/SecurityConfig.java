package com.cp.correioprivado.security;

import com.cp.correioprivado.filter.CustomAuthenticationFilter;
import com.cp.correioprivado.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    static final String ProducerRole = "Produtor";
    static final String ConsumerRole = "Consumidor";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(GET, "api/news").permitAll();
        http.authorizeRequests().antMatchers(GET, "api/topics").permitAll();
        //http.authorizeRequests().anyRequest().permitAll(); //very bad way to do it, this instruction allows all requests to be accepted
        //PRODUCER ONLY AUTHORIZATION
        http.authorizeRequests().antMatchers(POST, "api/topic/save").hasRole(ProducerRole);
        http.authorizeRequests().antMatchers(GET, "api/topics").hasRole(ProducerRole);
        http.authorizeRequests().antMatchers(POST, "api/news/save").hasRole(ProducerRole);
        //http.authorizeRequests().antMatchers(GET, "api/newsbyme").hasRole(ProducerRole);
        //GENERIC AUTHORIZATION
        http.authorizeRequests().antMatchers(GET, "api/news").hasAnyRole();
        http.authorizeRequests().antMatchers(GET, "api/topics").hasAnyRole();

        //CLIENT ONLY AUTHORIZATION
        http.authorizeRequests().antMatchers(POST, "api/topic_subscribed/subscribe").hasRole(ConsumerRole);

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
