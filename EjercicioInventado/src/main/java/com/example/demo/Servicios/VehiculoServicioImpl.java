package com.example.demo.Servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Repositorys.AlquilerRepository;
import com.example.demo.Repositorys.PedidoRepository;
// Verifica que estos nombres de paquetes existan en tu proyecto:
import com.example.demo.Repositorys.VehiculoRepository; 
import com.example.demo.clases.Vehiculo;
import com.example.demo.clases.Estado;

@Service
public class VehiculoServicioImpl implements VehiculoServicio {

    @Autowired
    private VehiculoRepository vehiculoRepo;
    
    @Autowired
    private AlquilerRepository alquilerRepo;
    
    @Autowired
    private PedidoRepository pedidoRepo;

    @Override
    public List<Vehiculo> listarTodos() {
        return vehiculoRepo.findAll();
    }

    @Override
    public Vehiculo obtenerPorId(Integer id) {
        return vehiculoRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Vehiculo vehiculo) {
        vehiculoRepo.save(vehiculo);
    }

    @Override
    @Transactional // <--- MUY IMPORTANTE
    public void borrar(Integer id) {
        Vehiculo v = vehiculoRepo.findById(id).orElse(null);
        if (v != null) {
            // 1. Borramos rastro en tablas hijas
            alquilerRepo.deleteByVehiculo(v);
            pedidoRepo.deleteByVehiculo(v);
            
            // 2. Borramos el padre
            vehiculoRepo.delete(v);
        }
    }
    
    @Override
    @Transactional
    public boolean actualizarEstado(Integer id, Estado nuevoEstado) {
        Vehiculo v = obtenerPorId(id);
        if (v != null) {
            v.setEstado(nuevoEstado);
            vehiculoRepo.save(v);
            return true;
        }
        return false;
    }

    @Override
    public boolean estaDisponible(Integer id) {
        Vehiculo v = obtenerPorId(id);
        return v != null && v.getEstado() == Estado.libre;
    }
}