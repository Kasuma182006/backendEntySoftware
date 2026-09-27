package com.entysoftware.aplication.service.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.mapper.MapperEmpleadoDto;
import com.entysoftware.aplication.model.dto.EmpleadoDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.models.Empleado;
import com.entysoftware.aplication.repository.EmpleadosRepository;
import com.entysoftware.aplication.service.interfaces.EmpleadosInterface;
import com.entysoftware.aplication.utils.ConsultaPaginadaUtils;

@Service
public class EmpleadosServiceImp implements EmpleadosInterface {

    private static final String CAMPO_ORDEN = "nombre";

    private final EmpleadosRepository empleadosRepository;
    private final MapperEmpleadoDto mapperEmpleadoDto;

    public EmpleadosServiceImp(EmpleadosRepository empleadosRepository, MapperEmpleadoDto mapperEmpleadoDto) {
        this.empleadosRepository = empleadosRepository;
        this.mapperEmpleadoDto = mapperEmpleadoDto;
    }

    /**
     * Lista paginada de empleados de un establecimiento, ordenados alfabéticamente por nombre.
     */
    public ResponseEntity<PaginaDto<EmpleadoDto>> listarEmpleados(Integer idEstablecimiento, int pagina, int tamano) {
        Pageable pageable = ConsultaPaginadaUtils.construirPageableOrdenadoAsc(pagina, tamano, CAMPO_ORDEN);

        Page<Empleado> paginaEmpleados = empleadosRepository.findByIdEstablecimiento(idEstablecimiento, pageable);

        return ResponseEntity.ok(PaginaDto.desdePage(paginaEmpleados.map(mapperEmpleadoDto::empleadoToDto)));
    }
}
