package com.entysoftware.aplication.model;


import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "base_inicial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseInicial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_base")
    private Integer idBase; 

    @Column(name = "FK_id_establecimiento")
    private Integer idEstablecimiento;
    
    @Column(name = "valor")
    private int valorBaseInicial;
    
    private LocalDate fecha;
    
    @Column(name= "hora_apertura")
    private LocalTime hora;
    
}
