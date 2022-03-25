package com.authJWT.authJWT.security.entity;


import com.authJWT.authJWT.entity.Comentario;
import com.authJWT.authJWT.entity.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id ;


    @NotNull
    private String nombre ;

    @NotNull
    @Column(unique = true)
    private String nombreUsuario ;

    @NotNull
    private String email ;

    @NotNull
    @JsonIgnore
    private String password ;

    @ManyToMany(fetch = FetchType.EAGER )
    @JoinTable( name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_id") , inverseJoinColumns =
            @JoinColumn(name = "rol_id"))

    private Set<Rol> roles = new HashSet<Rol>();




    @OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.EAGER , mappedBy = "usuario")
    private Set<Post>posts= new HashSet<Post>();


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_postFavorito", joinColumns = @JoinColumn(name = "usuario_id") , inverseJoinColumns =
    @JoinColumn(name = "post_id"))
    @JsonIgnoreProperties("usuario")
    private Set<Post>postsFavoritos= new HashSet<Post>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER ,  mappedBy = "usuario")
    @JsonIgnoreProperties(value = {"usuario"})
    private Set<Comentario>comentarios= new HashSet<Comentario>();


    public  Usuario(){

    }

    public Usuario(String nombre, String nombreUsuario, String email, String password) {
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }


    public Set<Post> getPostsFavoritos() {
        return postsFavoritos;
    }

    public void setPostsFavoritos(Set<Post> postsFavoritos) {
        this.postsFavoritos = postsFavoritos;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
}
