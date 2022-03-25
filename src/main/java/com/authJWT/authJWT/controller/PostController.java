package com.authJWT.authJWT.controller;

import com.authJWT.authJWT.dto.ComentarioDto;
import com.authJWT.authJWT.dto.Mensaje;
import com.authJWT.authJWT.dto.PostDto;
import com.authJWT.authJWT.entity.Categoria;
import com.authJWT.authJWT.entity.Comentario;
import com.authJWT.authJWT.entity.Post;
import com.authJWT.authJWT.security.entity.Usuario;
import com.authJWT.authJWT.security.entity.UsuarioPrincipal;
import com.authJWT.authJWT.security.service.UserDetailsServiceImpl;
import com.authJWT.authJWT.security.service.UsuarioService;
import com.authJWT.authJWT.service.CategoriaService;
import com.authJWT.authJWT.service.CloudinaryService;
import com.authJWT.authJWT.service.ComentarioService;
import com.authJWT.authJWT.service.PostService;
import com.authJWT.authJWT.utils.ApiError;
import com.authJWT.authJWT.utils.UtilsMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.naming.SizeLimitExceededException;
import javax.servlet.MultipartConfigElement;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/post")
@CrossOrigin("*")
public class PostController {



    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PostService postService;


    @Autowired
    private ComentarioService comentarioService;




    @GetMapping("")
    public ResponseEntity getAllPosts(){
        if (UtilsMethods.usuarioAuthenticado()!=null && UtilsMethods.UserIsAdmin()){
            return new ResponseEntity(this.postService.findAllOrderByFechaCreacion(), HttpStatus.OK);
        }
        return new ResponseEntity(this.postService.findByPublicadoTrueOrderByFechaCreacion(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id){

        Post post = this.postService.findById(id).orElse(null);

        if (post==null){
            return new ResponseEntity(new Mensaje("No se encontró el Post con id "+ id),HttpStatus.NOT_FOUND);
        }
        if(!post.getPublicado() ){

            if (UtilsMethods.usuarioAuthenticado()!=null && (UtilsMethods.UserIsAdmin()||
                    UtilsMethods.usuarioAuthenticado().getUsername().equals(post.getUsuario().getNombreUsuario()))){
                return new ResponseEntity(post, HttpStatus.OK);
            }else {
                return new ResponseEntity(new Mensaje("No se encontró el Post con id "+ id),HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity(post, HttpStatus.OK);
    }




    @GetMapping("/user/{userName}")
    public ResponseEntity getById(@PathVariable String userName){

        List<Post> posts = this.postService.findbyUser(userName);



        return new ResponseEntity(posts, HttpStatus.OK);
    }








    // Upload Image


    @PostMapping(value = "/image/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadImage( @RequestParam(value = "id", required = false) Long id ,@RequestParam(value = "img", required = false) MultipartFile img  ) throws IOException{


        UsuarioPrincipal us = UtilsMethods.usuarioAuthenticado();

      //  Usuario u = this.usuarioService.findByNombreUsuario(us.getUsername()).get();

        Post p ;

        BufferedImage bi = ImageIO.read( (img.getInputStream()));


        if (bi==null){
            return new ResponseEntity(new Mensaje("Imagen no válida"),HttpStatus.BAD_REQUEST);

        }



        // If id is null , get the last post created and update image , else , get the post with id then update it

        if (id== null) {

             p = this.postService.findbyUser(us.getUsername()).get(0);

        }else
        {
             p = this.postService.findById(id).orElse(null);

            if (p==null ){
            return new ResponseEntity(new Mensaje("No se encontró el Post con id "+ id),HttpStatus.NOT_FOUND);
        }
        if (!p.getUsuario().getNombreUsuario().equals(us.getUsername())  && !UtilsMethods.UserIsAdmin()  ){
            return new ResponseEntity(new Mensaje("No puede modificar un post que no le pertenece"),HttpStatus.FORBIDDEN);
        }

        }

            try {

                if (p.getImg() != null) {

                    String file = p.getImg().split("/")[7];
                    String idFile = file.split("\\.")[0];


                    cloudinaryService.delete(idFile);
                }


                Map result = cloudinaryService.upload(img);

                p.setImg(result.get("url").toString());

                this.postService.savePost(p);

                return new ResponseEntity(new Mensaje("Imagen guardada correctamente"), HttpStatus.OK);

            } catch (IOException e) {
            e.printStackTrace();

            return new ResponseEntity(new Mensaje("No se ha podido guardar la imágen"),HttpStatus.BAD_REQUEST);
        }
        catch (RuntimeException re){
            re.printStackTrace();

            return new ResponseEntity(new Mensaje("La imágen es demasiado grande"),HttpStatus.BAD_REQUEST);
        }

    }



    @PostMapping(value="")
    public ResponseEntity addPost( @RequestBody @Valid PostDto postDto ,  Errors errors) throws IOException{

        if (errors.hasErrors()){

            return new ResponseEntity(new ApiError(errors),HttpStatus.BAD_REQUEST);
        }



        UsuarioPrincipal us = UtilsMethods.usuarioAuthenticado();

        Usuario usuario = this.usuarioService.findByNombreUsuario(us.getUsername()).get();

        Post newPost= new Post();


        newPost.setContenido(postDto.getContenido());
        newPost.setTitulo(postDto.getTitulo());
        newPost.setUsuario(usuario);
        newPost.setPublicado(true);

        if (postDto.getCategoria()!=null){
            Categoria c = this.categoriaService.getByNombre(postDto.getCategoria()).orElse(null);
            if (c!=null){
                newPost.setCategoria(c);
            }else{
                newPost.setCategoria(null);
            }
        }else {
            newPost.setCategoria(null);
        }

        this.postService.savePost(newPost);

        return new ResponseEntity("Se ha guardado el nuevo post", HttpStatus.CREATED);

    }




    @PutMapping("/{id}")
    public ResponseEntity updatePost( @PathVariable Long id, @RequestBody @Valid PostDto postDto ,  Errors errors) {


        if (errors.hasErrors()){

            return new ResponseEntity(new ApiError(errors),HttpStatus.BAD_REQUEST);
        }

        UsuarioPrincipal us = UtilsMethods.usuarioAuthenticado();


        Post updatePost= this.postService.findById(id).orElse(null);




        if (updatePost==null ){
            return new ResponseEntity(new Mensaje("No se encontró el Post con id "+ id),HttpStatus.NOT_FOUND);
        }
        if (!updatePost.getUsuario().getNombreUsuario().equals(us.getUsername())  && !UtilsMethods.UserIsAdmin()  ){
            return new ResponseEntity(new Mensaje("No puede modificar un post que no le pertenece"),HttpStatus.FORBIDDEN);
        }


        updatePost.setTitulo(postDto.getTitulo());
        updatePost.setContenido(postDto.getContenido());

        updatePost.setPublicado((postDto.getPublicado()));


        if (postDto.getCategoria()!=null){
            Categoria c = this.categoriaService.getByNombre(postDto.getCategoria()).orElse(null);
            if (c!=null){
                updatePost.setCategoria(c);
            }else{
                updatePost.setCategoria(null);
            }
        }else {
            updatePost.setCategoria(null);
        }

        this.postService.savePost(updatePost);


        return new ResponseEntity(new Mensaje("Se ha actualizado el post con id "+ id), HttpStatus.OK);
    }








    // A post can deleted by user owned and user admin
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){

        Post post = this.postService.findById(id).orElse(null);
        if (post==null){
            return new ResponseEntity(new Mensaje("No se encontró el Post con id "+ id),HttpStatus.NOT_FOUND);
        }
        if (post.getUsuario().getNombreUsuario().equals(UtilsMethods.usuarioAuthenticado().getUsername()) || UtilsMethods.UserIsAdmin()){

            this.postService.delete(post);

            return new ResponseEntity(new Mensaje("Post eliminado correctamente"), HttpStatus.OK);
        }
        return new ResponseEntity(new Mensaje("No tiene los permisos para elimnar el post"),HttpStatus.FORBIDDEN);
    }




// Comentarios

    // TODO : Testear comentarios

    @PostMapping("/{id}/comentario")
    public ResponseEntity addComentario(@PathVariable Long id , @RequestBody ComentarioDto comentarioDto){

        Post post = this.postService.findById(id).orElse(null);
        if (post==null || !post.getPublicado()){
            return new ResponseEntity(new Mensaje("No se encontró el Post con id "+ id),HttpStatus.NOT_FOUND);
        }


        Usuario u =   this.usuarioService.findByNombreUsuario(UtilsMethods.usuarioAuthenticado().getUsername()).orElse(null);


        if(u==null){
            return new ResponseEntity(new Mensaje("El usuario no está autenticado"),HttpStatus.CREATED);

        }


        Comentario c = new Comentario();
        c.setPost(post);
        c.setUsuario(u);
        c.setContenido(comentarioDto.getContenido());
        this.comentarioService.save(c);

        return new ResponseEntity(new Mensaje("Comentario creado correctamente"),HttpStatus.CREATED);

    }



    @PutMapping("/{id}/comentario/{id_comentario}")
    public ResponseEntity updateComentario(@PathVariable Long id ,@PathVariable Long id_comentario, @RequestBody ComentarioDto comentarioDto){

        Post post = this.postService.findById(id).orElse(null);
        if (post==null || !post.getPublicado()){
            return new ResponseEntity(new Mensaje("No se encontró el Post con id "+ id),HttpStatus.NOT_FOUND);
        }

        Comentario c = this.comentarioService.findById(id_comentario).orElse(null);

        if (c==null) {
            return new ResponseEntity(new Mensaje("No se encontró el Comentario con id "+ id_comentario),HttpStatus.NOT_FOUND);
        }
        if (!c.getUsuario().getNombreUsuario().equals(UtilsMethods.usuarioAuthenticado().getUsername())){
            return new ResponseEntity(new Mensaje("No puede modificar un Comentario que no le pertenece"),HttpStatus.BAD_REQUEST);
        }

        c.setContenido(comentarioDto.getContenido());
        this.comentarioService.save(c);

        return new ResponseEntity(new Mensaje("Comentario modificado correctamente"),HttpStatus.OK);
    }



    @DeleteMapping("/{id}/comentario/{id_comentario}")
    public ResponseEntity deleteComentario(@PathVariable Long id ,@PathVariable Long id_comentario){

        Post post = this.postService.findById(id).orElse(null);
        if (post==null || !post.getPublicado()){
            return new ResponseEntity(new Mensaje("No se encontró el Post con id "+ id),HttpStatus.NOT_FOUND);
        }

        Comentario c = this.comentarioService.findById(id_comentario).orElse(null);

        if (c==null) {
            return new ResponseEntity(new Mensaje("No se encontró el Comentario con id "+ id_comentario),HttpStatus.NOT_FOUND);
        }




        if (UtilsMethods.UserIsAdmin() || c.getUsuario().getNombreUsuario().equals(UtilsMethods.usuarioAuthenticado().getUsername()) ||
                UtilsMethods.usuarioAuthenticado().getUsername().equals(post.getUsuario().getNombreUsuario())){

            c.remove();
            this.comentarioService.delete(c);

            return new ResponseEntity(new Mensaje("Se eliminó el comentario con id "+ id_comentario),HttpStatus.OK);
        }


            return new ResponseEntity(new Mensaje("No puede eliminar un Comentario que no le pertenece"),HttpStatus.BAD_REQUEST);


    }












}
