package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Servicios.UsuarioServicio;
import com.example.demo.clases.Usuario;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    // Redirección inicial
    @GetMapping("/")
    public String redirectAlLogin() {
        return "redirect:/login";
    }

    // Login (Gestionado visualmente aquí, lógica por Spring Security)
    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        return "login";
    }

    // Registro
    @GetMapping("/registrarse")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrarse";
    }

    @PostMapping("/registrarse")
    public String registrarUsuario(@RequestParam String usuario, @RequestParam String password) {
        Usuario nuevo = new Usuario();
        nuevo.setUsuario(usuario);
        nuevo.setPassword(password);
        
        // Importante: El servicio debe encriptar la contraseña y asignar rol por defecto
        usuarioServicio.guardarUsuario(nuevo); 
        
        return "redirect:/login";
    }
}