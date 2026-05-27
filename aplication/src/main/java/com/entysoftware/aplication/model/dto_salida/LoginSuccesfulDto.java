package com.entysoftware.aplication.model.dto_salida;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginSuccesfulDto {
    private String numero_identificacion;
    private String nombre;
    private String rol;
    private Integer id_establecimiento;
    private String estado_establecimiento;
    private String nombre_establecimiento;
    
}
