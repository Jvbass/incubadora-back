package com.incubadora.incubadora.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // --- DEPENDENCIAS INYECTADAS ---
    // Spring buscará estos beans en su contenedor (definidos en ApplicationConfig)

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider; // Inyectamos el provider

    // El UserRepository ya no es necesario aquí, porque el AuthenticationProvider
    // ya lo tiene a través del UserDetailsService.
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    /*
    *  lista destrings permitidos sin autorizacion
    * */
    private static final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/api/health",
            "/api/auth/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(PUBLIC_URLS).permitAll() // PUBLIC_URLS permitidas
                                .anyRequest().authenticated() // para cualquier otra petición, requiere autenticación
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Usamos el bean que fue inyectado
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // Le decimos a Spring que use el filtro jwtAuthFilter.java antes de su filtro de autenticación estándar.


        return http.build();
    }

}
