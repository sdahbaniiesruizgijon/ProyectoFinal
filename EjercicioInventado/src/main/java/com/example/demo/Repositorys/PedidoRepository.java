package com.example.demo.Repositorys;

import com.example.demo.clases.Pedido;
import com.example.demo.clases.Usuario;
import com.example.demo.clases.Vehiculo;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Para que el cliente vea su historial de la 3ª tabla
    List<Pedido> findByCliente(Usuario cliente);
    
    @Modifying
    @Transactional
    void deleteByVehiculo(Vehiculo vehiculo);
}