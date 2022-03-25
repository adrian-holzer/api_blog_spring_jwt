package com.authJWT.authJWT.dto;

import javax.validation.constraints.NotBlank;

public class ComentarioDto {


    @NotBlank
    private String contenido ;


    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
