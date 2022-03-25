package com.authJWT.authJWT.entity;

import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;



@Entity
public class Categoria {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id ;


    @NotBlank
    private String nombre ;


    @NotNull
    private boolean activo;


    @OneToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL, mappedBy = "categoria")
    private Set<Post>posts= new HashSet<>();



    public Categoria(@NotBlank String nombre, @NotNull boolean activo) {
        this.nombre = nombre;
        this.activo = activo;
    }

    public  Categoria(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
