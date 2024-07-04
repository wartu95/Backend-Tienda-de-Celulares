package com.cibertec.backend.config.security;

import com.cibertec.backend.services.UserDetailServiceImpl;
import com.cibertec.backend.utils.constants.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader(Constants.HEADER_AUTHORIZATION_KEY);
        //System.out.println(tokenHeader);
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){
//            System.out.println("OK");
            String token = tokenHeader.substring(7);
//            System.out.println(token);

            if(jwtUtils.isJWTValid(token)){
//                System.out.println("VALID");
                String username="";
                try {
                    username = jwtUtils.getUserNameFromToken(token);
                    //System.out.println(username);
                }catch (Exception e){
                    throw new RuntimeException();
                }

                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
//        System.out.println("filter");
        filterChain.doFilter(request, response);
    }
}
