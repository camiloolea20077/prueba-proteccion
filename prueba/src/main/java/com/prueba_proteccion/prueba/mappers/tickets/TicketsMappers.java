package com.prueba_proteccion.prueba.mappers.tickets;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.MappingTarget;

import com.prueba_proteccion.prueba.dto.tickets.CreateTickets;
import com.prueba_proteccion.prueba.dto.tickets.TicketsDto;
import com.prueba_proteccion.prueba.dto.tickets.UpdateTickets;
import com.prueba_proteccion.prueba.entity.tickets.TicketsEntity;

@Mapper(componentModel = "spring")
public interface TicketsMappers {

        @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "created_at", ignore = true),
            @Mapping(target = "updated_at", ignore = true),
            @Mapping(target = "tipo", source = "dto.type"),
            @Mapping(target = "prioridad_manual", source = "dto.priority"),
            @Mapping(target = "fecha_creation", source = "dto.date_creation"),
            @Mapping(target = "user_id", source = "dto.user_id")
    })
    TicketsEntity createToEntity(CreateTickets dto);

    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "type", source = "entity.tipo"),
            @Mapping(target = "priority", source = "entity.prioridad_manual"),
            @Mapping(target = "date_creation", source = "entity.fecha_creation"),
            @Mapping(target = "user_id", source = "entity.user_id")
    })
    TicketsDto toDto(TicketsEntity entity);

    @Mappings({
            @Mapping(target = "id", source = "dto.id"),
            @Mapping(target = "tipo", source = "dto.type"),
            @Mapping(target = "prioridad_manual", source = "dto.priority"),
            @Mapping(target = "fecha_creation", source = "dto.date_creation"),
            @Mapping(target = "user_id", source = "dto.user_id")
    })
    void updateEntityFromDto(UpdateTickets dto, @MappingTarget TicketsEntity entity);
}
