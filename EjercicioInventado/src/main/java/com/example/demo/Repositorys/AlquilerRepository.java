package com.example.demo.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import com.example.demo.clases.Alquiler;
import com.example.demo.clases.Usuario;
import com.example.demo.clases.Vehiculo;

import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Gestiona la persistencia de los contratos de alquiler.
 */
@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Integer> {
    List<Alquiler> findByCliente(Usuario cliente);
    List<Alquiler> findByClienteAndDevueltoFalse(Usuario cliente);

 
    /**
     * Elimina todos los alquileres asociados a un vehículo específico.
     * Útil para limpieza de datos antes de borrar un coche.
     * @param vehiculo El objeto vehículo relacionado.
     */
    @Modifying
    @Transactional
    void deleteByVehiculo(Vehiculo vehiculo);
}