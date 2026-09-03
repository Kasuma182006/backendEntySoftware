package com.entysoftware.aplication.error;

public class UsuarioNoEncontradoException extends RuntimeException{

    public UsuarioNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
