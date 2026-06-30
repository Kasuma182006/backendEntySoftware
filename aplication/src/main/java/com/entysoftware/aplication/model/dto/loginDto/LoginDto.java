package com.entysoftware.aplication.model.dto.loginDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String identificacion;
    private String password;
    private String id_establecimiento;
}
