package com.example.demo.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Repositorys.AlquilerRepository;
import com.example.demo.clases.Alquiler;
import com.example.demo.clases.Usuario;

import java.util.List;

/**
 * Gestiona el ciclo de vida de los alquileres en el sistema.
 */
@Service
public class AlquilerServicio {

    @Autowired
    private AlquilerRepository alquilerRepository;

    public void guardar(Alquiler alquiler) {
        alquilerRepository.save(alquiler);
    }

    /**
     * Recupera todos los contratos de alquiler registrados.
     * @return List de Alquiler.
     */
    public List<Alquiler> listarTodos() {
        return alquilerRepository.findAll();
    }

    public Alquiler obtenerPorId(Integer id) {
        return alquilerRepository.findById(id).orElse(null);
    }

    public List<Alquiler> listarPorCliente(Usuario cliente) {
        return alquilerRepository.findByCliente(cliente);
    }
    
    public void eliminar(Integer id) {
        alquilerRepository.deleteById(id);
    }
}