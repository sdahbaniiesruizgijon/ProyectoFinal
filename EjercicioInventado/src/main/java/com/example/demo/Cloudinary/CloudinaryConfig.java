package com.example.demo.Cloudinary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * Configuración de servicios externos y Beans del sistema.
 * Gestiona la conexión con el SDK de Cloudinary para imágenes y la configuración de RestTemplate.
 * * @author Tu Nombre
 * @version 1.0
 */
@Configuration
public class CloudinaryConfig {

	/**
     * Configura el Bean de Cloudinary con las credenciales de la API.
     * @return Instancia de Cloudinary lista para realizar subidas de archivos.
     */
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "diem6kh9m",
            "api_key", "133515474379846",
            "api_secret", "nYCni534Cg0LHKeM_2jJBUK7kzU"
        ));
    }
    
    /**
     * Crea un Bean de RestTemplate para realizar peticiones HTTP a APIs externas (como NHTSA).
     * @return Instancia de RestTemplate configurada.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
