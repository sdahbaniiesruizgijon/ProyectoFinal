package com.example.demo.Servicios;

import com.example.demo.Repositorys.PedidoRepository;
import com.example.demo.clases.Pedido;
import com.example.demo.clases.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class PedidoServicioImpl implements PedidoServicio {

    @Autowired
    private PedidoRepository repo;

    @Override
    public Pedido guardar(Pedido pedido) {
        return repo.save(pedido);
    }

    @Override
    public List<Pedido> listarPorCliente(Usuario cliente) {
        return repo.findByCliente(cliente);
    }

    @Override
    public List<Pedido> listarTodos() {
        return repo.findAll();
    }
}