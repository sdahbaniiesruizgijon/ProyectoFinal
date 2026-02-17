package com.example.demo.dto;

import com.example.demo.clases.Vehiculo;

/**
 * Objeto de transferencia para los datos de vehículos provenientes de APIs externas.
 * Actúa como capa intermedia entre el JSON externo y la entidad local.
 */
public class CarDTO {
    private Integer id;
    private String make;   
    private String model;  
    private Integer year;  

    public CarDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
    
    /**
     * Transforma el DTO en una entidad Vehiculo compatible con nuestra base de datos.
     * @return Instancia de Vehiculo con datos mapeados.
     */    public Vehiculo toEntity() {
        Vehiculo v = new Vehiculo();
        v.setMarca(this.make);
        v.setModelo(this.model);
        // Usamos .intValue() por si el Integer de la API viene como null, 
        // aunque lo ideal es validar antes.
        v.setAnio(this.year != null ? this.year : 0);
        v.setPrecio(0.0); 
        return v;
    }
    
 // En tu archivo CarDTO.java añadir este método:

    public String getShutterstockUrl() {
        // Genera una URL de búsqueda: https://www.shutterstock.com/es/search/toyota-corolla
        String query = (this.make + " " + this.model).replace(" ", "-").toLowerCase();
        return "https://www.shutterstock.com/es/search/" + query;
    }

    // También añade un campo para una imagen de previsualización (placeholder)
    public String getPlaceholderImage() {
        // Usamos un servicio gratuito de imágenes aleatorias de coches para que no salga vacío
        return "https://loremflickr.com/320/240/" + this.make.toLowerCase() + "," + this.model.toLowerCase() + ",car/all";
    }
}