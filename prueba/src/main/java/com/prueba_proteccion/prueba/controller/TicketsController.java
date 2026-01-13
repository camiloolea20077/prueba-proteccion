package com.prueba_proteccion.prueba.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba_proteccion.prueba.dto.tickets.CreateTickets;
import com.prueba_proteccion.prueba.dto.tickets.PageTickets;
import com.prueba_proteccion.prueba.dto.tickets.TicketsDto;
import com.prueba_proteccion.prueba.services.TicketsServices;
import com.prueba_proteccion.prueba.utils.ApiResponse;
import com.prueba_proteccion.prueba.utils.GlobalException;
import com.prueba_proteccion.prueba.utils.PageableDto;

@RestController
@RequestMapping("/tickets")
public class TicketsController {
    
    @Autowired
    TicketsServices ticketsServices;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createUserDataBasic(
        @RequestHeader("userId") Long userId,
        @Valid @RequestBody CreateTickets createTickets) throws Exception {
        try {
            createTickets.setUser_id(userId);
            TicketsDto savedUser = ticketsServices
                .create(createTickets);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> listConventions(
            @Valid @RequestBody PageableDto<Object> pageableDto) {
        try {
            Page<PageTickets> convention = ticketsServices.pageTickets(pageableDto);
            if (convention.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, convention);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
