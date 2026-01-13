package com.prueba_proteccion.prueba.dto.tickets;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CreateTickets {
    private String type;
    private Long priority;
    private LocalDateTime date_creation;
    private Long user_id;
}
