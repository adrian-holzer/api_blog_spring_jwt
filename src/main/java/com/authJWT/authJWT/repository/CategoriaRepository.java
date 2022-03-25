package com.authJWT.authJWT.repository;

import com.authJWT.authJWT.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {


    Optional<Categoria> findByNombreIgnoreCase(String nombre);


    List<Categoria> findAllByActivoTrue();



}
