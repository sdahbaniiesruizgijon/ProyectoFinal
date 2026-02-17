package com.example.demo.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.clases.Usuario;
import com.example.demo.clases.Vehiculo;
import java.util.List;

/**
 * Repositorio para la gestión de vehículos en base de datos.
 * Incluye métodos de búsqueda personalizada por marca.
 */
@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    List<Vehiculo> findByPropietario(Usuario usuario);
    
    // Método clave para la función de búsqueda por patrón
    /**
     * Busca vehículos cuya marca contenga una cadena de texto, ignorando mayúsculas.
     * @param marca Texto a buscar.
     * @return Lista de vehículos que coinciden con el patrón.
     */
    List<Vehiculo> findByMarcaContainingIgnoreCase(String marca);
}