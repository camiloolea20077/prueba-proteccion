package com.prueba_proteccion.prueba.dto.users;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    private String name;
    private String email;
    private String username;
    private String password;
    private Long role_id;
    private Long active;
    private Long farmId;
    private List<String> permisos;
}
