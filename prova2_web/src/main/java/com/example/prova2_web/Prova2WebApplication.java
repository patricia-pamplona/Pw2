package com.example.prova2_web;

import com.example.prova2_web.model.Usuario;
import com.example.prova2_web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.CacheControl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class Prova2WebApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(Prova2WebApplication.class, args);
    }

    @Autowired
    private UserRepository repository;


    @PostConstruct
    public void initAlmocos() {

        List<Usuario> users = Stream.of(
                new Usuario(1L, "admin", encoder().encode("admin"), true, false, false, false, true),
                new Usuario(2L, "user1", encoder().encode("user1"), false, false, false, false, true),
                new Usuario(3L, "user2", encoder().encode("user2"), false, false, false, false, true)

        ).collect(Collectors.toList());

        repository.saveAll(users);
    }

    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Register resource handler for images
        registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/images/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
		/*
		registry.addResourceHandler("/images/**").addResourceLocations("/images/")
		.setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());*/
    }

}
