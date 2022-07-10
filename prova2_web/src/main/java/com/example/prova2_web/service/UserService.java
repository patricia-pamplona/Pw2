package com.example.prova2_web.service;

import com.example.prova2_web.model.Usuario;
import com.example.prova2_web.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void insert(Usuario usuario){
        usuario.setPassword(encoder().encode(usuario.getPassword()));
        userRepository.save(usuario);
    }

    public List<Usuario> findAll(){
        return userRepository.findAll();
    }
    public Usuario findById(Long id){
        Optional<Usuario> usuario = userRepository.findById(id);
        if(usuario.isPresent()){
            return usuario.get();
        }else{
            return null;
        }
    }
    public void update(Usuario usuario){
        userRepository.saveAndFlush(usuario);
    }
    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> optional = userRepository.findUsuarioByUsername(username);

        if(optional.isPresent()) {
            return optional.get();
        }

        throw new UsernameNotFoundException("User not found");
    }

    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
