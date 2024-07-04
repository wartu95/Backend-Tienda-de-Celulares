package com.cibertec.backend.services;

import com.cibertec.backend.entites.UserAppWeb;
import com.cibertec.backend.repositories.IUserAppWebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private IUserAppWebRepository iUserAppWebRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        UserAppWeb userDetails=iUserAppWebRepository.findByName(name)
                .orElseThrow(()->new UsernameNotFoundException("El usuario "+name+" no existe."));

        Collection<? extends GrantedAuthority> authorities=userDetails.getRoles()
                .stream()
                .map(role->new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
                .collect(Collectors.toSet());


        return new User(userDetails.getName(),
                userDetails.getPassword(),true,true,true,true,authorities);

    }
}