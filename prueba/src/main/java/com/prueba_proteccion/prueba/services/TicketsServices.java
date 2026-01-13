package com.prueba_proteccion.prueba.services;

import org.springframework.data.domain.PageImpl;

import com.prueba_proteccion.prueba.dto.tickets.CreateTickets;
import com.prueba_proteccion.prueba.dto.tickets.PageTickets;
import com.prueba_proteccion.prueba.dto.tickets.TicketsDto;
import com.prueba_proteccion.prueba.utils.PageableDto;

public interface TicketsServices {
    TicketsDto create(CreateTickets createTicketsDto);
    PageImpl<PageTickets> pageTickets(PageableDto<Object> pageableDto);
}
