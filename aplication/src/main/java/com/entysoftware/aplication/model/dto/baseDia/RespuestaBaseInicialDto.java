package com.entysoftware.aplication.model.dto.baseDia;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RespuestaBaseInicialDto {
    
    private boolean abierto;
    private Integer baseInicial;
    private LocalTime horaApertura;
    private LocalDate fecha;
}
