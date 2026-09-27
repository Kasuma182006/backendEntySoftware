package com.entysoftware.aplication.service.interfaces;


import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.pagosDTOs.FacturaPedidoDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.PagarPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.PedidosDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.ResumenPedidosDto;

public interface PedidosInterface {

    public ResponseEntity<Integer> crearPedido(PedidosDto pedido);

    public ResponseEntity<List<PedidosDto>> pedidosHoy (Integer idEstablecimiento);

    public ResponseEntity<String> editarPedido(PedidosDto editarPedido);

    public ResponseEntity<FacturaPedidoDto> pagoPedido(PagarPedidoDto pago);

    public ResponseEntity<ResumenPedidosDto> listarPedidos(Integer idEstablecimiento, LocalDate fechaInicio,
            LocalDate fechaFin, int pagina, int tamano);

}
