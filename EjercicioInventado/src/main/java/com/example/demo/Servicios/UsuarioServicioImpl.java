package com.example.demo.Servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Repositorys.UsuarioRepository;
import com.example.demo.clases.Usuario;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {
    @Autowired
    private UsuarioRepository repo;
    
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        // Encriptamos solo cuando es un registro nuevo
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        // Forzamos que siempre sea CLIENTE al registrarse
        usuario.setRol("ROLE_CLIENTE");
        return repo.save(usuario);
    }

 
    public Usuario procesarUsuarioOAuth(String email, String nombre) {
        Usuario existente = repo.findByUsuario(email);
        
        if (existente == null) {
            // Es la primera vez que entra con Google/GitHub
            Usuario nuevo = new Usuario();
            nuevo.setUsuario(email);
            nuevo.setPassword("OAUTH_USER"); // No necesitan contraseña real
            nuevo.setRol("ROLE_CLIENTE");    // <--- ASIGNAMOS EL ROL AQUÍ
            return repo.save(nuevo);
        }
        
        return existente;
    }

    @Override
    public List<Usuario> listarUsuario() {
        return repo.findAll();
    }

    @Override
    public Usuario obtenerPorNombre(String username) {
        return repo.findByUsuario(username);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        // IMPORTANTE: Antes de salvar, recuperamos el usuario original de la BD
        // para no perder el ROL o encriptar doble la contraseña.
        Usuario existente = repo.findById(usuario.getId()).orElse(null);
        if (existente != null) {
            // Si la contraseña que viene es distinta a la de la BD, la encriptamos
            if (!usuario.getPassword().equals(existente.getPassword())) {
                usuario.setPassword(encoder.encode(usuario.getPassword()));
            }
        }
        return repo.save(usuario);
    }

    @Override
    public void borrarUsuario(String usuario) {
        repo.deleteByUsuario(usuario);
    }
}