package com.entysoftware.aplication.model.dto.cierreCaja;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaMasFuerte {
    private LocalDate dia;
    private Integer cantidadProductosVendidos;
    private Integer totalVendido;
}
