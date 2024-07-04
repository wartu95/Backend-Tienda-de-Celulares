package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.RoleUserAppWeb;
import com.cibertec.backend.entites.StateProduct;
import com.cibertec.backend.utils.enums.ERole;
import com.cibertec.backend.utils.enums.EStateProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleUserAppWebRepository extends JpaRepository<RoleUserAppWeb, String> {


    Optional<RoleUserAppWeb> findByName(ERole name);
}