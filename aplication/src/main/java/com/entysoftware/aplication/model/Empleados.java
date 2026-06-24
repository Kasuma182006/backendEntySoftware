package com.entysoftware.aplication.model;

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
@Table(name = "empleados")
@Data
@AllArgsConstructor
public class Empleados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "numero_identificacion")
    private String numeroIdentificacion;
    @Column(name = "FK_id_establecimiento")
    private Integer idEstablecimiento;
    
    private String nombre;
    private String password;
    private String rol;
     
}
