package com.prueba_proteccion.prueba.services;

import com.prueba_proteccion.prueba.dto.users.CreateUserDto;
import com.prueba_proteccion.prueba.dto.users.UserDto;


public interface UserService {
    UserDto create(CreateUserDto createUserDto);
}
