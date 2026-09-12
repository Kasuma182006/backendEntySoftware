package com.entysoftware.aplication.service.interfaces;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.PagosTransferenciaDto;

public interface PagosTransferenciaInterface {

    public ResponseEntity<PaginaDto<PagosTransferenciaDto>> listarPagosTransferencia(Integer idEstablecimiento, LocalDate fechaInicio,
            LocalDate fechaFin, int pagina, int tamano);

    public ResponseEntity<PagosTransferenciaDto> crearPagoTransferencia(PagosTransferenciaDto pagoTransferencia);

    public ResponseEntity<String> editarPagoTransferencia(PagosTransferenciaDto pagoTransferencia);

    public ResponseEntity<String> eliminarPagoTransferencia(Integer idTransferencia);
}
