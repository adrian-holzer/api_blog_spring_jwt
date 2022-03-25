package com.authJWT.authJWT.security.service;


import com.authJWT.authJWT.security.entity.Usuario;
import com.authJWT.authJWT.security.entity.UsuarioPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {



    @Autowired
    UsuarioService usuarioService;



    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {

        Usuario usuario = this.usuarioService.findByNombreUsuario(nombreUsuario).get();
        return UsuarioPrincipal.build(usuario);

    }
}
