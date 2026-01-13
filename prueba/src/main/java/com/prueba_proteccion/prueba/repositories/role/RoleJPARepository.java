package com.prueba_proteccion.prueba.repositories.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba_proteccion.prueba.entity.role.Role;
import com.prueba_proteccion.prueba.entity.role.RoleEntity;



public interface RoleJPARepository extends JpaRepository<RoleEntity, Long> {
    Optional<Role> findByNombre(String nombre);
    public Optional<RoleEntity> existsByNombre(String nombre);
}
