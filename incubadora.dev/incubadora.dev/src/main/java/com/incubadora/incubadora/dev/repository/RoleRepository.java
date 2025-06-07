package com.incubadora.incubadora.dev.repository;

import com.incubadora.incubadora.dev.entity.core.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Busca un rol por su nombre.
     * Es utilizado durante el registro de un nuevo usuario para asignarle un rol por defecto (ej. "Desarrollador").
     * @param name El nombre del rol a buscar.
     * @return un Optional que contiene el rol si se encuentra, o un Optional vacío si no.
     */
    Optional<Role> findByName(String name);
}
