package com.entysoftware.aplication.model.dto_entrada;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmpleadosDTO {
    private String id_propeitario;
    private String numero_identificacion;
    private int FK_id_establecimiento;
    private String password;
}
