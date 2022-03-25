package com.authJWT.authJWT.controller;


import com.authJWT.authJWT.dto.Mensaje;
import com.authJWT.authJWT.entity.Categoria;
import com.authJWT.authJWT.security.entity.UsuarioPrincipal;
import com.authJWT.authJWT.service.CategoriaService;
import com.authJWT.authJWT.utils.UtilsMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {


    @Autowired
    CategoriaService categoriaService ;



    @GetMapping("")
    public ResponseEntity getCategorias(){

        if (UtilsMethods.UserIsAdmin()){
            return new ResponseEntity(this.categoriaService.getAll(), HttpStatus.OK);
        }

        return new ResponseEntity(this.categoriaService.findAllByActivoTrue(), HttpStatus.OK);
    }



    @GetMapping("/{nombre}")
    public ResponseEntity getCategoriaByNombre(@PathVariable String nombre){


        Categoria categoria = this.categoriaService.getByNombre(nombre).orElse(null);


        if (categoria==null){
            return new ResponseEntity(new Mensaje("No se encontró la Categoria con nombre "+ nombre),HttpStatus.NOT_FOUND);
        }

        if (categoria.isActivo()){
            return new ResponseEntity(categoria, HttpStatus.OK);
        }

        if (UtilsMethods.UserIsAdmin()){
            return new ResponseEntity(categoria, HttpStatus.OK);
        }

        return new ResponseEntity(new Mensaje("No se encontró la Categoria con nombre "+ nombre),HttpStatus.NOT_FOUND);

    }


    // Modifico la categoria solo si soy admin


    @PutMapping("/{nombre}")
    public ResponseEntity updateCategoriaByNombre(@PathVariable String nombre){


        Categoria categoria = this.categoriaService.getByNombre(nombre).orElse(null);


        if (categoria==null){
            return new ResponseEntity(new Mensaje("No se encontró la Categoria con nombre "+ nombre),HttpStatus.NOT_FOUND);
        }

        if (UtilsMethods.UserIsAdmin()){

            // Modifico


            return new ResponseEntity(categoria, HttpStatus.OK);
        }

        return new ResponseEntity(new Mensaje("No tiene permisos para modificar"),HttpStatus.NOT_FOUND);

    }



    @DeleteMapping("/{nombre}")
    public ResponseEntity deleteCategoria(@PathVariable String nombre){


        Categoria categoria = this.categoriaService.getByNombre(nombre).orElse(null);


        if (categoria==null){
            return new ResponseEntity(new Mensaje("No se encontró la Categoria con nombre "+ nombre),HttpStatus.NOT_FOUND);
        }

        if (UtilsMethods.UserIsAdmin()){

            // Elimino la categoria


            return new ResponseEntity(categoria, HttpStatus.OK);
        }

        return new ResponseEntity(new Mensaje("No tiene permisos para modificar"),HttpStatus.NOT_FOUND);

    }

}
