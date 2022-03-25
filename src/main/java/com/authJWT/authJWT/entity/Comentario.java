package com.authJWT.authJWT.entity;


import com.authJWT.authJWT.security.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sun.istack.Nullable;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Comentario {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id ;


    @NotBlank
    private String contenido ;



    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaModificacion;


    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value ={ "roles", "email", "postsFavoritos","comentarios","posts"})
    private Usuario usuario;


    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

//    public Comentario getComentario() {
//        return comentario;
//    }
//
//    public void setComentario(Comentario comentario) {
//        this.comentario = comentario;
//    }

//    public Set<Comentario> getComentarios() {
//        return comentarios;
//    }
//
//    public void setComentarios(Set<Comentario> comentarios) {
//        this.comentarios = comentarios;
//    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }


    public void remove(){
        this.getPost().getComentarios().remove(this);
        this.getUsuario().getComentarios().remove(this);
    }


}
