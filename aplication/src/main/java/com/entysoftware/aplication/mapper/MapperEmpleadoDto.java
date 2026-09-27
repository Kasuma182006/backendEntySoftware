package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.EmpleadoDto;
import com.entysoftware.aplication.model.models.Empleado;

@Component
public class MapperEmpleadoDto {

    public EmpleadoDto empleadoToDto(Empleado empleado) {
        return new EmpleadoDto(
            empleado.getIdEstablecimiento(),
            empleado.getNumeroIdentificacion(),
            empleado.getNombre(),
            empleado.getRol()
        );
    }
}
