package com.authJWT.authJWT.utils;

import com.authJWT.authJWT.security.entity.UsuarioPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UtilsMethods {


    public static UsuarioPrincipal usuarioAuthenticado(){
        Authentication ususarioAuthenticado =  SecurityContextHolder.getContext().getAuthentication();

        try {
            UsuarioPrincipal us = (UsuarioPrincipal) ususarioAuthenticado.getPrincipal();
            return us;
        }catch (Exception ex){


            return null;
        }
    }



    public static boolean UserIsAdmin(){

        UsuarioPrincipal auth = UtilsMethods.usuarioAuthenticado();

        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return false;

    }






}
