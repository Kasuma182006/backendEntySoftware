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
@Table(name = "voucher_tarjeta")
@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
public class PagoVoucher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idVoucher;

    @Column (name = "FK_id_establecimiento")
    private Integer idEstablecimiento;

    @Column (name = "codigo")
    private String codigo;

    @Column (name = "valor")
    private Integer valor;

    @Column (name = "fecha")
    private LocalDateTime fecha;

}
