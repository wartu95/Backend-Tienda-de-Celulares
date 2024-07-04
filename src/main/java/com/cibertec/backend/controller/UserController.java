package com.cibertec.backend.controller;


import com.cibertec.backend.entites.RoleUserAppWeb;
import com.cibertec.backend.entites.UserAppWeb;
import com.cibertec.backend.repositories.IUserAppWebRepository;
import com.cibertec.backend.utils.dto.UserDTO;
import com.cibertec.backend.utils.enums.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserAppWebRepository iUserAppWebRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "create")
    public ResponseEntity<?> crateUser(@RequestBody UserDTO userDTO){

        Set<RoleUserAppWeb> roleEntitySet=userDTO.getRoles().stream()
                .map(role ->RoleUserAppWeb.builder()
                        .name(ERole.valueOf(role))
                        .build()).collect(Collectors.toSet());

        UserAppWeb userToaEntity=UserAppWeb.builder()
                .name(userDTO.getName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(roleEntitySet).
                build();

        iUserAppWebRepository.save(userToaEntity);

        return new ResponseEntity<>(userToaEntity,HttpStatus.OK);
    }



}