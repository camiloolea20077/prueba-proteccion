package com.prueba_proteccion.prueba.repositories.tickets;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba_proteccion.prueba.entity.tickets.TicketsEntity;

public interface TicketsJPARepository  extends JpaRepository<TicketsEntity, Long>{
    
}
