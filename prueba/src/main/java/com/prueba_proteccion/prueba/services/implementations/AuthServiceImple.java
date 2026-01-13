package com.prueba_proteccion.prueba.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.prueba_proteccion.prueba.dto.auth.AuthDto;
import com.prueba_proteccion.prueba.dto.auth.LoginDto;
import com.prueba_proteccion.prueba.dto.auth.UserDetailDto;
import com.prueba_proteccion.prueba.mappers.users.UserMappers;
import com.prueba_proteccion.prueba.repositories.auth.AuthQueryRepository;
import com.prueba_proteccion.prueba.repositories.role.RoleQueryRepository;
import com.prueba_proteccion.prueba.repositories.users.UserJPARepository;
import com.prueba_proteccion.prueba.repositories.users.UsersQueryRepository;
import com.prueba_proteccion.prueba.security.JwtTokenProvider;
import com.prueba_proteccion.prueba.services.AuthService;
import com.prueba_proteccion.prueba.services.UserService;
import com.prueba_proteccion.prueba.utils.AESencryptUtil;
import com.prueba_proteccion.prueba.utils.GlobalException;
import com.prueba_proteccion.prueba.utils.MapperRepository;

@Service
public class AuthServiceImple implements AuthService{
        private static final Integer TIME_TOKEN = 5;

    private static final String SEPARATOR = "---";

    @Autowired
    private AESencryptUtil encrypt;

    @Autowired
    private AuthQueryRepository authQueryRepository;

    @Autowired
    private MapperRepository mapperRepository;

    @Autowired
    private JwtTokenProvider _jwtTokenProvider;

    private final AuthenticationManager _authenticationManager;

    private final UserService _userService;

    private final UsersQueryRepository userQueryRepository;
    private final UserJPARepository userJPARepository;
    private final UserMappers userMappers;
    private final RoleQueryRepository roleQueryRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImple(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider,
            UsersQueryRepository userQueryRepository, UserJPARepository userJPARepository, UserMappers userMappers, PasswordEncoder passwordEncoder,
            RoleQueryRepository roleQueryRepository
            ) {
        _jwtTokenProvider = jwtTokenProvider;
        this.userQueryRepository = userQueryRepository;
        this.userJPARepository = userJPARepository;
        this.userMappers = userMappers;
        this.roleQueryRepository = roleQueryRepository;
        this.passwordEncoder = passwordEncoder;
        _authenticationManager = authenticationManager;
        _userService = userService;
    }

    public AuthDto login(LoginDto loginDto) {
        try {
            Authentication authentication = _authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Obtener el usuario
            UserDetailDto user = authQueryRepository.findByUserLogin(loginDto.getEmail());
            if (user == null) {
                throw new GlobalException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
                
            // Generación del token
            String token = _jwtTokenProvider.generateToken(authentication, loginDto.getEmail());
            
            // Crear DTO de respuesta
            AuthDto authDto = new AuthDto();
            authDto.setUser(user);
            authDto.setToken(token);
            
            return authDto;
        } catch (BadCredentialsException e) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
