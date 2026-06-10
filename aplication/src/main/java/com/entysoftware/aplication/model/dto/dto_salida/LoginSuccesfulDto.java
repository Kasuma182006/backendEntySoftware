package com.entysoftware.aplication.model.dto.dto_salida;

import java.util.List;


import com.entysoftware.aplication.model.Categorias;
import com.entysoftware.aplication.model.Inventario;
import com.entysoftware.aplication.model.Mesas;

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
    private List<Mesas> mesas;
    private List<Categorias> categorias;
    private List<Inventario> inventario;

    
}
