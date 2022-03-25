package com.authJWT.authJWT.service;


import com.authJWT.authJWT.entity.Categoria;
import com.authJWT.authJWT.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {



   @Autowired
   private CategoriaRepository categoriaRepository ;




    public List<Categoria> getAll(){

        return  this.categoriaRepository.findAll();

    }

   public Optional<Categoria> getByNombre(String nombreCategoria){

    return  this.categoriaRepository.findByNombreIgnoreCase(nombreCategoria);
   }


    public List<Categoria>  findAllByActivoTrue(){

        return  this.categoriaRepository.findAllByActivoTrue();
    }





    public Optional<Categoria> getById(Long id){

        return  this.categoriaRepository.findById(id);
    }

}
