package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api")
@Tag(name = "Salud del Servicio", description = "Verificación del estado de disponibilidad de la API.")
public class HealthController {

    @Operation(
        summary = "Verificar disponibilidad de la API",
        description = "Endpoint de chequeo de salud (health check) que confirma que la API está en ejecución y respondiendo peticiones."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "La API está disponible y respondiendo correctamente.")
    })
    @GetMapping("/health")
    public Map<String,String> health() {

        Map<String,String> request = Map.of("Api", "Succesful");

        return  request;
    }


}
