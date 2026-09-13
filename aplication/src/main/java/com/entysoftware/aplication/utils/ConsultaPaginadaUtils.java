package com.entysoftware.aplication.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class ConsultaPaginadaUtils {

    public static final int TAMANO_PAGINA_MAXIMO = 100;

    private ConsultaPaginadaUtils() {
    }

    /**
     * Construye un Pageable ordenado de forma descendente por el campo indicado.
     * La página negativa se ajusta a 0 y el tamaño se limita al rango [1, TAMANO_PAGINA_MAXIMO].
     */
    public static Pageable construirPageableOrdenadoDesc(int pagina, int tamano, String campoOrden) {
        return construirPageableOrdenado(pagina, tamano, campoOrden, Sort.Direction.DESC);
    }

    /**
     * Construye un Pageable ordenado de forma ascendente por el campo indicado.
     * La página negativa se ajusta a 0 y el tamaño se limita al rango [1, TAMANO_PAGINA_MAXIMO].
     */
    public static Pageable construirPageableOrdenadoAsc(int pagina, int tamano, String campoOrden) {
        return construirPageableOrdenado(pagina, tamano, campoOrden, Sort.Direction.ASC);
    }

    private static Pageable construirPageableOrdenado(int pagina, int tamano, String campoOrden, Sort.Direction direccion) {
        int paginaValida = Math.max(pagina, 0);
        int tamanoValido = Math.min(Math.max(tamano, 1), TAMANO_PAGINA_MAXIMO);
        return PageRequest.of(paginaValida, tamanoValido, Sort.by(direccion, campoOrden));
    }
}
