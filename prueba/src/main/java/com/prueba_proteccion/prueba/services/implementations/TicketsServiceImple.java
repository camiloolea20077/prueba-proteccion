package com.prueba_proteccion.prueba.services.implementations;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.prueba_proteccion.prueba.dto.tickets.CreateTickets;
import com.prueba_proteccion.prueba.dto.tickets.PageTickets;
import com.prueba_proteccion.prueba.dto.tickets.TicketsDto;
import com.prueba_proteccion.prueba.entity.tickets.TicketsEntity;
import com.prueba_proteccion.prueba.mappers.tickets.TicketsMappers;
import com.prueba_proteccion.prueba.repositories.tickets.TicketsJPARepository;
import com.prueba_proteccion.prueba.repositories.tickets.TicketsQueryRepository;
import com.prueba_proteccion.prueba.services.TicketsServices;
import com.prueba_proteccion.prueba.utils.GlobalException;
import com.prueba_proteccion.prueba.utils.PageableDto;

@Service
public class TicketsServiceImple implements TicketsServices {
    
    private final TicketsQueryRepository ticketsQueryRepository;
    private final TicketsJPARepository ticketsJPARepository;
    private final TicketsMappers ticketsMappers;

    public TicketsServiceImple(TicketsQueryRepository ticketsQueryRepository, TicketsJPARepository ticketsJPARepository,
            TicketsMappers ticketsMappers) {
        this.ticketsQueryRepository = ticketsQueryRepository;
        this.ticketsJPARepository = ticketsJPARepository;
        this.ticketsMappers = ticketsMappers;
    }
    
    @Override
    public TicketsDto create(CreateTickets createTickets) {
        try {
            TicketsEntity ticketsEntity = ticketsMappers.createToEntity(createTickets);
            ticketsEntity.setUser_id(createTickets.getUser_id());
            TicketsEntity save = ticketsJPARepository.save(ticketsEntity);
            return ticketsMappers.toDto(save);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public PageImpl<PageTickets> pageTickets(PageableDto<Object> pageableDto) {
        return ticketsQueryRepository.pageTickets(pageableDto);
    }
}
