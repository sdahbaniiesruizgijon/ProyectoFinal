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

    // Login 
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
public String registrarUsuario(@RequestParam String usuario, @RequestParam String password, Model model) {
    
    //  Definimos la expresión regular
    // Mínimo 8 caracteres, una mayúscula, una minúscula y un número
    String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    //  Validamos la contraseña
    if (!password.matches(regex)) {
        model.addAttribute("error", "La contraseña no cumple con los requisitos mínimos.");
        model.addAttribute("usuarioExistente", usuario); // Para no borrarle el nombre de usuario
        return "registrarse"; // Recarga la página mostrando el error
    }
    Usuario nuevo = new Usuario();
    nuevo.setUsuario(usuario);
    nuevo.setPassword(password);
    
    usuarioServicio.guardarUsuario(nuevo); 
    
    return "redirect:/login";
}
}