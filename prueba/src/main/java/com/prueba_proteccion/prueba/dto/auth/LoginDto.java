package com.prueba_proteccion.prueba.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    public String email;
    public String password;
    private Long farmId;
}
