package com.incubadora.incubadora.dev.service;

import com.incubadora.incubadora.dev.dto.AuthResponse;
import com.incubadora.incubadora.dev.dto.LoginRequest;
import com.incubadora.incubadora.dev.dto.RegisterRequest;
import com.incubadora.incubadora.dev.entity.core.Role;
import com.incubadora.incubadora.dev.entity.core.User;
import com.incubadora.incubadora.dev.exception.UserException;
import com.incubadora.incubadora.dev.repository.RoleRepository;
import com.incubadora.incubadora.dev.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
*
* */

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Inyección de dependencias por constructor
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        // 1. Validar si el usuario o email ya existen y lanzar nuestra excepción personalizada
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserException("El nombre de usuario '" + request.getUsername() + "' ya está en uso.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException("El email '" + request.getEmail() + "' ya está en uso.");
        }

        // 2. Obtener el rol por defecto (ej. "Desarrollador")
        Role userRole = roleRepository.findByName("Desarrollador")
                .orElseThrow(() -> new IllegalStateException("El rol 'Desarrollador' no se encontró. Asegúrate de que exista en la BD."));

        // 3. Crear y guardar el nuevo usuario
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword())); // Hashear la contraseña
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(userRole);

        userRepository.save(user);

        // 4. Generar y devolver el token
        // Para UserDetails, Spring Security necesita una implementación. Por ahora, creamos una simple.
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash()) // Spring no lo usa aquí, pero es buena práctica ponerlo
                .authorities("ROLE_" + user.getRole().getName()) // ej. "ROLE_Desarrollador"
                .build();

        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Autenticar al usuario con Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Si la autenticación es exitosa, buscar el usuario para generar el token
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // 3. Generar y devolver el token
        String token = jwtService.generateToken(user.toUserDetails()); // Necesitamos un método en User para convertir a UserDetails
        return new AuthResponse(token);
    }
}