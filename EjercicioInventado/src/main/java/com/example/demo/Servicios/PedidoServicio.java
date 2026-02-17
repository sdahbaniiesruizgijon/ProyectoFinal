package com.example.demo.Servicios;

import com.example.demo.clases.Pedido;
import com.example.demo.clases.Usuario;
import java.util.List;

public interface PedidoServicio {
    Pedido guardar(Pedido pedido);
    List<Pedido> listarPorCliente(Usuario cliente);
    List<Pedido> listarTodos();
}