package com.prueba_proteccion.prueba.entity.tickets;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class TicketsEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private Long prioridad_manual;

    private LocalDateTime fecha_creation;

    private Long user_id;
    
	@Column(name = "updated_at")
	private LocalDateTime updated_at;

    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;

    @Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime created_at;
    
    @PrePersist
	protected void onCreate() {
		created_at = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updated_at = LocalDateTime.now();
	}
}
