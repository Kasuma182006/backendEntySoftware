package com.entysoftware.aplication.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.CostosDto;
import com.entysoftware.aplication.model.models.Costo;
import com.entysoftware.aplication.model.models.Establecimiento;

@Component
public class MapperCostosDto {

    public Costo dtoToCostos(CostosDto costoDto, Establecimiento establecimiento) {
        Costo costo = new Costo();
        costo.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        costo.setValorCosto(costoDto.getValorCosto());
        costo.setTipoPago(costoDto.getTipoPago().toUpperCase());
        costo.setFecha(LocalDate.now());
        return costo;
    }
}
