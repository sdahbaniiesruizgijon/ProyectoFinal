package com.example.demo.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.demo.clases.Vehiculo;
import com.example.demo.Servicios.VehiculoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST que expone los recursos de vehículos para integraciones externas.
 * Produce respuestas en formato JSON.
 */
@RestController
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehículos API", description = "Servicios REST para integración externa")
public class VehiculoRestController {

    @Autowired
    private VehiculoServicio servicio;

    @Operation(summary = "Lista todos los vehículos en formato JSON")
    /**
     * Endpoint para listar todos los vehículos disponibles en el sistema.
     * @return List de objetos Vehiculo.
     */
    @GetMapping
    public List<Vehiculo> getAll() {
        return servicio.listarTodos();
    }
    
    // Puedes añadir más endpoints si quieres
    @Operation(summary = "Obtener vehículo por ID")
    
    /**
     * Busca un vehículo específico por su identificador.
     * @param id El ID del vehículo a consultar.
     * @return Objeto Vehiculo encontrado.
     */
    @GetMapping("/{id}")
    public Vehiculo getById(@PathVariable Integer id) {
        return servicio.obtenerPorId(id);
    }
}