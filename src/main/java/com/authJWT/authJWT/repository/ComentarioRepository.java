package com.authJWT.authJWT.repository;

import com.authJWT.authJWT.entity.Comentario;
import com.authJWT.authJWT.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {


    List<Comentario> findByPost(Post post);


    void  deleteAllByPost(Post post);

}
