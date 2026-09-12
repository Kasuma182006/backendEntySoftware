package com.entysoftware.aplication.service.interfaces;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.PagosVoucherDto;

public interface PagosVoucherInterface {

    public ResponseEntity<PaginaDto<PagosVoucherDto>> listarPagosVoucher(Integer idEstablecimiento, LocalDate fechaInicio,
            LocalDate fechaFin, int pagina, int tamano);

    public ResponseEntity<PagosVoucherDto> crearPagoVoucher(PagosVoucherDto pagoVoucher);

    public ResponseEntity<String> editarPagoVoucher(PagosVoucherDto pagoVoucher);

    public ResponseEntity<String> eliminarPagoVoucher(Integer idVoucher);
}
