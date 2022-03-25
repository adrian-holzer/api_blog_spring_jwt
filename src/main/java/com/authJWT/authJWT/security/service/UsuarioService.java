package com.authJWT.authJWT.security.service;


import com.authJWT.authJWT.security.entity.Usuario;
import com.authJWT.authJWT.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {



    @Autowired
    UsuarioRepository usuarioRepository;


    public Optional<Usuario> findByNombreUsuario(String nombreUsuario){

        return  this.usuarioRepository.findByNombreUsuarioOrderByPostsIdAsc(nombreUsuario);

    }



    public boolean existsByNombreUsuario(String nombreUsuario){

        return  this.usuarioRepository.existsByNombreUsuario(nombreUsuario);

    }


    public boolean existsByEmail(String email){

        return  this.usuarioRepository.existsByEmail(email);

    }



    public void save(Usuario usuario){

       this.usuarioRepository.save(usuario);

    }

    public List<Usuario> findAll(){
        return this.usuarioRepository.findAll();
    }
}
