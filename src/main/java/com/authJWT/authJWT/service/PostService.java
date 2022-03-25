package com.authJWT.authJWT.service;


import com.authJWT.authJWT.entity.Comentario;
import com.authJWT.authJWT.entity.Post;
import com.authJWT.authJWT.repository.CategoriaRepository;
import com.authJWT.authJWT.repository.ComentarioRepository;
import com.authJWT.authJWT.repository.PostRepository;
import com.authJWT.authJWT.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UsuarioRepository usuarioRepository;


    @Autowired
    CategoriaRepository categoriaRepository;


    @Autowired
    ComentarioRepository comentarioRepository;






    public void savePost(Post post){
        this.postRepository.save(post);

    }


    public Optional<Post> findById(Long id){


            return this.postRepository.findById(id);


    }


    public List<Post> findAllOrderByFechaCreacion(){

        return this.postRepository.findAllByOrderByFechaCreacionDesc();

    }




    public List<Post> findbyUser(String nombreUsuario){

        return this.postRepository.findAllByUsuarioNombreUsuarioOrderByFechaCreacionDesc(nombreUsuario);

    }


    public List<Post> findByPublicadoTrueOrderByFechaCreacion(){

        return this.postRepository.findAllByPublicadoTrueOrderByFechaCreacionDesc();

    }


    public void delete(Post post){

        post.removePost();

        this.postRepository.delete(post);
    }

}
