package com.entysoftware.aplication.customExceptions;

public class UsuarioNoEncontradoException extends RuntimeException{

    public UsuarioNoEncontradoException(String mensaje){
        super(mensaje);
    }
}
