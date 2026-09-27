package com.entysoftware.aplication.service.interfaces;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.EmpleadoDto;
import com.entysoftware.aplication.model.dto.PaginaDto;

public interface EmpleadosInterface {

    public ResponseEntity<PaginaDto<EmpleadoDto>> listarEmpleados(Integer idEstablecimiento, int pagina, int tamano);
}
