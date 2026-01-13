package com.prueba_proteccion.prueba.services.implementations;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prueba_proteccion.prueba.dto.tickets.PageTickets;
import com.prueba_proteccion.prueba.dto.users.CreateUserDto;
import com.prueba_proteccion.prueba.dto.users.UserDto;
import com.prueba_proteccion.prueba.entity.users.UsersEntity;
import com.prueba_proteccion.prueba.mappers.users.UserMappers;
import com.prueba_proteccion.prueba.repositories.users.UserJPARepository;
import com.prueba_proteccion.prueba.repositories.users.UsersQueryRepository;
import com.prueba_proteccion.prueba.services.UserService;
import com.prueba_proteccion.prueba.utils.GlobalException;
import com.prueba_proteccion.prueba.utils.PageableDto;



@Service
public class UserServiceImpl implements UserService {

    private final UsersQueryRepository userQueryRepository;
    private final UserJPARepository userJPARepository;
    private final UserMappers userMappers;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersQueryRepository userQueryRepository, UserJPARepository userJPARepository,
            UserMappers userMappers, PasswordEncoder passwordEncoder) {
        this.userQueryRepository = userQueryRepository;
        this.userJPARepository = userJPARepository;
        this.userMappers = userMappers;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto create(CreateUserDto createUserDto) {
        Boolean exists = userQueryRepository.existsByEmail(createUserDto.getEmail().toLowerCase());
        if (exists)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El correo ya se encuentra registrado");

        try {
            String encodedPassword = passwordEncoder.encode(createUserDto.getPassword());
            createUserDto.setPassword(encodedPassword);
            UsersEntity userEntity = userMappers
                    .createToEntity(createUserDto);
            System.out.println("Guardando usuario: " + userEntity.toString());
            UsersEntity saveUserEntity = userJPARepository
                    .save(userEntity);

            return userMappers.toDto(saveUserEntity);
        } catch (Exception e) {
            System.err.println("Error al crear el usuario: " + e.toString());
            e.printStackTrace();
            throw new RuntimeException("Error al crear el usuario: " + e.getMessage(), e);
        }
    }
}
