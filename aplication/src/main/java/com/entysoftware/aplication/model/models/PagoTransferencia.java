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

@Setter 
@Getter 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity 
@Table(name = "pagos_transferencia")
public class PagoTransferencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idTransferencia;
    
    @Column (name = "FK_id_establecimiento")
    private Integer idEstablecimiento;

    private String codigo;

    private Integer valor;

    @Column (name = "entidad_financiera")
    private String entidadFinanciera;

    private LocalDateTime fecha;

}
