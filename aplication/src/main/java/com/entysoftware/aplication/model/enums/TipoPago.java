package com.entysoftware.aplication.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoPago {
    EFECTIVO,
    TRANSFERENCIA,
    TARJETA;

    /**
     * Convierte el texto recibido del frontend (en mayúsculas o minúsculas) al enum.
     * Cualquier valor distinto de los tres permitidos se rechaza.
     */
    @JsonCreator
    public static TipoPago desdeTexto(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("El tipo de pago es obligatorio. Valores permitidos: EFECTIVO, TRANSFERENCIA, TARJETA");
        }
        try {
            return TipoPago.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de pago inválido: '" + valor + "'. Valores permitidos: EFECTIVO, TRANSFERENCIA, TARJETA");
        }
    }
}
