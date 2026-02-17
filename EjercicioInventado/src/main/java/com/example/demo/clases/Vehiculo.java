package com.example.demo.clases;

import java.util.List;
import jakarta.persistence.*;

/**
 * Clase que representa un vehículo dentro del inventario del concesionario.
 * Se utiliza tanto para procesos de venta como de alquiler.
 * * @author Tu Nombre
 * @version 1.0
 */
@Entity
public class Vehiculo {
	/** Identificador único del vehículo en la base de datos */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** Marca del fabricante del vehículo */
    private String marca;
    private String modelo;
    private int anio;
    private double precio;
    private String imagenUrl;
    
    /** Estado actual del vehículo (LIBRE, ALQUILADO, VENDIDO) */
    @Enumerated(EnumType.STRING)
    private Estado estado; 

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario propietario;
    
    @ManyToOne
    @JoinColumn(name = "inquilino_id")
    private Usuario inquilino;
    
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alquiler> alquileres;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    /**
     * Obtiene el precio de venta o base del vehículo.
     * @return double con el importe del precio.
     */
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public Estado getEstado() { return estado; }
    /**
     * Asigna un nuevo estado al vehículo.
     * @param estado El nuevo estado proveniente del enumerado Estado.
     */
    public void setEstado(Estado estado) { this.estado = estado; }
    public Usuario getPropietario() { return propietario; }
    public void setPropietario(Usuario propietario) { this.propietario = propietario; }
    public Usuario getInquilino() { return inquilino; }
    public void setInquilino(Usuario inquilino) { this.inquilino = inquilino; }
    public List<Alquiler> getAlquileres() { return alquileres; }
    public void setAlquileres(List<Alquiler> alquileres) { this.alquileres = alquileres; }
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
}