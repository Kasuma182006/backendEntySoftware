package com.entysoftware.aplication.model;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "propietarios")
@Data
@AllArgsConstructor
public class Propietarios {
    @Id
    private  String id_propietario;

    private String nombre;
    private String password;
    private LocalDate fecha_inicio_servicio;
    private LocalDate fecha_fin_servicio;
    private String estado;
}
