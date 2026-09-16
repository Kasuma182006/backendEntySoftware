package com.entysoftware.aplication.model.models;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "cierre_caja")
@NoArgsConstructor 
@AllArgsConstructor 
@Setter 
@Getter 
public class CierreCaja {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name = "FK_id_establecimiento")
    private Integer idEstablecimiento;

    private Integer cantidadTransferencia;

    private Integer cantidadEfectivo;

    private  Integer cantidadTarjeta;

    private LocalDateTime fecha;


}
