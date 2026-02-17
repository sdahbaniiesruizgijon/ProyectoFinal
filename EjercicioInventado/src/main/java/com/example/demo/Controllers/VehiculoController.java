package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Servicios.*;
import com.example.demo.clases.*;
import com.example.demo.dto.CarDTO;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoServicio vehiculoServicio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private PedidoServicio pedidoServicio;
    
    @Autowired
    private AlquilerServicio alquilerServicio;
    
    @Autowired
    private CarApiService carApiService;

    // =========================================================
    // 1. VISTAS PRINCIPALES Y BÚSQUEDA EXTERNA
    // =========================================================

    @GetMapping("")
    public String listar(Model model, Authentication auth) {
        model.addAttribute("vehiculos", vehiculoServicio.listarTodos());
        model.addAttribute("username", (auth != null) ? auth.getName() : "Invitado");
        return "catalogo";
    }

    @GetMapping("/buscar-api")
    public String buscarEnApi(@RequestParam String marca, Model model, Authentication auth) {
        // 1. CARGA LOCAL: Imprescindible para que no se borre tu stock de la pantalla
        model.addAttribute("vehiculos", vehiculoServicio.listarTodos());
        model.addAttribute("username", (auth != null) ? auth.getName() : "Invitado");

        //  BUSQUEDA API:'marca' no debe llegue vacío
        if (marca != null && !marca.trim().isEmpty()) {
            List<CarDTO> resultados = carApiService.buscarCochesExternos(marca);
            model.addAttribute("resultadosApi", resultados);
            
            // Debug para ver en consola si la API encuentra algo
            System.out.println("Coches encontrados para " + marca + ": " + resultados.size());
        }
        
        return "catalogo"; 
    }

    
    @PostMapping("/importar")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN')")
    public String importarVehiculo(@RequestParam String marca, @RequestParam String modelo, @RequestParam int anio) {
        Vehiculo nuevo = new Vehiculo();
        nuevo.setMarca(marca);
        nuevo.setModelo(modelo);
        nuevo.setAnio(anio);
        nuevo.setPrecio(15000.0);
        nuevo.setEstado(Estado.libre);
        
        vehiculoServicio.guardar(nuevo);
        return "redirect:/vehiculos"; // Redirige al catálogo general
    }

    // =========================================================
    // 2. ACCIONES DE CLIENTE (Compra y Alquiler)
    // =========================================================


    @GetMapping("/mis-vehiculos")
    @PreAuthorize("hasRole('CLIENTE')")
    public String misVehiculos(Model model, Authentication auth) {
        Usuario cliente = usuarioServicio.obtenerPorNombre(auth.getName());
        
        List<Alquiler> alquileres = alquilerServicio.listarPorCliente(cliente);
        
        // Calculamos la multa actualizada para cada alquiler activo antes de mostrarlo
        for (Alquiler a : alquileres) {
            a.calcularMulta();
            alquilerServicio.guardar(a); // Actualizamos en BD el valor de la multa
        }
        
        model.addAttribute("pedidos", pedidoServicio.listarPorCliente(cliente));
        model.addAttribute("alquileres", alquileres);
        return "mis-vehiculos"; 
    }

    @PostMapping("/comprar/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public String comprar(@PathVariable Integer id, Authentication auth) {
        Usuario cliente = usuarioServicio.obtenerPorNombre(auth.getName());
        Vehiculo v = vehiculoServicio.obtenerPorId(id);
        
        if (v != null && v.getEstado() == Estado.libre) {
            v.setPropietario(cliente);
            v.setEstado(Estado.vendido);
            vehiculoServicio.guardar(v);
            
            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setCliente(cliente);
            nuevoPedido.setVehiculo(v);
            nuevoPedido.setFecha(LocalDateTime.now());
            nuevoPedido.setPrecioVenta(v.getPrecio());
            
            pedidoServicio.guardar(nuevoPedido); 
            return "redirect:/vehiculos/mis-vehiculos?exito=compra";
        }
        return "redirect:/vehiculos?error=no-disponible";
    }

    @GetMapping("/alquilar/{id}")
    @PreAuthorize("hasRole('CLIENTE')")
    public String formAlquilar(@PathVariable Integer id, Model model) {
        Vehiculo v = vehiculoServicio.obtenerPorId(id);
        if (v != null && v.getEstado() == Estado.libre) {
            Alquiler alquiler = new Alquiler();
            alquiler.setVehiculo(v);
            model.addAttribute("alquiler", alquiler);
            return "form-alquiler"; 
        }
        return "redirect:/vehiculos";
    }

    @PostMapping("/procesar-alquiler")
    @PreAuthorize("hasRole('CLIENTE')")
    public String procesarAlquiler(@ModelAttribute Alquiler alquiler, Authentication auth, Model model) {
        // Verificación de seguridad: fechaFin no puede ser anterior a fechaInicio
        if (alquiler.getFechaFin().isBefore(alquiler.getFechaInicio())) {
            return "redirect:/vehiculos/alquilar/" + alquiler.getVehiculo().getId() + "?error=fecha-invalida";
        }

        Usuario cliente = usuarioServicio.obtenerPorNombre(auth.getName()); //
        Vehiculo v = vehiculoServicio.obtenerPorId(alquiler.getVehiculo().getId()); //

        if (v != null && v.getEstado() == Estado.libre) { //
            alquiler.setCliente(cliente); //
            alquiler.setDevuelto(false); //
            
            v.setEstado(Estado.alquilado); //
            vehiculoServicio.guardar(v); //
            alquilerServicio.guardar(alquiler); //
        }
        return "redirect:/vehiculos/mis-vehiculos?exito=alquiler"; //
    }
    @PostMapping("/devolver/{idAlquiler}")
    @PreAuthorize("hasRole('CLIENTE')")
    public String devolverCoche(@PathVariable Integer idAlquiler) {
        Alquiler alquiler = alquilerServicio.obtenerPorId(idAlquiler);
        if (alquiler != null && !alquiler.isDevuelto()) {
            alquiler.setDevuelto(true);
            Vehiculo v = alquiler.getVehiculo();
            v.setEstado(Estado.libre); 
            vehiculoServicio.guardar(v);
            alquilerServicio.guardar(alquiler);
        }
        return "redirect:/vehiculos/mis-vehiculos?exito=devolucion";
    }

    // =========================================================
    // 3. ACCIONES DE ADMIN (Gestión CRUD)
    // =========================================================

    @GetMapping("/aniadir")
    @PreAuthorize("hasRole('ADMIN')")
    public String formAniadir(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("estados", Estado.values());
        return "aniadir";
    }

    @PostMapping("/aniadir")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN')")
    public String guardarNuevo(@ModelAttribute Vehiculo vehiculo) {
        vehiculoServicio.guardar(vehiculo);
        return "redirect:/vehiculos";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String formEditar(@PathVariable Integer id, Model model) {
        Vehiculo v = vehiculoServicio.obtenerPorId(id);
        if (v != null) {
            model.addAttribute("vehiculo", v);
            model.addAttribute("estados", Estado.values());
            return "editar";
        }
        return "redirect:/vehiculos";
    }

    @PostMapping("/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public String actualizarVehiculo(@ModelAttribute Vehiculo vehiculo) {
        vehiculoServicio.guardar(vehiculo);
        return "redirect:/vehiculos";
    }
    
    @GetMapping("/detalles/{id}")
    public String verDetalles(@PathVariable Integer id, Model model) {
        Vehiculo v = vehiculoServicio.obtenerPorId(id);
        if (v != null) {
            model.addAttribute("vehiculo", v);
            return "estadisticas"; // Nombre del archivo HTML que creaste
        }
        return "redirect:/vehiculos";
    }

    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Integer id) {
        vehiculoServicio.borrar(id);
        return "redirect:/vehiculos";
    }
}