package com.authJWT.authJWT.security.dto;

import com.authJWT.authJWT.security.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtDto {


    private  String token ;
    private  String baerer="Baerer" ;
    private  String nombreUsuario ;
    private Usuario usuario ;
    private Collection<? extends GrantedAuthority> authorities;


    public JwtDto() {
    }

    public JwtDto(String token, String nombreUsuario,Usuario usuario , Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.authorities = authorities;
        this.usuario= usuario;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBaerer() {
        return baerer;
    }

    public void setBaerer(String baerer) {
        this.baerer = baerer;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
