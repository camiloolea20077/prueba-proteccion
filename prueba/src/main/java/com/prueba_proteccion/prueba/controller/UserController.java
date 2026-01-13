package com.prueba_proteccion.prueba.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba_proteccion.prueba.dto.users.CreateUserDto;
import com.prueba_proteccion.prueba.dto.users.UserDto;
import com.prueba_proteccion.prueba.services.UserService;
import com.prueba_proteccion.prueba.utils.ApiResponse;



@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createUserDataBasic(
        @Valid @RequestBody CreateUserDto createUserDto) throws Exception {
        try {
            UserDto savedUser = userService
                .create(createUserDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
