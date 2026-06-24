package com.entysoftware.aplication.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Entity
@Table(name = "establecimientos")
@Data
@AllArgsConstructor
public class Establecimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_establecimiento")
    private Integer idEstablecimiento;
    
    @Column(name = "nombre_establecimiento")
    private String nombreEstablecimiento;
    @Column(name = "FK_id_propietario")
    private String idPropietario;
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
    @Column(name = "estado_establecimiento")
    private String estadoEstablecimiento;

}
