package com.entysoftware.aplication.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.entysoftware.aplication.error.RangoFechasInvalidoException;

/**
 * Rango semiabierto [desde, hasta) usado para filtrar por fecha en consultas.
 */
public record RangoFechas(LocalDateTime desde, LocalDateTime hasta) {

    /**
     * Sin fechas no hay filtro (Optional vacío); con una sola fecha se filtra ese día;
     * con ambas se filtra el rango incluyendo el día final completo.
     *
     * @throws RangoFechasInvalidoException si la fecha de inicio es posterior a la fecha fin
     */
    public static Optional<RangoFechas> resolver(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null && fechaFin == null) {
            return Optional.empty();
        }

        LocalDate inicio = fechaInicio != null ? fechaInicio : fechaFin;
        LocalDate fin = fechaFin != null ? fechaFin : fechaInicio;

        if (inicio.isAfter(fin)) {
            throw new RangoFechasInvalidoException("La fecha de inicio " + inicio + " no puede ser posterior a la fecha fin " + fin);
        }

        return Optional.of(new RangoFechas(inicio.atStartOfDay(), fin.plusDays(1).atStartOfDay()));
    }
}
