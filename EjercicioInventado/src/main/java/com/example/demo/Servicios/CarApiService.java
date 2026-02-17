package com.example.demo.Servicios;

import com.example.demo.dto.CarDTO;
import com.example.demo.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * Servicio encargado de la comunicación con la API externa de NHTSA.
 * Gestiona peticiones HTTP para obtener datos reales de vehículos.
 */
@Service
public class CarApiService {

    @Autowired
    private RestTemplate restTemplate;

    // Estos valores se leen de tu application.properties
    @Value("${carapi.api.url}")
    private String apiUrl;

    @Value("${carapi.api.token}")
    private String apiToken;

    @Value("${carapi.api.secret}")
    private String apiSecret;

    /**
     * Método para obtener el Token JWT necesario para las consultas
     */
    private String obtenerJwt() {
        String authUrl = apiUrl + "/auth/login";
        
        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("api_token", apiToken);
        authRequest.put("api_secret", apiSecret);

        try {
            // Enviamos la petición POST para loguearnos
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(authUrl, authRequest, AuthResponse.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getToken();
            }
        } catch (Exception e) {
            System.err.println("Error obteniendo el JWT: " + e.getMessage());
        }
        return null;
    }

    /**
     * Consulta la API de NHTSA para obtener modelos basados en una marca.
     * @param marca Nombre del fabricante (ej. "Toyota").
     * @return Lista de objetos CarDTO con la información técnica.
     */
    public List<CarDTO> buscarCochesExternos(String marca) {
        List<CarDTO> listaFinal = new ArrayList<>();
        // Esta URL devuelve todos los modelos de una marca (ej: Toyota)
        String url = "https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/" + marca + "?format=json";

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> body = response.getBody();
            if (body != null && body.get("Results") != null) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("Results");
                
                for (Map<String, Object> item : results) {
                    CarDTO dto = new CarDTO();
                    // NHTSA devuelve Model_ID y Model_Name
                    dto.setId((Integer) item.get("Model_ID"));
                    dto.setMake((String) item.get("Make_Name"));
                    dto.setModel((String) item.get("Model_Name"));
                    dto.setYear(2024); // Esta API específica no da el año en este endpoint, lo ponemos por defecto
                    listaFinal.add(dto);
                }
            }
        } catch (Exception e) {
            System.err.println("Error con NHTSA API: " + e.getMessage());
        }
        return listaFinal;
    }
}