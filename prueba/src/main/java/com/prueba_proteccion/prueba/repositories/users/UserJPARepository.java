package com.prueba_proteccion.prueba.repositories.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba_proteccion.prueba.entity.users.UsersEntity;

public interface UserJPARepository extends JpaRepository<UsersEntity, Long>  {
    public Optional<UsersEntity> findByEmail(String email);
}
