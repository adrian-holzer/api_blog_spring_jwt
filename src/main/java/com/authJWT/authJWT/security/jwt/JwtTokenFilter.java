package com.authJWT.authJWT.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtTokenFilter extends OncePerRequestFilter {


    private static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {


        try {
            String token = getToken(req);

            if (token!=null && jwtProvider.validateToken(token)){
                String nombteUsuario = jwtProvider.getNombreUsuarioFromToken(token);

                UserDetails userDetails= userDetailsService.loadUserByUsername(nombteUsuario);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }catch (Exception e){

            logger.error("fail en el metodo doFilter"+ e);
        }
        filterChain.doFilter(req,res);
    }


    private  String getToken(HttpServletRequest request){

        String header = request.getHeader("Authorization");
        if (header!=null && header.startsWith("Bearer")){
            return  header.replace("Bearer","");

        }
        return  null;

    }
}
