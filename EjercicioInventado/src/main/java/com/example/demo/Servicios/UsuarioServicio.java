package com.example.demo.Servicios;

import java.util.List;

import com.example.demo.clases.Usuario;

public interface UsuarioServicio {
    List<Usuario> listarUsuario();
    Usuario guardarUsuario(Usuario usuario);
    Usuario obtenerPorNombre(String username);
    Usuario actualizarUsuario(Usuario usuario);
    void borrarUsuario(String username);
}
