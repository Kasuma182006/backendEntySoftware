package com.entysoftware.aplication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MesasDto {
    
    private Integer idMesa;
    private Integer idEstablecimiento;
    private String nombreMesa;
    private Boolean estadoMesa;
    

}
