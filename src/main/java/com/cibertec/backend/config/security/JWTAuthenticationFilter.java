package com.cibertec.backend.config.security;

import com.cibertec.backend.entites.UserAppWeb;
import com.cibertec.backend.utils.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JWTUtils jwtUtils;

    public JWTAuthenticationFilter(JWTUtils jwtUtils){
        this.jwtUtils=jwtUtils;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UserAppWeb userAppWeb=null;
        String mail="";
        String password="";

        try {

            ServletInputStream servletInputStream= request.getInputStream();

            userAppWeb=new ObjectMapper().readValue(servletInputStream, UserAppWeb.class);
            mail=userAppWeb.getName();
            password=userAppWeb.getPassword();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(mail,password);


        return getAuthenticationManager().authenticate(authenticationToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        User user=(User) authResult.getPrincipal();

        List<String> userRoles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token="";
        Date expiryDate = new Date();
        try {
            token=jwtUtils.generateAccessToken(user.getUsername(),userRoles);
            expiryDate=jwtUtils.getClaim(token, Claims::getExpiration);
        }catch (Exception e){
            throw new RuntimeException(e);
        }


        response.addHeader(Constants.HEADER_AUTHORIZATION_KEY,token);
        Map<String,Object> httpResponse=new HashMap<>();
        httpResponse.put("token", token);
        httpResponse.put("Message","Autenticación correcta");
        httpResponse.put("Username",user.getUsername());
        httpResponse.put("expiration",expiryDate);

        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }


}
