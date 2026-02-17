package com.example.demo.Servicios;

import com.example.demo.Repositorys.UsuarioRepository;
import com.example.demo.clases.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = repo.findByUsuario(username);
        if (u == null) throw new UsernameNotFoundException("Usuario no existe");

        return new org.springframework.security.core.userdetails.User(
            u.getUsuario(),
            u.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority(u.getRol()))
        );
    }
}