package com.incubadora.incubadora.dev.repository;

import com.incubadora.incubadora.dev.entity.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Busca un usuario por su nombre de usuario.
     * Es utilizado por Spring Security para cargar los detalles del usuario durante la autenticación.
     * @param username El nombre de usuario a buscar.
     * @return un Optional que contiene al usuario si se encuentra, o un Optional vacío si no.
     */
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * Verifica si ya existe un usuario con el nombre de usuario proporcionado.
     * Es útil para la validación durante el registro para evitar duplicados.
     * @param username El nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si ya existe un usuario con el email proporcionado.
     * Es útil para la validación durante el registro para evitar duplicados.
     * @param email El email a verificar.
     * @return true si el email existe, false en caso contrario.
     */
    boolean existsByEmail(String email);
}

