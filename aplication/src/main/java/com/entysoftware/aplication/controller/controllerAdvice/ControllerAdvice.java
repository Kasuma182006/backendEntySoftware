package com.entysoftware.aplication.controller.controllerAdvice;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.entysoftware.aplication.controller.controlerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.customExceptions.BaseYaRegistrada;
import com.entysoftware.aplication.customExceptions.EstablecimientoNoEncontradoException;
import com.entysoftware.aplication.customExceptions.UsuarioNoEncontradoException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerAdvice {
    

    @ExceptionHandler(BaseYaRegistrada.class)
    public ResponseEntity<ControllerAdviceDto> HandlerBaseYaRegistrada(BaseYaRegistrada except , HttpServletRequest request){

        String path = request.getRequestURI();

        ControllerAdviceDto excepcionDto = new ControllerAdviceDto(LocalDateTime.now(),"409","Conflict",except.getMessage(),path);
        return ResponseEntity.status(409).body(excepcionDto);
    }

    
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ControllerAdviceDto> HandlerUsuarioNoEncontradoExepcion(UsuarioNoEncontradoException except , HttpServletRequest request){

        String path = request.getRequestURI();

        ControllerAdviceDto excepcionDto = new ControllerAdviceDto(LocalDateTime.now(),"404","Not Found",except.getMessage(),path);
        return ResponseEntity.status(404).body(excepcionDto);
    }

    
    @ExceptionHandler(EstablecimientoNoEncontradoException.class)
    public ResponseEntity<ControllerAdviceDto> HandlerUsuarioNoEncontradoException( EstablecimientoNoEncontradoException except , HttpServletRequest request){

        String path = request.getRequestURI();

        ControllerAdviceDto excepcionDto = new ControllerAdviceDto(LocalDateTime.now(),"404","No Found",except.getMessage(),path);
        return ResponseEntity.status(404).body(excepcionDto);
    }





    

}
