package com.prueba_proteccion.prueba.dto.tickets;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketsDto {
    private Long id;
    private String type;
    private Long priority;
    private LocalDateTime date_creation;
    private Long user_id;
}
