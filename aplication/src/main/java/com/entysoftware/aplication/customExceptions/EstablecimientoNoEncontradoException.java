package com.entysoftware.aplication.customExceptions;


public class EstablecimientoNoEncontradoException extends RuntimeException {
    public EstablecimientoNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
