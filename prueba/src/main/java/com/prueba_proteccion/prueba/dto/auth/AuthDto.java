package com.prueba_proteccion.prueba.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    private UserDetailDto user;
    private String token;
}

