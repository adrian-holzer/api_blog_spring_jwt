package com.authJWT.authJWT.security;


import com.authJWT.authJWT.security.jwt.JwtEntryPoint;
import com.authJWT.authJWT.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MainSecurity extends WebSecurityConfigurerAdapter {



    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtEntryPoint jwtEntryPoint;




    @Bean
    public JwtTokenFilter JwtTokenFilter(){
        return new JwtTokenFilter();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/login","/auth/nuevo").permitAll()
                .antMatchers(HttpMethod.GET,"/auth").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST,"/post").authenticated()
                .antMatchers(HttpMethod.PUT,"/post/{id}").authenticated()
                .antMatchers(HttpMethod.POST,"/post/{id}/comentario").authenticated()
                .antMatchers(HttpMethod.GET,"/post/user/{userName}").authenticated()
                .antMatchers(HttpMethod.POST,"/post/image/upload").authenticated()
                .antMatchers(HttpMethod.POST,"/post/{id}/image/upload").authenticated()
                .antMatchers(HttpMethod.GET,"/post/**","/categoria/**").permitAll()
                //.anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }


}
