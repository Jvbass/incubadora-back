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

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Inyección de dependencias
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }



    /*Registro */
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

       // También añadimos el rol al token durante el registro para un inicio de sesión inmediato
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("userId", user.getId()); // Añadimos el ID del usuario para futuras referencias"

        String token = jwtService.generateToken(extraClaims, user); // Pasamos user directamente
        return new AuthResponse(token);
    }


/*Login */
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // Creamos las claims personalizadas para el token
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("userId", user.getId());

        String token = jwtService.generateToken(extraClaims, user);

        return new AuthResponse(token);
    }
}