package com.prueba_proteccion.prueba.services;

import com.prueba_proteccion.prueba.dto.auth.AuthDto;
import com.prueba_proteccion.prueba.dto.auth.LoginDto;

public interface AuthService {
    public AuthDto login(LoginDto loginDto);
}
