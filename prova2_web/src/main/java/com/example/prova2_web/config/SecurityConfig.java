package com.example.prova2_web.config;

import com.example.prova2_web.service.AlmocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AlmocoService service;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/error").permitAll()
                .antMatchers("/login", "/index").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .logoutSuccessUrl("/");
        return http.build();
    }


        /*
        http
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/", true)
                //.usernameParameter("email")
                .permitAll()
                .and()
                //.rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
                //.and()
                .logout()
                .deleteCookies("JSESSIONID")
                .permitAll();

        return http.build();

         */


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
