package com.example.prova2_web.repository;

import com.example.prova2_web.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findUsuarioByUsername(String username);
}
