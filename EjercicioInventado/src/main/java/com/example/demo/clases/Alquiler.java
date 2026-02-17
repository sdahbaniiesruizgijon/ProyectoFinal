package com.example.demo.clases;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa un contrato de alquiler entre un cliente y un vehículo.
 * Incluye la lógica para el cálculo de penalizaciones por retraso.
 */
@Entity
public class Alquiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Usuario cliente;

    @ManyToOne
    private Vehiculo vehiculo;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean devuelto = false;
    private double multa = 0.0;

    public Alquiler() {}
 
    /**
     * Valida si el rango de fechas de alquiler es coherente.
     * @return true si la fecha de inicio es anterior o igual a la de fin.*/
    public boolean esPeriodoValido() {
        if (fechaInicio == null || fechaFin == null) return false;
        return !fechaFin.isBefore(fechaInicio); //
    }
    /**
     * Calcula la multa automática si la fecha actual es posterior a la fecha fin.
     * La multa se basa en un coste diario de 50.0€ más un 20% de recargo.
     */
    public void calcularMulta() {
        // Solo calculamos si no ha sido devuelto y ya pasó la fecha fin
        if (!devuelto && LocalDate.now().isAfter(fechaFin)) {
            long diasRetraso = ChronoUnit.DAYS.between(fechaFin, LocalDate.now());
            
            if (diasRetraso > 0) {
                double costeBasePorDia = 50.0; // Precio fijo por día de retraso
                double porcentajeRecargo = 0.20; // 20% de recargo adicional por "gestión"
                
                double subtotalMulta = diasRetraso * costeBasePorDia;
                this.multa = subtotalMulta + (subtotalMulta * porcentajeRecargo);
            }
        } else if (devuelto) {
            // Si ya se devolvió, mantenemos la multa que se calculó en el momento de la entrega
        } else {
            this.multa = 0.0;
        }
    }

    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public boolean isDevuelto() { return devuelto; }
    public void setDevuelto(boolean devuelto) { this.devuelto = devuelto; }
    public double getMulta() { return multa; }
    public void setMulta(double multa) { this.multa = multa; }
}