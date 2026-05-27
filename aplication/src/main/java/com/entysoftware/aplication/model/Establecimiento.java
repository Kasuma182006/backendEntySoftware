package com.entysoftware.aplication.model;
import java.time.LocalDate;

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
    private Integer id_establecimiento;
    
    private String nombre_establecimiento;
    private String FK_id_propietario;
    private LocalDate fecha_creacion;
    private String estado_establecimiento;

}
