package com.entysoftware.aplication.service.service_Implement;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.model.CuerpoPedidos;
import com.entysoftware.aplication.model.EncabezadoPedidos;
import com.entysoftware.aplication.model.dto.PedidosDto;
import com.entysoftware.aplication.repository.CuerpoPedidosRepository;
import com.entysoftware.aplication.repository.EncabezadoPedidosRepository;
import com.entysoftware.aplication.service.PedidosInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PedidoServiceImpl implements PedidosInterface {
    
    private final EncabezadoPedidosRepository encabezadoPedidosRepository;
    private final CuerpoPedidosRepository cuerpoPedidosRepository;
    
    public PedidoServiceImpl (EncabezadoPedidosRepository encabezadoPedidosRepository,CuerpoPedidosRepository cuerpoPedidosRepository){
        this.encabezadoPedidosRepository = encabezadoPedidosRepository;
        this.cuerpoPedidosRepository = cuerpoPedidosRepository;
    }

    public ResponseEntity<Integer> crearPedido(PedidosDto pedido){
        
        EncabezadoPedidos encabezadoPedido = new EncabezadoPedidos(null,pedido.getFK_id_mesa(),"Efectivo","En espera",pedido.getValor_domicilio(),pedido.getPrecio_total(),LocalDate.now(),pedido.getDescripcion()); 
        
        EncabezadoPedidos idEncabezadoPedido = encabezadoPedidosRepository.save(encabezadoPedido);
        
        
        List<CuerpoPedidos> listaCuerpoPedido =  pedido.getPedido().stream()
                                                                   .map(cuerpoPedido -> new CuerpoPedidos(null,idEncabezadoPedido.getId_pedido(),cuerpoPedido.get("idInventario"),cuerpoPedido.get("cantidad")))
                                                                   .toList();
                
        log.debug("lista de productos pedidos: {}", listaCuerpoPedido);

        listaCuerpoPedido.stream()
                         .forEach(guardarCuerpo -> cuerpoPedidosRepository.save(guardarCuerpo));

        log.debug("se han creado los cuerpos del pedido en la base de datos");




        return ResponseEntity.ok(idEncabezadoPedido.getId_pedido());
    }
}
