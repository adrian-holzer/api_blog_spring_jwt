package com.authJWT.authJWT.dto;

import com.authJWT.authJWT.security.entity.Usuario;
import com.authJWT.authJWT.security.entity.UsuarioPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


public class PostDto implements Serializable {




    @NotBlank
    private String titulo ;

    @NotBlank
    private String contenido ;


    private Boolean publicado;

    private String categoria ;


    private Usuario usuario ;


    public PostDto(@NotBlank String titulo, @NotBlank String contenido,  Boolean publicado, String categoria) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.publicado = publicado;
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


    public Boolean getPublicado() {
        return publicado;
    }

    public void setPublicado(Boolean publicado) {
        this.publicado = publicado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }





}
