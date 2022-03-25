package com.authJWT.authJWT.security.controller;


import com.authJWT.authJWT.dto.Mensaje;
import com.authJWT.authJWT.entity.Post;
import com.authJWT.authJWT.security.dto.JwtDto;
import com.authJWT.authJWT.security.dto.LoginUsuario;
import com.authJWT.authJWT.security.dto.NuevoUsuario;
import com.authJWT.authJWT.security.entity.Rol;
import com.authJWT.authJWT.security.entity.Usuario;
import com.authJWT.authJWT.security.entity.UsuarioPrincipal;
import com.authJWT.authJWT.security.enums.RolNombre;
import com.authJWT.authJWT.security.jwt.JwtProvider;
import com.authJWT.authJWT.security.service.RolService;
import com.authJWT.authJWT.security.service.UsuarioService;
import com.authJWT.authJWT.service.PostService;
import com.authJWT.authJWT.utils.ApiError;
import com.authJWT.authJWT.utils.UtilsMethods;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;


    @Autowired
    private PostService postService;



    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario,
                                   BindingResult bindingResult){

        if (bindingResult.hasErrors()){

            return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())){

            return new ResponseEntity<>(new Mensaje("Ese nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByEmail(nuevoUsuario.getEmail())){

            return new ResponseEntity<>(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);
        }
        Usuario usuario = new Usuario(nuevoUsuario.getNombre(),
                nuevoUsuario.getNombreUsuario(),nuevoUsuario.getEmail(),
                passwordEncoder.encode(nuevoUsuario.getPassword()));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.findByRolNombre(RolNombre.ROLE_USER).get());

        if (nuevoUsuario.getRoles().contains("admin")){
            roles.add(rolService.findByRolNombre(RolNombre.ROLE_ADMIN).get());

        }
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        return new ResponseEntity<>(new Mensaje("Usuario guardado"),HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, Errors errors , BindingResult bindingResult){


//
//        if (bindingResult.hasErrors()){
//            return  new ResponseEntity(new ApiError(bindingResult.ge), HttpStatus.BAD_REQUEST);
//        }


        if(errors.hasErrors()){
            return  new ResponseEntity(new ApiError(errors), HttpStatus.BAD_REQUEST);

        }
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(),loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt= jwtProvider.generateToken(authentication);
        UserDetails userDetails=(UserDetails)authentication.getPrincipal();
        Usuario u =  this.usuarioService.findByNombreUsuario(userDetails.getUsername()).get();
        JwtDto jwtDto= new JwtDto(jwt, userDetails.getUsername(),u,userDetails.getAuthorities());

        return  new ResponseEntity(jwtDto,HttpStatus.OK);

    }


    @GetMapping("")
    public ResponseEntity getAllUsuarios() {


        List<Usuario>listUsuarios = this.usuarioService.findAll();


        return  new ResponseEntity(listUsuarios, HttpStatus.OK);

    }



    @GetMapping("/userLogged")
    public ResponseEntity getUserLogged() {


        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();



            if (principal instanceof UserDetails) {

                String username = ((UserDetails)principal).getUsername();
                Usuario u = this.usuarioService.findByNombreUsuario(username).get();
                return  new ResponseEntity(u, HttpStatus.OK);
            } else {

               String username = principal.toString();
               Usuario u = this.usuarioService.findByNombreUsuario(username).get();
                return  new ResponseEntity(u, HttpStatus.NOT_FOUND);
            }

        }catch (NoSuchElementException se){

            return  new ResponseEntity(null, HttpStatus.OK);
        }





    }



    @GetMapping("/{nombreUsuario}")
    public ResponseEntity getUsuario(@PathVariable String nombreUsuario) {


        Usuario u =  this.usuarioService.findByNombreUsuario(nombreUsuario).orElse(null);



        if (u!=null){
            return new ResponseEntity(u, HttpStatus.OK);
        }

        return  new ResponseEntity(new Mensaje("No se encontró el usuario con nombre de Usuario "+ nombreUsuario), HttpStatus.NOT_FOUND);

    }




    // Favoritos



    @PostMapping("/favorito")
    public ResponseEntity addFavorito(@RequestBody  Long postFav) {


        UsuarioPrincipal u =  UtilsMethods.usuarioAuthenticado();

        Usuario user = this.usuarioService.findByNombreUsuario(u.getUsername()).orElse(null);


        // Agrego favorito

        Post post = this.postService.findById(postFav).orElse(null);

        if (post==null || !post.getPublicado() ){
            return new ResponseEntity(new Mensaje("No se encontró el Post con id " + postFav),HttpStatus.NOT_FOUND);
        }


        user.getPostsFavoritos().add(post);
        this.usuarioService.save(user);

        return  new ResponseEntity(new Mensaje("Se ha agregado a favorito el post con id "+ postFav ), HttpStatus.CREATED);

    }





    @DeleteMapping("/favoritos/{id}")
    public ResponseEntity deleteFavorito(@PathVariable Long id) {


        UsuarioPrincipal u =  UtilsMethods.usuarioAuthenticado();

        Usuario user = this.usuarioService.findByNombreUsuario(u.getUsername()).orElse(null);

        Post post = this.postService.findById(id).orElse(null);


        if (!post.getPublicado() || post==null || !user.getPostsFavoritos().contains(post)){
            return new ResponseEntity(new Mensaje("No se encontró el Post con id " + id + " entre los favoritos"),HttpStatus.NOT_FOUND);
        }


        user.getPostsFavoritos().remove(post);

        this.usuarioService.save(user);

        return  new ResponseEntity(new Mensaje("Se ha eliminado a favorito el post con id "+ id ), HttpStatus.OK);

    }








//
//    @PostMapping("/favorito/{id}")
//    public ResponseEntity delete(@PathVariable Long id){
//
//        Post post = this.postService.findById(id).orElse(null);
//        if (post==null){
//            return new ResponseEntity(new Mensaje("No se encontró el Post con id "+ id),HttpStatus.NOT_FOUND);
//        }
//        if (post.getUsuario().getNombreUsuario().equals(UtilsMethods.usuarioAuthenticado().getUsername()) || UtilsMethods.UserIsAdmin()){
//
//            this.postService.delete(post);
//
//            return new ResponseEntity(new Mensaje("Post eliminado correctamente"), HttpStatus.OK);
//        }
//        return new ResponseEntity(new Mensaje("No tiene los permisos para elimnar el post"),HttpStatus.FORBIDDEN);
//    }


}
