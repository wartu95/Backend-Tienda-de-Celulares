package com.cibertec.backend.utils.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {


    @NotBlank
    private String name;

    @NotBlank
    private String password;

    private Set<String> roles;
}
