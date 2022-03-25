package com.authJWT.authJWT.security.service;

import com.authJWT.authJWT.security.entity.Rol;
import com.authJWT.authJWT.security.entity.Usuario;
import com.authJWT.authJWT.security.enums.RolNombre;
import com.authJWT.authJWT.security.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RolService {



    @Autowired
    RolRepository rolRepository;



    public Optional<Rol> findByRolNombre(RolNombre rolNombre){

       return  this.rolRepository.findByRolNombre(rolNombre);


    }



    public void save(Rol rol){

        this.rolRepository.save(rol);

    }
}
