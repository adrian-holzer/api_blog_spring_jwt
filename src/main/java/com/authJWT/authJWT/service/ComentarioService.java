package com.authJWT.authJWT.service;


import com.authJWT.authJWT.entity.Comentario;
import com.authJWT.authJWT.entity.Post;
import com.authJWT.authJWT.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {


    @Autowired
    ComentarioRepository comentarioRepository;



    public List<Comentario> getComentariosByPost(Post post){
        return this.comentarioRepository.findByPost(post);
    }

    public Optional<Comentario> findById(Long id){
       return  this.comentarioRepository.findById(id);
    }

    public void save(Comentario comentario){
        this.comentarioRepository.save(comentario);
    }

    public void delete(Comentario comentario){
        this.comentarioRepository.delete(comentario);
    }

}
