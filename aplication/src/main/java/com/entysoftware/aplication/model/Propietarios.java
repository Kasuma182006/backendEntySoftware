package com.entysoftware.aplication.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
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
    @Column(name = "id_propietario")
    private  String idPropietario;

    private String nombre;
    private String password;

    @Column(name = "fecha_inicio_servicio")
    private LocalDate fechaInicioServicio;

    @Column(name = "fecha_fin_servicio")
    private LocalDate fechaFinServicio;
    
    private String estado;
}
