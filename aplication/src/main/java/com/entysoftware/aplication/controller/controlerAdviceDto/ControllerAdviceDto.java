package com.entysoftware.aplication.controller.controlerAdviceDto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   
@AllArgsConstructor
@NoArgsConstructor
public class ControllerAdviceDto {
    private LocalDateTime time;
    private String status;
    private String error;
    private String mensaje;
    private String path;
}
