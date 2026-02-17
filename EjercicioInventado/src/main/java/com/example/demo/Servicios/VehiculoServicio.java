package com.example.demo.Servicios;

import java.util.List;
import com.example.demo.clases.Vehiculo;
import com.example.demo.clases.Estado;

public interface VehiculoServicio {
    List<Vehiculo> listarTodos();
    Vehiculo obtenerPorId(Integer id);
    void guardar(Vehiculo v);
    void borrar(Integer id);
    boolean actualizarEstado(Integer id, Estado nuevoEstado);
    boolean estaDisponible(Integer id);
}