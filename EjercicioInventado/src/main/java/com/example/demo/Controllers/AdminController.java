package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.Servicios.AlquilerServicio;
import com.example.demo.Servicios.PedidoServicio;
import com.example.demo.Servicios.UsuarioServicio;
import com.example.demo.Servicios.VehiculoServicio;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private PedidoServicio pedidoServicio;
    
    @Autowired
    private AlquilerServicio alquilerServicio;

    @Autowired
    private VehiculoServicio vehiculoServicio; 

    @GetMapping("/dashboard")
    public String panelControl(Model model) {
        
        // 1. Lista de todos los Clientes registrados
        model.addAttribute("usuarios", usuarioServicio.listarUsuario());
        
        // 2. Historial de Ventas (Coches que ya se vendieron)
        model.addAttribute("ventas", pedidoServicio.listarTodos());
        
        // 3. Estado de Alquileres (Activos e Historial)
        model.addAttribute("alquileres", alquilerServicio.listarTodos());
        
        // 4. Stock actual (Opcional, para ver cuántos libres quedan)
        model.addAttribute("totalCoches", vehiculoServicio.listarTodos().size());

        return "admin-dashboard";
    }
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        // 1. Obtenemos la lista de usuarios desde el servicio
        model.addAttribute("usuarios", usuarioServicio.listarUsuario());
        
        return "usuarios"; 
    }

    // --- GESTIÓN DE USUARIOS (Opcional: Eliminar usuarios problemáticos) ---
    @GetMapping("/eliminar-usuario/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        // usuarioServicio.eliminar(id); 
        return "redirect:/admin/dashboard";
    }
}