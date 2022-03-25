package com.authJWT.authJWT.security.entity;

import com.authJWT.authJWT.security.enums.RolNombre;
import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Rol {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id ;


    @NotNull
    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre ;



    public Rol() {

    }



    public Rol(RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RolNombre getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }
}
