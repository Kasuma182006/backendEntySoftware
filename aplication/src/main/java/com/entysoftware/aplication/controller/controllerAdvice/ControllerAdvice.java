package com.entysoftware.aplication.controller.controllerAdvice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.customExceptions.BaseYaRegistrada;
import com.entysoftware.aplication.customExceptions.EstablecimientoNoEncontradoException;
import com.entysoftware.aplication.customExceptions.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.customExceptions.UsuarioNoEncontradoException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerAdvice {

    private static final String ESTADO_CONFLICTO = "409";
    private static final String ERROR_CONFLICTO = "Conflict";

    private static final String ESTADO_NO_ENCONTRADO = "404";
    private static final String ERROR_NO_ENCONTRADO = "Not Found";
    private static final String ERROR_NO_ENCONTRADO_ALTERNATIVO = "No Found";

    @ExceptionHandler(BaseYaRegistrada.class)
    public ResponseEntity<ControllerAdviceDto> handlerBaseYaRegistrada(BaseYaRegistrada except, HttpServletRequest request) {
        return construirRespuestaError(HttpStatus.CONFLICT, ESTADO_CONFLICTO, ERROR_CONFLICTO, except.getMessage(), request);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ControllerAdviceDto> handlerUsuarioNoEncontradoException(UsuarioNoEncontradoException except, HttpServletRequest request) {
        return construirRespuestaError(HttpStatus.NOT_FOUND, ESTADO_NO_ENCONTRADO, ERROR_NO_ENCONTRADO, except.getMessage(), request);
    }

    @ExceptionHandler(EstablecimientoNoEncontradoException.class)
    public ResponseEntity<ControllerAdviceDto> handlerEstablecimientoNoEncontradoException(EstablecimientoNoEncontradoException except, HttpServletRequest request) {
        return construirRespuestaError(HttpStatus.NOT_FOUND, ESTADO_NO_ENCONTRADO, ERROR_NO_ENCONTRADO_ALTERNATIVO, except.getMessage(), request);
    }

    @ExceptionHandler(ObjetosNoEncontradosExepcion.class)
    public ResponseEntity<ControllerAdviceDto> handlerObjetosNoEncontradosExepcion(ObjetosNoEncontradosExepcion except, HttpServletRequest request) {
        return construirRespuestaError(HttpStatus.NOT_FOUND, ESTADO_NO_ENCONTRADO, ERROR_NO_ENCONTRADO_ALTERNATIVO, except.getMessage(), request);
    }

    private ResponseEntity<ControllerAdviceDto> construirRespuestaError(@NonNull HttpStatus httpStatus, String status, String error, String mensaje, HttpServletRequest request) {
        String path = request.getRequestURI();
        ControllerAdviceDto excepcionDto = new ControllerAdviceDto(LocalDateTime.now(), status, error, mensaje, path);
        return ResponseEntity.status(httpStatus).body(excepcionDto);
    }

}
