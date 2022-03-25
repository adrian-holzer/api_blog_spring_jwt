package com.authJWT.authJWT.repository;

import com.authJWT.authJWT.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post,Long> {


     List<Post> findAllByOrderByFechaCreacionDesc();
     List<Post> findAllByPublicadoTrueOrderByFechaCreacionDesc();
     List<Post> findAllByUsuarioNombreUsuarioOrderByFechaCreacionDesc(String NombreUsuario);



}
