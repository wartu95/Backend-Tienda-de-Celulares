package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.UserAppWeb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserAppWebRepository extends JpaRepository<UserAppWeb,Long> {

    Optional<UserAppWeb> findByName(String name);
}
