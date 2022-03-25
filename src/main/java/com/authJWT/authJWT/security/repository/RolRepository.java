package com.authJWT.authJWT.security.repository;

import com.authJWT.authJWT.security.entity.Rol;
import com.authJWT.authJWT.security.entity.Usuario;
import com.authJWT.authJWT.security.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {



    Optional<Rol> findByRolNombre(RolNombre rolNombre);

}
