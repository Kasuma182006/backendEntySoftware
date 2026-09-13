package com.entysoftware.aplication.error;

public class DatosDuplicados extends RuntimeException{
    public DatosDuplicados(String mensaje){
        super(mensaje);
    }
}
